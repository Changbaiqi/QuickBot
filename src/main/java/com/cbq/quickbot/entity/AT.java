package com.cbq.quickbot.entity;

import lombok.Data;


public class AT extends CQCode{
    private Long qq;

    public AT() {
    }

    public AT(Long qq) {
        setType("at");
        this.qq = qq;
    }

    public Long getQq() {
        return qq;
    }

    public String getCQText(){
        return "[CQ:"+getType()+",qq="+qq+"] ";
    }
}
