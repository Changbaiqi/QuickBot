package com.cbq.quickbot.handler;

import com.cbq.quickbot.entity.GroupInviteEvent;
import com.cbq.quickbot.entity.GroupRequest;

/**
 * @description: 相关请求事件
 * @author 长白崎
 * @date 2023/7/25 14:49
 * @version 1.0
 */
public interface RequestHandler {

    /**
     * 群请求事件
     * @param groupInviteEvent
     */
    public void requestGroupInvite(GroupInviteEvent groupInviteEvent);
    public void requestGroupAdd();

}
