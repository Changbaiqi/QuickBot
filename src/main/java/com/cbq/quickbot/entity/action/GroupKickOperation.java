package com.cbq.quickbot.entity.action;

/**
 * @description: 群踢人
 * @author 长白崎
 * @date 2023/7/25 0:48
 * @version 1.0
 */
public class GroupKickOperation extends QQOperation{
    //群号
    private Long group_id;
    //要踢的QQ号
    private Long user_id;
    //是否拒绝此人的加群申请
    private Boolean reject_add_request;

    public Long getGroup_id() {
        return group_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Boolean getReject_add_request() {
        return reject_add_request;
    }

    public static class Builder{
        private GroupKickOperation groupKickOperation;
        public Builder(){
            groupKickOperation = new GroupKickOperation();
            groupKickOperation.setAction("set_group_kick");
        }

        public Builder groupId(Long groupId){
            groupKickOperation.group_id = groupId;
            return this;
        }

        public Builder userId(Long userId){
            groupKickOperation.user_id = userId;
            return this;
        }

        public Builder rejectAddRequest(Boolean rejectAddRequest){
            groupKickOperation.reject_add_request = rejectAddRequest;
            return this;
        }

        public GroupKickOperation build(){
            return groupKickOperation;
        }


    }
}
