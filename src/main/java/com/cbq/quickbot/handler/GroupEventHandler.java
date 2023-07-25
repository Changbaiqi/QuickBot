package com.cbq.quickbot.handler;

import com.cbq.quickbot.annotation.EventFilter;
import com.cbq.quickbot.annotation.GroupListen;
import com.cbq.quickbot.bot.GroupEvent;
import com.cbq.quickbot.bot.QuickBotApplication;
import com.cbq.quickbot.entity.AT;
import com.cbq.quickbot.entity.action.*;
import com.cbq.quickbot.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @description: 群事件监听
 * @author 长白崎
 * @date 2023/7/25 0:48
 * @version 1.0
 */
@Slf4j
public class GroupEventHandler {
    public GroupEventHandler(String jsonText, QuickBotApplication application) {
        //System.out.println(jsonText);
        ObjectMapper objectMapper = new ObjectMapper();
        //构建群事件
        GroupEvent groupEvent = new GroupEvent.Builder().jsonText(jsonText).build();

        //获取Application中的监听对象列表
        application.getBotlistenMap().forEach((key, value) -> {
            Method[] declaredMethods = value.getClass().getDeclaredMethods();

            ArrayList<Method> methods = new ArrayList<>();
            for (Method declaredMethod : declaredMethods) {
                methods.add(declaredMethod);
            }
            //类中方法排序，方便后续的过滤登记标记
            methods.sort((m1, m2) -> {
                int m1V = 0;
                int m2V = 0;
                if (m1.isAnnotationPresent(EventFilter.class)) {
                    m1V = m1.getAnnotation(EventFilter.class).level();
                }
                if (m2.isAnnotationPresent(EventFilter.class)) {
                    m2V = m2.getAnnotation(EventFilter.class).level();
                }
                return m2V - m1V;
            });

            //遍历获取次类中的方法，看是否带了群监听注解-------------------------------
            for (Method method : methods) {
                int state = runMethed(application, value, groupEvent, method);
                if (state == 1)
                    continue;
                else if (state == 2)
                    return;
            }
        });
    }

    /**
     * 0代表正常请求，1代表continue，2代表return；
     *
     * @param groupEvent
     * @param method
     * @return
     */
    private int runMethed(QuickBotApplication application, Object clazzObject, GroupEvent groupEvent, Method method) {
        ObjectMapper objectMapper = new ObjectMapper();

        //是否带有群监听-----------------
        if (!method.isAnnotationPresent(GroupListen.class))
            return 1;
        GroupListen groupListen = method.getAnnotation(GroupListen.class);


        //获取监听的群
        long[] qqList = groupListen.qqList();

        //此部分用于过滤群号监听条件------------------

        if (qqList.length != 0) {
            boolean isFilter = true;
            for (int i = 0; i < qqList.length; ++i) {
                if (groupEvent.getOriginalMessage().getGroup_id().compareTo(qqList[i]) == 0) {
                    isFilter = false;
                    break;
                }
            }
            if (isFilter)
                return 1;
        }




        //发送信息前条件拦截，那么就加上去----------------------------------
        if (method.isAnnotationPresent(EventFilter.class)) {
            EventFilter eventFilter = method.getAnnotation(EventFilter.class);



            //如果设定了指定QQ号触发，那么就触进行过滤--------------
            if(eventFilter.triggerQQ().length!=0){
                long[] triggerQQList = eventFilter.triggerQQ();
                boolean isFilter = true;
                for (long qq : triggerQQList) {
                    if( groupEvent.getReceiveMessage().getSendQQ().compareTo(qq)==0){
                        isFilter = false;
                        break;
                    }
                }
                if(isFilter)
                    return 1;
            }


            //是否开启了atBot---------
            boolean atBot = eventFilter.atBot();
            if (atBot == true && !groupEvent.getReceiveMessage().getAtBot()) {
                return 1;
            }

            //是否含有正则匹配事件--------------------
            String rex = eventFilter.rex();
            if (!rex.equals("")) {
                boolean isFilter = true;
                if (groupEvent.getReceiveMessage().getTextMessage().matches(rex)) {
                    isFilter = false;
                }
                if (isFilter)
                    return 1;
            }


        }


        //通过Bean调用方法执行------------------------------------------------
        new Thread(() -> {
            Object invoke = null;
            try {
                //获取返回的对象信息
                invoke = method.invoke(clazzObject, groupEvent);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }


            //判断是否为Message信息，如果是Message信息那么我们就可以发送了------------------------
            if (invoke instanceof Message) {
                Message message = (Message) invoke;
                ActionSubmit actionSubmit = new ActionSubmit.Builder()
                        .setAciton("send_group_msg")
                        .addParam("group_id", groupEvent.getOriginalMessage().getGroup_id())
                        .addParam("message", message.getText())
                        .build();
                application.getCqClient().getWebSocket().send(actionSubmit.getJson());

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
                                if(qqOperation.getAction().equals("set_group_admin")){
                                    GroupAdminOperation admin = (GroupAdminOperation) qqOperation;
                                    ActionSubmit submit = new ActionSubmit.Builder()
                                            .setAciton(admin.getAction())
                                            .addParam("group_id",admin.getGroup_id())
                                            .addParam("user_id",admin.getUser_id())
                                            .addParam("enable",admin.getEnable())
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
