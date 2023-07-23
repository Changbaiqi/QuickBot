package com.cbq.quickbot.bot;

import com.alibaba.fastjson2.JSONObject;
import com.cbq.quickbot.entity.ReceiveMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Map;

@Data
public class GroupEvent {
    //接收的原始信息数据
    private ReceiveMessage  originalMessage;
    //private

    public static GroupEvent create(String jsonText){
        ObjectMapper objectMapper = new ObjectMapper();
        GroupEvent groupEvent = new GroupEvent();
        try {
            ReceiveMessage receiveMessage = objectMapper.readValue(jsonText, ReceiveMessage.class);
            groupEvent.setOriginalMessage(receiveMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return groupEvent;
    }

    public ReceiveMessage getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(ReceiveMessage originalMessage) {
        this.originalMessage = originalMessage;
    }
}
