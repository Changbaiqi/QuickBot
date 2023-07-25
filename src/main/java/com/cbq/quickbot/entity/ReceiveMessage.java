package com.cbq.quickbot.entity;

import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveMessage {
    private Long botQQ;
    private Long messageId;
    private Boolean isAtBot=false;
    private Long sendQQ;
    private Long sendGroupQQ;
    private Reply reply;
    private List<AT> atList;
    //private List<Msg>
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

    public Long getMessageId() {
        return messageId;
    }

    public Reply getReply() {
        return reply;
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
            receiveMessage.messageId = originalMessage.getMessage_id();

            //替换转换后的聊天
            receiveMessage.textMessage = CQturn(message);
            return this;
        }

        /**
         * 用于CQ码的转换
         * @param message
         * @return
         */
        private String CQturn(String message){
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

            //回复引用信息转换----------------------------
            Pattern replyPattern = Pattern.compile("\\[CQ:reply,id=([^\\]]*)\\]");
            Matcher replyMatcher = replyPattern.matcher(message);
            while (replyMatcher.find()){
                String group = replyMatcher.group(1);
                receiveMessage.reply = new Reply(Long.parseLong(group));
                message = message.replace("[CQ:reply,id="+group+"] ","");
            }

            //匹配表情----------------------
            Pattern facePattern = Pattern.compile("\\[CQ:face,id=([^\\]]*)\\]");
            Matcher faceMatcher = facePattern.matcher(message);
            while (faceMatcher.find()){
                String group = faceMatcher.group(1);
                message = message.replace("[CQ:face,id="+group+"] ","");
            }
            return message;
        }

        public ReceiveMessage build(){
            return receiveMessage;
        }
    }
}
