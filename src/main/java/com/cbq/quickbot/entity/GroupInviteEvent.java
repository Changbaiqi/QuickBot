package com.cbq.quickbot.entity;

import com.cbq.quickbot.entity.action.GroupAddRequestOperation;
import com.cbq.quickbot.entity.action.GroupAdminOperation;

public class GroupInviteEvent {
    private GroupRequest request;
    private GroupAddRequestOperation.Builder groupAddRequestOperation;

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


    public GroupAddRequestOperation.Builder getGroupAddRequestOperation() {
        return groupAddRequestOperation;
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
            groupInviteEvent.groupAddRequestOperation = new GroupAddRequestOperation.Builder()
                    .flag(groupInviteEvent.request.getFlag())
                    .subType(groupInviteEvent.request.getSub_type());
            return groupInviteEvent;
        }
    }
}
