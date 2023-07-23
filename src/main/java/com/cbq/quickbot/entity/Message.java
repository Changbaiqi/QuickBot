package com.cbq.quickbot.entity;

import java.util.List;

public class Message {
    private StringBuffer messageBuffer;


    public String getText() {
        return messageBuffer.toString();
    }

    public void setMessageBuffer(StringBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    public static class Builder{
        private Message message = new Message();

        public Builder(){
            message.messageBuffer = new StringBuffer();
        }
        public Builder at(AT at){
            message.messageBuffer.append(at.getCQText());
            return this;
        }
        public Builder text(String text){
            message.messageBuffer.append(text);
            return this;
        }
        public Message build(){
            return message;
        }
    }
}
