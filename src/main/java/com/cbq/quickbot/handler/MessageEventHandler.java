package com.cbq.quickbot.handler;

import com.cbq.quickbot.annotation.EventFilter;
import com.cbq.quickbot.annotation.GroupListen;
import com.cbq.quickbot.bot.GroupEvent;
import com.cbq.quickbot.bot.QuickBotApplication;
import com.cbq.quickbot.entity.Message;
import com.cbq.quickbot.entity.action.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MessageEventHandler {
    public MessageEventHandler(QuickBotApplication application,Object clazzObject,Object methodParam, Method method){

        int state = runMethod(application,clazzObject, methodParam, method);
    }




    /**
     * 0代表正常请求，1代表continue，2代表return；
     *
     * @param clazzObject
     * @param method
     * @return
     */
    private int runMethod(QuickBotApplication application, Object clazzObject, Object methodParam, Method method) {
        ObjectMapper objectMapper = new ObjectMapper();

        //通过Bean调用方法执行------------------------------------------------
        new Thread(() -> {
            Object invoke = null;
            try {
                //获取返回的对象信息
                invoke = method.invoke(clazzObject, methodParam);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }


            //判断是否为Message信息，如果是Message信息那么我们就可以发送了------------------------
            if (invoke instanceof Message) {
                Message message = (Message) invoke;

                List<QQOperation> operationList = message.getOperationList();
                for (QQOperation qqOperation : operationList) {
                    //设置群头衔事件判断---------------------------------------
                    if (qqOperation.getAction().equals("set_group_special_title")) {
                        GroupSpecialTitleOperation groupSpecialTitleOperation = (GroupSpecialTitleOperation) qqOperation;
                        ActionSubmit submit = new ActionSubmit.Builder()
                                .setAciton("set_group_special_title")
                                .addParam("group_id", groupSpecialTitleOperation.getGroup_id())
                                .addParam("user_id", groupSpecialTitleOperation.getUser_id())
                                .addParam("special_title", groupSpecialTitleOperation.getSpecial_title())
                                .addParam("duration", groupSpecialTitleOperation.getDuration()).build();

                        application.getCqClient().getWebSocket().send(submit.getJson());
                    }else

                        //踢人---------------------
                        if (qqOperation.getAction().equals("set_group_kick")) {
                            GroupKickOperation groupSpecialTitleOperation = (GroupKickOperation) qqOperation;
                            ActionSubmit submit = new ActionSubmit.Builder()
                                    .setAciton("set_group_kick")
                                    .addParam("group_id", groupSpecialTitleOperation.getGroup_id())
                                    .addParam("user_id", groupSpecialTitleOperation.getUser_id())
                                    .addParam("reject_add_request", groupSpecialTitleOperation.getReject_add_request())
                                    .build();
                            application.getCqClient().getWebSocket().send(submit.getJson());
                        }else

                            //信息撤回----------------------
                            if(qqOperation.getAction().equals("delete_msg")){
                                DeleteMsgOperation deleteMsgOperation = (DeleteMsgOperation) qqOperation;
                                ActionSubmit submit = new ActionSubmit.Builder()
                                        .setAciton("delete_msg")
                                        .addParam("message_id",deleteMsgOperation.getMessage_id())
                                        .build();
                                application.getCqClient().getWebSocket().send(submit.getJson());
                            }else
                                //群打卡
                                if(qqOperation.getAction().equals("send_group_sign")){
                                    SendGroupSignOperation sendGroupSignOperation = (SendGroupSignOperation) qqOperation;
                                    ActionSubmit submit = new ActionSubmit.Builder()
                                            .setAciton(sendGroupSignOperation.getAction())
                                            .addParam("group_id",sendGroupSignOperation.getGroup_id())
                                            .build();
                                    application.getCqClient().getWebSocket().send(submit.getJson());
                                }else
                                    //群单人禁言
                                    if(qqOperation.getAction().equals("set_group_ban")){
                                        GroupBanOperation ban = (GroupBanOperation) qqOperation;
                                        ActionSubmit submit = new ActionSubmit.Builder()
                                                .setAciton(ban.getAction())
                                                .addParam("group_id",ban.getGroup_id())
                                                .addParam("user_id",ban.getUser_id())
                                                .addParam("duration",ban.getDuration())
                                                .build();
                                        application.getCqClient().getWebSocket().send(submit.getJson());
                                    }else
                                        //群添加管理
                                    if(qqOperation.getAction().equals("set_group_admin")){
                                        GroupAdminOperation admin = (GroupAdminOperation) qqOperation;
                                        ActionSubmit submit = new ActionSubmit.Builder()
                                                .setAciton(admin.getAction())
                                                .addParam("group_id",admin.getGroup_id())
                                                .addParam("user_id",admin.getUser_id())
                                                .addParam("enable",admin.getEnable())
                                                .build();
                                        application.getCqClient().getWebSocket().send(submit.getJson());
                                    }else
                                        //群要求或者加群请求
                                        if(qqOperation.getAction().equals("set_group_add_request")){
                                            GroupAddRequestOperation operation = (GroupAddRequestOperation) qqOperation;
                                            ActionSubmit submit = new ActionSubmit.Builder()
                                                    .setAciton(operation.getAction())
                                                    .addParam("flag",operation.getFlag())
                                                    .addParam("sub_type",operation.getSubType())
                                                    .addParam("approve",operation.getApprove())
                                                    .addParam("reason",operation.getReason())
                                                    .build();
                                            application.getCqClient().getWebSocket().send(submit.getJson());
                                        }
                }
            }
        }).start();


        //发送信息之后条件拦截--------------------------------------
        if (method.isAnnotationPresent(EventFilter.class)) {
            EventFilter eventFilter = method.getAnnotation(EventFilter.class);
            //判断是否事件截断
            if (eventFilter.isCut()) {
                return 2;
            }
        }

        return 0;
    }



}
