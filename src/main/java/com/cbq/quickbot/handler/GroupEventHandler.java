package com.cbq.quickbot.handler;

import com.cbq.quickbot.annotation.BotListen;
import com.cbq.quickbot.annotation.EventFilter;
import com.cbq.quickbot.annotation.GroupListen;
import com.cbq.quickbot.annotation.Rex;
import com.cbq.quickbot.bot.GroupEvent;
import com.cbq.quickbot.bot.QuickBotApplication;
import com.cbq.quickbot.entity.AT;
import com.cbq.quickbot.entity.MetaMessage;
import com.cbq.quickbot.entity.action.*;
import com.cbq.quickbot.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 长白崎
 * @version 1.0
 * @description: 群事件监听
 * @date 2023/7/25 0:48
 */
@Slf4j
public class GroupEventHandler {
    public GroupEventHandler(String jsonText, QuickBotApplication application) {
        //System.out.println(jsonText);
        ObjectMapper objectMapper = new ObjectMapper();
        //构建群事件
        GroupEvent groupEvent = new GroupEvent.Builder().jsonText(jsonText).build();

        List<Object> listenList = new ArrayList<>(application.getBotlistenMap().values());

        //对类级别进行排序
        listenList.sort((v1, v2) -> {
            BotListen botListen1 = v1.getClass().getAnnotation(BotListen.class);
            BotListen botListen2 = v2.getClass().getAnnotation(BotListen.class);
            return botListen2.level() - botListen1.level();
        });

        //获取Application中的监听对象列表

        for (Object value : listenList) {
            BotListen botListen = value.getClass().getAnnotation(BotListen.class);

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
            int isRunSum = 0;//用于记录这个类中的事件是否至少有一个触发
            for (Method method : methods) {
                int[] state = runMethod(application, value, groupEvent, method);

                //是否有触发事件
                if (state[0] == 1)
                    isRunSum += 1;

                if (state[1] == 1)
                    continue;
                else if (state[1] == 2)
                    break;
            }

            //监听类级别的截断器
            if (isRunSum > 0 && botListen.isCut())
                return;
        }
    }

    /**
     * 0代表正常请求，1代表continue，2代表return；[表示是否有事件执行,表示执行状态]
     *
     * @param groupEvent
     * @param method
     * @return
     */
    private int[] runMethod(QuickBotApplication application, Object clazzObject, GroupEvent groupEvent, Method method) {
        ObjectMapper objectMapper = new ObjectMapper();

        //是否带有群监听-----------------
        if (!method.isAnnotationPresent(GroupListen.class))
            return new int[]{0, 1};
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
                return new int[]{0, 1};
        }


