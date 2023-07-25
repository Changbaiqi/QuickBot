package com.cbq.quickbot.entity;

public class Poke extends CQCode{
    private Long qq;

    public Poke(Long qq) {
        setType("poke");
        this.qq = qq;
    }

    public Long getQq() {
        return qq;
    }

    public String getCQText(){
        return "[CQ:"+getType()+",qq="+qq+"] ";
    }
}
