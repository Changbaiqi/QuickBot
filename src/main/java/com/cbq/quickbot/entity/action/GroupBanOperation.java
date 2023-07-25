package com.cbq.quickbot.entity.action;

/**
 * @description: 群单人禁言
 * @author 长白崎
 * @date 2023/7/25 0:47
 * @version 1.0
 */
public class GroupBanOperation extends QQOperation{
    private Long group_id;
    private Long user_id;
    private Long duration;

    public Long getGroup_id() {
        return group_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Long getDuration() {
        return duration;
    }

    public static class Builder{
        private GroupBanOperation groupBanOperation;
        public Builder(){
            groupBanOperation = new GroupBanOperation();
            groupBanOperation.setAction("set_group_ban");
        }

        public Builder groupId(Long groupId){
            groupBanOperation.group_id = groupId;
            return this;
        }

        public Builder userId(Long userId){
            groupBanOperation.user_id = userId;
            return this;
        }

        public Builder duration(Long duration){
            groupBanOperation.duration = duration;
            return this;
        }
        public GroupBanOperation build(){
            return groupBanOperation;
        }
    }
}
