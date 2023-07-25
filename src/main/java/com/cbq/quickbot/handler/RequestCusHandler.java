package com.cbq.quickbot.handler;

import com.cbq.quickbot.entity.GroupInviteEvent;
import com.cbq.quickbot.entity.Message;

/**
 * @description: 相关请求事件
 * @author 长白崎
 * @date 2023/7/25 14:49
 * @version 1.0
 */
public interface RequestCusHandler {

    /**
     * 群请求事件
     * @param groupInviteEvent
     */
    public Message requestGroupInvite(GroupInviteEvent groupInviteEvent);

}
