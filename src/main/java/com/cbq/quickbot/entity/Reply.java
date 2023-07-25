package com.cbq.quickbot.entity;

/**
 * @description: 回复封装
 * @author 长白崎
 * @date 2023/7/25 1:41
 * @version 1.0
 */
public class Reply extends CQCode{
    Long messageId;

    public Reply(Long messageId) {
        setType("reply");
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getCQText(){
        return "[CQ:"+getType()+",id="+messageId+"] ";
    }
}
