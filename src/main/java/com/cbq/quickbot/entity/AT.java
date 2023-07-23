package com.cbq.quickbot.entity;

import lombok.Data;


public class AT {
    private Long qq;

    public AT() {
    }

    public AT(Long qq) {
        this.qq = qq;
    }

    public Long getQq() {
        return qq;
    }
}
