package com.cbq.quickbot.handler;

import com.cbq.quickbot.annotation.GroupListen;
import com.cbq.quickbot.bot.GroupEvent;
import com.cbq.quickbot.bot.QuickBotApplication;
import com.cbq.quickbot.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GroupEventHandler {
    public GroupEventHandler(String jsonText, QuickBotApplication application){
        ObjectMapper objectMapper = new ObjectMapper();
        GroupEvent groupEvent = GroupEvent.create(jsonText);
        application.getBotlistenMap().forEach((key,value)->{
            Method[] declaredMethods = value.getClass().getDeclaredMethods();
            //System.out.println("方法有");
            for(Method method: declaredMethods){
                //是否带有群监听
                if(!method.isAnnotationPresent(GroupListen.class))
                    continue;

                GroupListen groupListen = method.getAnnotation(GroupListen.class);
                long[] qqList = groupListen.qqList();

                boolean isFilter = true;
                for(int i= 0; i <qqList.length; ++i){
                    if(groupEvent.getOriginalMessage().getGroup_id().compareTo(qqList[i])==0) {
                        isFilter = false;
                        break;
                    }
                }
                if(isFilter)
                    continue;

                try {
                    Object invoke = method.invoke(value, groupEvent);
                    if(invoke instanceof Message){
                        Message message = (Message) invoke;
                        Map<String,Object> result = new HashMap<>();
                        result.put("action","send_group_msg");
                        Map<String,Object> params= new HashMap<>();
                        params.put("group_id",groupEvent.getOriginalMessage().getGroup_id());
                        params.put("message",message.getText());
                        result.put("params",params);
                        System.out.println("要发送的信息为："+message.getText());
                        try {
                            String resultJson = objectMapper.writeValueAsString(result);
                            System.out.println(resultJson);
                            application.getCqClient().getWebSocket().send(resultJson);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                //System.out.println(method.getName());
            }
        });
    }
}
