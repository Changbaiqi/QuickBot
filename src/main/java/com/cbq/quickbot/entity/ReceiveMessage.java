package com.cbq.quickbot.entity;

import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveMessage {
    private Long botQQ;
    private Boolean isAtBot=false;
    private Long sendQQ;
    private Long sendGroupQQ;
    private List<AT> atList;
    private String textMessage;

    public Long getSendQQ() {
        return sendQQ;
    }

    public Long getSendGroupQQ() {
        return sendGroupQQ;
    }

    public List<AT> getAtList() {
        return atList;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public Long getBotQQ() {
        return botQQ;
    }

    public Boolean getAtBot() {
        return isAtBot;
    }

    /**
     * 返回第一个AT
     * @return
     */
    public AT getFistAt(){
        if(atList.size()==0)
            return null;
        else
           return atList.get(0);
    }
    public static class Builder{
        ReceiveMessage receiveMessage= new ReceiveMessage();

        public Builder(){
            receiveMessage.atList = new ArrayList<>();
        }


        public Builder turn(OriginalMessage originalMessage){
            String message = originalMessage.getMessage();

            receiveMessage.botQQ = originalMessage.getSelf_id();
            receiveMessage.sendQQ = originalMessage.getUser_id();
            receiveMessage.sendGroupQQ = originalMessage.getGroup_id();

            //at信息转换
            Pattern atPattern = Pattern.compile("\\[CQ:at,qq=([\\d]*)\\]");
            Matcher atMatcher = atPattern.matcher(message);
            while (atMatcher.find()){
                String group = atMatcher.group(1);
                if(receiveMessage.botQQ.compareTo(Long.parseLong(group))!=0)
                    receiveMessage.atList.add(new AT(Long.parseLong(group)));
                else
                    receiveMessage.isAtBot = true;
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
