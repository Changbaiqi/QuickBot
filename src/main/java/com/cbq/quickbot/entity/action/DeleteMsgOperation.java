package com.cbq.quickbot.entity.action;

/**
 * @description: 撤回信息
 * @author 长白崎
 * @date 2023/7/25 0:47
 * @version 1.0
 */
public class DeleteMsgOperation extends QQOperation{

    private Long message_id;
    public DeleteMsgOperation(){

    }

    public Long getMessage_id() {
        return message_id;
    }

    public static class Builder{
        private DeleteMsgOperation deleteMsgOperation;
        public Builder(){
            deleteMsgOperation = new DeleteMsgOperation();
            deleteMsgOperation.setAction("delete_msg");
        }

        public Builder messageId(Long messageId){
            deleteMsgOperation.message_id = messageId;
            return this;
        }
        public DeleteMsgOperation build(){
            return deleteMsgOperation;
        }
    }
}
