package com.cbq.quickbot.entity;

public class GroupRequest {
    private String post_type;
    private String request_type;
    private Long time;
    private Long self_id;
    private String sub_type;
    private String flag;
    private Long group_id;
    private Long user_id;
    private Long invitor_id;
    private String comment;

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getSelf_id() {
        return self_id;
    }

    public void setSelf_id(Long self_id) {
        this.self_id = self_id;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getInvitor_id() {
        return invitor_id;
    }

    public void setInvitor_id(Long invitor_id) {
        this.invitor_id = invitor_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
