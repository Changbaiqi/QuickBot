package com.cbq.quickbot.bot;

import com.alibaba.fastjson2.JSONObject;
import com.cbq.quickbot.entity.OriginalMessage;
import com.cbq.quickbot.entity.ReceiveMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Map;

public class GroupEvent {
    //接收的原始信息数据
    private OriginalMessage originalMessage;
    private ReceiveMessage receiveMessage;

    public OriginalMessage getOriginalMessage() {
        return originalMessage;
    }

    public ReceiveMessage getReceiveMessage() {
        return receiveMessage;
    }

    public static class Builder {
        GroupEvent groupEvent = null;

        public Builder() {
            groupEvent = new GroupEvent();
        }

        public Builder jsonText(String jsonText) {
            ObjectMapper objectMapper = new ObjectMapper();

            ///装载信息数据
            try {
                OriginalMessage originalMessage = objectMapper.readValue(jsonText, OriginalMessage.class);
                groupEvent.originalMessage = originalMessage;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            //转换格式化之后的信息
            ReceiveMessage receive = new ReceiveMessage.Builder()
                    .turn(groupEvent.originalMessage)
                    .build();

            groupEvent.receiveMessage = receive;
            return this;
        }

        public GroupEvent build() {
            return groupEvent;
        }


    }


}
