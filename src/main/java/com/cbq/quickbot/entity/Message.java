package com.cbq.quickbot.entity;

import com.cbq.quickbot.entity.action.QQOperation;

import java.util.ArrayList;
import java.util.List;

public class Message {
    //普通发送的信息
    private StringBuffer messageBuffer;

    //QQ机器人的一些操作
    List<QQOperation> operationList;

    public String getText() {
        return messageBuffer.toString();
    }

    public List<QQOperation> getOperationList() {
        return operationList;
    }

    public void setMessageBuffer(StringBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    public static class Builder{
        private Message message = new Message();

        public Builder(){
            message.messageBuffer = new StringBuffer();
            message.operationList = new ArrayList<>();
        }
        public Builder at(AT at){
            message.messageBuffer.append(at.getCQText());
            return this;
        }
        public Builder reply(Long messageId){
            message.messageBuffer.append(new Reply(messageId).getCQText());
            return this;
        }
        public Builder reply(Reply reply){
            message.messageBuffer.append(reply.getCQText());
            return this;
        }

        public Builder text(String text){
            message.messageBuffer.append(text);
            return this;
        }
        public Builder addOperation(QQOperation operation){
            message.operationList.add(operation);
            return this;
        }
        public Message build(){
            return message;
        }
    }
}
