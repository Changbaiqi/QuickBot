package com.cbq.quickbot.handler;

import com.cbq.quickbot.bot.QuickBotApplication;
import com.cbq.quickbot.entity.GroupInviteEvent;
import com.cbq.quickbot.entity.GroupRequest;
import com.cbq.quickbot.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.Map;

public class RequestHandler {
    public RequestHandler(String textJson, QuickBotApplication application) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String,Object> map = objectMapper.readValue(textJson, Map.class);
            application.getBotlistenMap().forEach((key, value) -> {
                try {
                    if (value instanceof RequestCusHandler) {
                        if (map.get("request_type").equals("group")) {
                            GroupRequest groupRequest = objectMapper.readValue(textJson, GroupRequest.class);
                            if(groupRequest.getSub_type().equals("invite")){
                                GroupInviteEvent groupInviteEvent = new GroupInviteEvent.Builder()
                                        .groupRequest(groupRequest)
                                        .build();
                                Message message = ((RequestCusHandler) value).requestGroupInvite(groupInviteEvent);
                                if (message!=null){
                                    Method requestGroupInvite = value.getClass().getDeclaredMethod("requestGroupInvite", groupInviteEvent.getClass());
                                    new MessageEventHandler(application,value,groupInviteEvent,requestGroupInvite);
                                }
                            }
                        }
                        return;
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
