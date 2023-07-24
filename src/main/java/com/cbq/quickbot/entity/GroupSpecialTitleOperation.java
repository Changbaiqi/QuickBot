package com.cbq.quickbot.entity;

import lombok.Builder;
import lombok.Data;

@Builder
public class GroupSpecialTitleOperation extends QQOperation{

    private Long group_id;
    private Long user_id;
    private String special_title;
    private Long duration;

    public GroupSpecialTitleOperation(Long group_id, Long user_id, String special_title, Long duration) {
        this.group_id = group_id;
        this.user_id = user_id;
        this.special_title = special_title;
        this.duration = duration;
        setAction("set_group_special_title");
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
}
