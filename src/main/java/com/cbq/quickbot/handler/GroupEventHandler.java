package com.cbq.quickbot.handler;

import com.cbq.quickbot.annotation.EventFilter;
import com.cbq.quickbot.annotation.GroupListen;
import com.cbq.quickbot.bot.GroupEvent;
import com.cbq.quickbot.bot.QuickBotApplication;
import com.cbq.quickbot.entity.AT;
import com.cbq.quickbot.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class GroupEventHandler {
    public GroupEventHandler(String jsonText, QuickBotApplication application) {
        //System.out.println(jsonText);
        ObjectMapper objectMapper = new ObjectMapper();
        //构建群事件
        GroupEvent groupEvent = new GroupEvent.Builder().jsonText(jsonText).build();

        //获取Application中的监听对象列表
        application.getBotlistenMap().forEach((key, value) -> {
            Method[] declaredMethods = value.getClass().getDeclaredMethods();


            //遍历获取次类中的方法，看是否带了群监听注解-------------------------------
            for (Method method : declaredMethods) {

                //是否带有群监听-----------------
                if (!method.isAnnotationPresent(GroupListen.class))
                    continue;
                GroupListen groupListen = method.getAnnotation(GroupListen.class);


                //是否带有群事件过滤器如果带，那么就加上去--------------
                if (method.isAnnotationPresent(EventFilter.class)) {
                    EventFilter eventFilter = method.getAnnotation(EventFilter.class);
                    //是否开启了atBot---------
                    boolean atBot = eventFilter.atBot();
                    if (atBot == true) {
                        boolean isFilter = true;
                        for (AT at:groupEvent.getReceiveMessage().getAtList()) {
                            if (groupEvent.getOriginalMessage().getSelf_id().compareTo(at.getQq()) == 0) {
                                isFilter = false;
                            }
                        }
                        if (isFilter)
                            return;
                    }

                    //是否含有正则匹配事件--------------------
                    String rex = eventFilter.rex();
                    if(rex!=""){
                        boolean isFilter = true;
                        if(groupEvent.getReceiveMessage().getTextMessage().contains(rex)){
                            isFilter =false;
                        }
                        if(isFilter)
                            return;
                    }

                }


                //获取监听的群
                long[] qqList = groupListen.qqList();


                //此部分用于过滤群号监听条件------------------
                boolean isFilter = true;
                if (qqList.length != 0) {
                    for (int i = 0; i < qqList.length; ++i) {
                        if (groupEvent.getOriginalMessage().getGroup_id().compareTo(qqList[i]) == 0) {
                            isFilter = false;
                            break;
                        }
                    }
                } else
                    isFilter = false;
                if (isFilter)
                    continue;

                Object invoke = null;
                try {
                    //获取返回的对象信息
                    invoke = method.invoke(value, groupEvent);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }


                //判断是否为Message信息，如果是Message信息那么我们就可以发送了----------
                if (invoke instanceof Message) {
                    Message message = (Message) invoke;
                    Map<String, Object> result = new HashMap<>();
                    result.put("action", "send_group_msg");
                    Map<String, Object> params = new HashMap<>();
                    params.put("group_id", groupEvent.getOriginalMessage().getGroup_id());
                    params.put("message", message.getText());
                    result.put("params", params);
                    System.out.println("要发送的信息为：" + message.getText());
                    try {
                        String resultJson = objectMapper.writeValueAsString(result);
                        System.out.println(resultJson);
                        application.getCqClient().getWebSocket().send(resultJson);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
    }
}
