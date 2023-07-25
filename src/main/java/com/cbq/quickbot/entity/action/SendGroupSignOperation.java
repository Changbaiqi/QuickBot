package com.cbq.quickbot.entity.action;

/**
 * @description: 群签到
 * @author 长白崎
 * @date 2023/7/25 0:50
 * @version 1.0
 */
public class SendGroupSignOperation extends QQOperation{
    private Long group_id;

    public Long getGroup_id() {
        return group_id;
    }

    public static class Builder{
        private SendGroupSignOperation sendGroupSignOperation;
        public Builder(){
            sendGroupSignOperation = new SendGroupSignOperation();
            sendGroupSignOperation.setAction("send_group_sign");
        }
        public Builder groupId(Long groupId){
            sendGroupSignOperation.group_id = groupId;
            return this;
        }
        public SendGroupSignOperation build(){
            return sendGroupSignOperation;
        }
    }
}
