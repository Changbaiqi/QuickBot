package com.cbq.quickbot.entity;

import java.util.HashMap;
import java.util.Map;

public class MetaMessage {
    private String type;
    private Map<String ,Object> data;

    public String getType() {
        return type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public static class Builder{
        private MetaMessage message;
        public Builder(){
            message = new MetaMessage();
            message.data = new HashMap<>();
        }

        public Builder type(String type){
            message.type = type;
            return this;
        }

        public Builder addParam(String key,Object value){
            message.data.put(key,value);
            return this;
        }

        public MetaMessage build(){
            return message;
        }

    }
}
