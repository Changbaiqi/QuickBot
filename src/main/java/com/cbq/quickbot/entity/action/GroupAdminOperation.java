package com.cbq.quickbot.entity.action;

/**
 * @description: 设置管理员
 * @author 长白崎
 * @date 2023/7/25 1:20
 * @version 1.0
 */
public class GroupAdminOperation extends QQOperation{
    private Long group_id;
    private Long user_id;
    private Boolean enable;

    public Long getGroup_id() {
        return group_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Boolean getEnable() {
        return enable;
    }


    public static class Builder{
        private GroupAdminOperation groupAdminOperation;
        public Builder(){
            groupAdminOperation = new GroupAdminOperation();
            groupAdminOperation.setAction("set_group_admin");
        }

        public Builder groupId(Long groupId){
            groupAdminOperation.group_id = groupId;
            return this;
        }

        public Builder userId(Long userId){
            groupAdminOperation.user_id = userId;
            return this;
        }

        /**
         * false为取消管理员，true为设置为管理员
         * @param enable
         * @return
         */
        public Builder enable(Boolean enable){
            groupAdminOperation.enable = enable;
            return this;
        }

        public GroupAdminOperation build(){
            return groupAdminOperation;
        }
    }

}
