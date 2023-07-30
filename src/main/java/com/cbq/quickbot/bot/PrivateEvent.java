package com.cbq.quickbot.bot;

import com.cbq.quickbot.entity.OriginalMessage;
import com.cbq.quickbot.entity.ReceiveMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrivateEvent {
    private String jsonText;
    //接收的原始信息数据
    private OriginalMessage originalMessage;
    private ReceiveMessage receiveMessage;

    public OriginalMessage getOriginalMessage() {
        return originalMessage;
    }

    public ReceiveMessage getReceiveMessage() {
        return receiveMessage;
    }

    public String getJsonText() {
        return jsonText;
    }

    public static class Builder {
        PrivateEvent privateEvent = null;

        public Builder() {
            privateEvent = new PrivateEvent();
        }

        public Builder jsonText(String jsonText) {
            ObjectMapper objectMapper = new ObjectMapper();
            privateEvent.jsonText= jsonText;
            ///装载信息数据
            try {
                OriginalMessage originalMessage = objectMapper.readValue(jsonText, OriginalMessage.class);
                privateEvent.originalMessage = originalMessage;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            //转换格式化之后的信息
            ReceiveMessage receive = new ReceiveMessage.Builder()
                    .turn(privateEvent.originalMessage)
                    .build();

            privateEvent.receiveMessage = receive;
            return this;
        }

        public PrivateEvent build() {
            return privateEvent;
        }


    }


}
