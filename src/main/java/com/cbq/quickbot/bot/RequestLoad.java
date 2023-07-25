package com.cbq.quickbot.bot;

import com.cbq.quickbot.entity.GroupInviteEvent;
import com.cbq.quickbot.entity.GroupRequest;
import com.cbq.quickbot.handler.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class RequestLoad {
    public RequestLoad(String textJson, QuickBotApplication application) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String,Object> map = objectMapper.readValue(textJson, Map.class);
            application.getBotlistenMap().forEach((key, value) -> {
                try {
                    if (value instanceof RequestHandler) {
                        if (map.get("request_type").equals("group")) {
                            GroupRequest groupRequest = objectMapper.readValue(textJson, GroupRequest.class);
                            if(groupRequest.getSub_type().equals("invite")){
                                GroupInviteEvent groupInviteEvent = new GroupInviteEvent.Builder()
                                        .groupRequest(groupRequest)
                                        .build();
                                ((RequestHandler) value).requestGroupInvite(groupInviteEvent);
                            }
                        }
                        //((RequestHandler) value).requestGroup();
                        return;
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
