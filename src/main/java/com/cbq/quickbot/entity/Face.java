package com.cbq.quickbot.entity;

public class Face extends CQCode{
    private Long faceId;

    public Face(Long faceId) {
        setType("face");
        this.faceId = faceId;
    }

    public Long getFaceId() {
        return faceId;
    }

    public String getCQText(){
        return "[CQ:"+getType()+",id="+faceId+"] ";
    }
}
