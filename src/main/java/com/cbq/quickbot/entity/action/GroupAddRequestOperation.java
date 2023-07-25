package com.cbq.quickbot.entity.action;

public class GroupAddRequestOperation extends QQOperation{
    private String flag;
    private String sub_type;
    private Boolean approve;
    private String reason;

    public String getFlag() {
        return flag;
    }

    public String getSubType() {
        return sub_type;
    }

    public Boolean getApprove() {
        return approve;
    }

    public String getReason() {
        return reason;
    }


    public static class Builder{
        private GroupAddRequestOperation groupAddRequestOperation;

        public Builder(){
            groupAddRequestOperation = new GroupAddRequestOperation();
            groupAddRequestOperation.setAction("set_group_add_request");
        }

        public Builder flag(String flag){
            groupAddRequestOperation.flag = flag;
            return this;
        }

        public Builder subType(GROUP type){
            if(type.equals(GROUP.ADD))
                groupAddRequestOperation.sub_type = "add";
            else if(type.equals(GROUP.INVITE))
                groupAddRequestOperation.sub_type = "invite";

            return this;
        }
        public Builder subType(String type){
                groupAddRequestOperation.sub_type = type;

            return this;
        }

        public Builder approve(Boolean approve){
            groupAddRequestOperation.approve = approve;
            return this;
        }

        public Builder reason(String reason){
            groupAddRequestOperation.reason = reason;
            return this;
        }
        public GroupAddRequestOperation build(){
            return groupAddRequestOperation;
        }
    }
}
