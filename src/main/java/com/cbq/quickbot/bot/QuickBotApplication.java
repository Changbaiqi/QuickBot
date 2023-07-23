package com.cbq.quickbot.bot;

import com.cbq.quickbot.annotation.BotListen;
import com.cbq.quickbot.annotation.EnableQuickBot;
import com.cbq.quickbot.entity.BeanClass;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.annotation.Resource;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class QuickBotApplication {
    private Class startClass;

    //用于存储扫描到的Bean的Class
    private ConcurrentMap<String, BeanClass> classMap = new ConcurrentHashMap<>();

    //Bot监听对象
    private ConcurrentMap<String,Object> botlistenMap = new ConcurrentHashMap<>();


    private CQClient cqClient;
    public QuickBotApplication(Class startClass) {
        this.startClass = startClass;
        initBot();
    }

    /**
     * 检测是否开启了QuickBot，如果开启了那么就返回true，如果未开启那么久返回false
     * @param startClass 启动类Class
     * @return
     */
    public boolean isOpen(Class startClass){

        if(startClass.isAnnotationPresent(EnableQuickBot.class)){
            return true;
        }
        return false;
    }

    /**
     * 初始化Bot框架
     */
    public void initBot(){
        System.out.println("""
                 ________  ___  ___  ___  ________  ___  __    ________  ________  _________  \s
                |\\   __  \\|\\  \\|\\  \\|\\  \\|\\   ____\\|\\  \\|\\  \\ |\\   __  \\|\\   __  \\|\\___   ___\\\s
                \\ \\  \\|\\  \\ \\  \\\\\\  \\ \\  \\ \\  \\___|\\ \\  \\/  /|\\ \\  \\|\\ /\\ \\  \\|\\  \\|___ \\  \\_|\s
                 \\ \\  \\\\\\  \\ \\  \\\\\\  \\ \\  \\ \\  \\    \\ \\   ___  \\ \\   __  \\ \\  \\\\\\  \\   \\ \\  \\ \s
                  \\ \\  \\\\\\  \\ \\  \\\\\\  \\ \\  \\ \\  \\____\\ \\  \\\\ \\  \\ \\  \\|\\  \\ \\  \\\\\\  \\   \\ \\  \\\s
                   \\ \\_____  \\ \\_______\\ \\__\\ \\_______\\ \\__\\\\ \\__\\ \\_______\\ \\_______\\   \\ \\__\\
                    \\|___| \\__\\|_______|\\|__|\\|_______|\\|__| \\|__|\\|_______|\\|_______|    \\|__|
                          \\|__|                                                               \s
                v1.0.0
                ----------------------------------------------------------------------------------------------------------------
                """);
        //如果未开启Bot那么久不继续启动框架
        if(!isOpen(this.startClass))
            return;

        //扫描Bean
        scannerBeanClass();

//        for(String beanName : classMap.keySet()){
//            //classMap.get(beanName);
//            System.out.println("Beans》》"+beanName);
//        }
        //创建连接
        cqClient = new CQClient.Builder()
                .setURL("ws://localhost:8080")
                .application(this)
                .build();

        //System.out.println("框架启动成功");
        //防止结束
        while(true){}
    }

    /**
     * 扫描Bean的Class
     */
    public void scannerBeanClass(){
        String packageName = startClass.getPackageName();
        packageName = packageName.replace(".","/");
        //通过传进来的启动类获取类加载器
        ClassLoader classLoader = startClass.getClassLoader();
        //通过包路径获取真实文件路径
        URL resource = classLoader.getResource(packageName);
        File file = new File(resource.getFile());
        //System.out.println(file);
        scannerClass(file,startClass);
    }


    /**
     * 扫描Class
     * @param file
     * @param startClass
     */
    private void scannerClass(File file,Class startClass){
        //子文件
        File[] files = file.listFiles();
        for(File f: files){
            //如果为文件夹那么就继续扫描
            if(f.isDirectory()) {
                //System.out.println(f.getAbsolutePath());
                scannerClass(f,startClass);
                continue;
            }

            //获取文件绝对路径
            String absolutePath = f.getAbsolutePath();
            if(absolutePath.endsWith(".class")){
                //String clazzPath = f.getPath();
                String clazzNameNoEnd = f.getName().replace(".class","");
                String decapitalize = Introspector.decapitalize(clazzNameNoEnd);
                //System.out.println(decapitalize);
                ClassLoader classLoader = startClass.getClassLoader();
                try {
                    String path = startClass.getPackageName().replace(".","\\");

                    String classPath = absolutePath.substring(absolutePath.indexOf(path),f.getAbsolutePath().indexOf(".class")).replace("\\",".");
                    //System.out.println(classPath);
                    Class<?> aClass = classLoader.loadClass(classPath);
                    //检出如果是bot监听对象
                    if(aClass.isAnnotationPresent(BotListen.class)) {
                        if (!classMap.containsKey(clazzNameNoEnd)) {
                            classMap.put(decapitalize, createBean(clazzNameNoEnd, aClass));
                            botlistenMap.put(decapitalize,aClass.newInstance());
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 创建Bean
     * @param name
     * @param beanClass
     * @return
     */
    public BeanClass createBean(String name,Class beanClass) {
        BeanClass beanClass1 = new BeanClass();
        beanClass1.setName(name);
        beanClass1.setBeanClass(beanClass);
        return beanClass1;
    }

    /**
     * 设置Bean
     */
    public void setBean(){

    }

    /**
     * 获取Bean
     */
    public void getBean(){

    }

    public ConcurrentMap<String, BeanClass> getClassMap() {
        return classMap;
    }

    public ConcurrentMap<String, Object> getBotlistenMap() {
        return botlistenMap;
    }

    public CQClient getCqClient() {
        return cqClient;
    }
}
