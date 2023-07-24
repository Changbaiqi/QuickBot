package com.cbq.quickbot.entity;

public abstract class QQOperation {
    private String action;
    public String getAction(){
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