        //发送信息前条件拦截，那么就加上去----------------------------------
        if (method.isAnnotationPresent(EventFilter.class)) {
            EventFilter eventFilter = method.getAnnotation(EventFilter.class);


            //如果设定了指定QQ号触发，那么就触进行过滤--------------
            if (eventFilter.triggerQQ().length != 0) {
                long[] triggerQQList = eventFilter.triggerQQ();
                boolean isFilter = true;
                for (long qq : triggerQQList) {
                    if (groupEvent.getReceiveMessage().getSendQQ().compareTo(qq) == 0) {
                        isFilter = false;
                        break;
                    }
                }
                if (isFilter)
                    return new int[]{0, 1};
            }


            //是否开启了atBot---------
            boolean atBot = eventFilter.atBot();
            if (atBot == true && !groupEvent.getReceiveMessage().getAtBot()) {
                return new int[]{0, 1};
            }

            //是否含有正则匹配事件--------------------
            Rex rex = eventFilter.rex();
            String rexStr = rex.rex();
            REXMODEL model = rex.model();
            if (!rexStr.equals("")) {
                boolean isFilter = true;
                String textMessage = groupEvent.getReceiveMessage().getTextMessage();
                Pattern pattern = Pattern.compile(rexStr);
                Matcher matcher = pattern.matcher(textMessage);
                if(model.equals(REXMODEL.CHILD)) {
                    while (matcher.find()) {
                        for (int i = 1; i <= matcher.groupCount(); i++) {
                            groupEvent.getReceiveMessage().addRexStr(matcher.group(i));
                        }
                        isFilter = false;
                    }
                }else if (model.equals(REXMODEL.ALL)){
                    if(matcher.find() && matcher.start()==0 && matcher.end()==textMessage.length()){
                        for (int i = 1; i <= matcher.groupCount(); i++) {
                            groupEvent.getReceiveMessage().addRexStr(matcher.group(i));
                        }
                        isFilter = false;
                    }
                }
//                if (groupEvent.getReceiveMessage().getTextMessage().matches(rex)) {
//                    isFilter = false;
//                }
                if (isFilter)
                    return new int[]{0, 1};
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

                //控制事件相关---------------------------------------------------
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

                        try {
                            String jsonSubmit = objectMapper.writeValueAsString(submit);
                            application.getCqClient().getWebSocket().send(jsonSubmit);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    } else

                        //踢人---------------------
                        if (qqOperation.getAction().equals("set_group_kick")) {
                            GroupKickOperation groupSpecialTitleOperation = (GroupKickOperation) qqOperation;
                            ActionSubmit submit = new ActionSubmit.Builder()
                                    .setAciton("set_group_kick")
                                    .addParam("group_id", groupSpecialTitleOperation.getGroup_id())
                                    .addParam("user_id", groupSpecialTitleOperation.getUser_id())
                                    .addParam("reject_add_request", groupSpecialTitleOperation.getReject_add_request())
                                    .build();
                            try {
                                String jsonSubmit = objectMapper.writeValueAsString(submit);
                                application.getCqClient().getWebSocket().send(jsonSubmit);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        } else

                            //信息撤回----------------------
                            if (qqOperation.getAction().equals("delete_msg")) {
                                DeleteMsgOperation deleteMsgOperation = (DeleteMsgOperation) qqOperation;
                                ActionSubmit submit = new ActionSubmit.Builder()
                                        .setAciton("delete_msg")
                                        .addParam("message_id", deleteMsgOperation.getMessage_id())
                                        .build();
                                try {
                                    String jsonSubmit = objectMapper.writeValueAsString(submit);
                                    application.getCqClient().getWebSocket().send(jsonSubmit);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            } else
                                //群打卡
                                if (qqOperation.getAction().equals("send_group_sign")) {
                                    SendGroupSignOperation sendGroupSignOperation = (SendGroupSignOperation) qqOperation;
                                    ActionSubmit submit = new ActionSubmit.Builder()
                                            .setAciton(sendGroupSignOperation.getAction())
                                            .addParam("group_id", sendGroupSignOperation.getGroup_id())
                                            .build();
                                    try {
                                        String jsonSubmit = objectMapper.writeValueAsString(submit);
                                        application.getCqClient().getWebSocket().send(jsonSubmit);
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else
                                    //群单人禁言
                                    if (qqOperation.getAction().equals("set_group_ban")) {
                                        GroupBanOperation ban = (GroupBanOperation) qqOperation;
                                        ActionSubmit submit = new ActionSubmit.Builder()
                                                .setAciton(ban.getAction())
                                                .addParam("group_id", ban.getGroup_id())
                                                .addParam("user_id", ban.getUser_id())
                                                .addParam("duration", ban.getDuration())
                                                .build();
                                        try {
                                            String jsonSubmit = objectMapper.writeValueAsString(submit);
                                            application.getCqClient().getWebSocket().send(jsonSubmit);
                                        } catch (JsonProcessingException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else
                                        //设置管理员
                                        if (qqOperation.getAction().equals("set_group_admin")) {
                                            GroupAdminOperation admin = (GroupAdminOperation) qqOperation;
                                            ActionSubmit submit = new ActionSubmit.Builder()
                                                    .setAciton(admin.getAction())
                                                    .addParam("group_id", admin.getGroup_id())
                                                    .addParam("user_id", admin.getUser_id())
                                                    .addParam("enable", admin.getEnable())
                                                    .build();
                                            try {
                                                String jsonSubmit = objectMapper.writeValueAsString(submit);
                                                application.getCqClient().getWebSocket().send(jsonSubmit);
                                            } catch (JsonProcessingException e) {
                                                throw new RuntimeException(e);
                                            }
                                        } else
                                            //发送戳一戳
                                            if (qqOperation.getAction().equals("pokeOperation")) {
                                                PokeOperation operation = (PokeOperation) qqOperation;
                                                ArrayList<MetaMessage> metaMessages = new ArrayList<>();
                                                metaMessages.add(new MetaMessage.Builder().type("poke").addParam("qq", operation.getQq()).build());
                                                ActionSubmit submit = new ActionSubmit.Builder()
                                                        .setAciton("send_group_msg")
                                                        .addParam("group_id", groupEvent.getReceiveMessage().getSendGroupQQ())
                                                        .addParam("message", metaMessages)
                                                        .build();
                                                try {
                                                    String jsonSubmit = objectMapper.writeValueAsString(submit);
                                                    application.getCqClient().getWebSocket().send(jsonSubmit);
                                                } catch (JsonProcessingException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                }


                //发送信息----------------------------------------------------------------
                ActionSubmit actionSubmit = new ActionSubmit.Builder()
                        .setAciton("send_group_msg")
                        .addParam("group_id", groupEvent.getOriginalMessage().getGroup_id())
                        .addParam("message", message.getMessageBuffer())
                        .build();

                try {
                    String jsonSubmit = objectMapper.writeValueAsString(actionSubmit);
                    application.getCqClient().getWebSocket().send(jsonSubmit);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        //发送信息之后条件拦截--------------------------------------
        if (method.isAnnotationPresent(EventFilter.class)) {
            EventFilter eventFilter = method.getAnnotation(EventFilter.class);
            //判断是否事件截断
            if (eventFilter.isCut()) {
                return new int[]{1, 2};
            }
        }

        return new int[]{1, 0};
    }
}
