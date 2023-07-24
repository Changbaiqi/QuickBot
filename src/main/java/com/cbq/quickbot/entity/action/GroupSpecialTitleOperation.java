package com.cbq.quickbot.entity.action;

import lombok.Builder;

@Builder
public class GroupSpecialTitleOperation extends QQOperation{

    private Long group_id;
    private Long user_id;
    private String special_title;
    private Long duration;

    public GroupSpecialTitleOperation(Long group_id, Long user_id, String special_title, Long duration) {
        setAction("set_group_special_title");
        this.group_id = group_id;
        this.user_id = user_id;
        this.special_title = special_title;
        this.duration = duration;
    }
    GroupSpecialTitleOperation(){

    }


    public Long getGroup_id() {
        return group_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public String getSpecial_title() {
        return special_title;
    }

    public Long getDuration() {
        return duration;
    }

    public static class Builder{
        private GroupSpecialTitleOperation groupSpecialTitleOperation;
        public Builder(){
            groupSpecialTitleOperation = new GroupSpecialTitleOperation();
            groupSpecialTitleOperation.setAction("set_group_special_title");
        }

        public Builder groupId(Long groupId){
            groupSpecialTitleOperation.group_id = groupId;
            return this;
        }

        public Builder userId(Long userId){
            groupSpecialTitleOperation.user_id = userId;
            return this;
        }
        public Builder specialTitle(String specialTitle){
            groupSpecialTitleOperation.special_title = specialTitle;
            return this;
        }

        public Builder duration(Long duration){
            groupSpecialTitleOperation.duration = duration;
            return this;
        }

        public GroupSpecialTitleOperation build(){
            return groupSpecialTitleOperation;
        }
    }
}
