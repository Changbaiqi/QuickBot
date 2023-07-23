package com.cbq.quickbot.entity;

public class Message {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class Builder{
        private Message message = new Message();

        public Builder setMessage(String text){
            this.message.text = text;
            return this;
        }
        public Message build(){
            return message;
        }
    }
}
