package com.cbq.quickbot.entity;

import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveMessage {
    private Long sendQQ;
    private List<AT> atList;
    private String textMessage;

    public Long getSendQQ() {
        return sendQQ;
    }

    public List<AT> getAtList() {
        return atList;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public static class Builder{
        ReceiveMessage receiveMessage= new ReceiveMessage();

        public Builder(){
            receiveMessage.atList = new ArrayList<>();
        }


        public Builder turn(OriginalMessage originalMessage){
            String message = originalMessage.getMessage();

            receiveMessage.sendQQ = originalMessage.getUser_id();

            //at信息转换
            Pattern atPattern = Pattern.compile("\\[CQ:at,qq=([\\d]*)\\]");
            Matcher atMatcher = atPattern.matcher(message);
            while (atMatcher.find()){
                String group = atMatcher.group(1);
                receiveMessage.atList.add(new AT(Long.parseLong(group)));
                message = message.replace("[CQ:at,qq="+group+"] ","");
            }

            receiveMessage.textMessage = message;
            return this;
        }

        public ReceiveMessage build(){
            return receiveMessage;
        }
    }
}
