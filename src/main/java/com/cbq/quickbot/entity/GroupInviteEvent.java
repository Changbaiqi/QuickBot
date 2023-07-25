package com.cbq.quickbot.entity;

public class GroupInviteEvent {
    private GroupRequest request;

    public GroupRequest getRequest() {
        return request;
    }

    /**
     * 同意入群
     */
    public void agree(){
        
    }

    /**
     * 拒绝入群
     */
    public void deny(){

    }

    public static class Builder{
        private GroupInviteEvent groupInviteEvent;

        public Builder(){
            groupInviteEvent = new GroupInviteEvent();
        }
        public Builder groupRequest(GroupRequest groupRequest){
            groupInviteEvent.request = groupRequest;
            return this;
        }

        public GroupInviteEvent build(){
            return groupInviteEvent;
        }
    }
}
