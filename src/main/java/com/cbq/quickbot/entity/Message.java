package com.cbq.quickbot.entity;

import com.cbq.quickbot.entity.action.QQOperation;

import java.util.ArrayList;
import java.util.List;

public class Message {
    //普通发送的信息
    private List<MetaMessage> messageBuffer;

    //QQ机器人的一些操作
    List<QQOperation> operationList;

    public String getText() {
        return messageBuffer.toString();
    }

    public List<QQOperation> getOperationList() {
        return operationList;
    }

    public List<MetaMessage> getMessageBuffer() {
        return messageBuffer;
    }

    public static class Builder{
        private Message message = new Message();

        public Builder(){
            message.messageBuffer = new ArrayList<>();
            message.operationList = new ArrayList<>();
        }
        public Builder at(AT at){
            //message.messageBuffer.append(at.getCQText());
            message.messageBuffer.add(
                    new MetaMessage.Builder()
                            .type("at")
                            .addParam("qq",at.getQq()).build());
            return this;
        }
        public Builder at(Long atQQ){
            //message.messageBuffer.append(at.getCQText());
            message.messageBuffer.add(
                    new MetaMessage.Builder()
                            .type("at")
                            .addParam("qq",atQQ).build());
            return this;
        }
        public Builder reply(Long messageId){
            //message.messageBuffer.append(new Reply(messageId).getCQText());
            message.messageBuffer.add(
                    new MetaMessage.Builder()
                            .type("reply")
                            .addParam("id",messageId)
                            .build()
            );
            return this;
        }
        public Builder reply(Reply reply){
            //message.messageBuffer.append(reply.getCQText());
            message.messageBuffer.add(
                    new MetaMessage.Builder()
                            .type("reply")
                            .addParam("id",reply.getMessageId())
                            .build()
            );
            return this;
        }

        public Builder text(String text){
            message.messageBuffer.add(
                    new MetaMessage.Builder()
                            .type("text")
                            .addParam("text",text)
                            .build()
            );
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
