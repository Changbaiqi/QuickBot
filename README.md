# **QuickBot**

[![img](https://camo.githubusercontent.com/76fdee18b974fc86f26e83b003f840012166ecf8bf835b0ca4211abaf6979340/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4a61766131372d70617373696e672d722e737667)](https://camo.githubusercontent.com/76fdee18b974fc86f26e83b003f840012166ecf8bf835b0ca4211abaf6979340/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4a61766131372d70617373696e672d722e737667) [![img](https://img.shields.io/badge/license-GPL3.0-blue.svg)](https://camo.githubusercontent.com/f81eec727df88e89897980ae2f9640bbb59b9d4d035cf740db885f782c7e8083/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c6963656e73652d417061636865322e302d626c75652e737667) [![img](https://camo.githubusercontent.com/2d19c3e0ed08f8d58d8c641ad04ddfeeee7c3078a046378ab2aea7aae65a6fe2/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4d6176656e332e382e312d6275696c64696e672d722e737667)](https://camo.githubusercontent.com/2d19c3e0ed08f8d58d8c641ad04ddfeeee7c3078a046378ab2aea7aae65a6fe2/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4d6176656e332e382e312d6275696c64696e672d722e737667)

---

**Hello，这里是基于CQHttp的正向WebSocket写的Java使用框架。**

>  目前处于开发阶段，功能还未完善~



## 目前功能：

> - [x] 注解编程
> - [x] 自定义配置
> - [x] 发送信息
> - [x] atQQ机器人触发
> - [x] 正则匹配触发事件
> - [x] 事件截断
> - [x] 触发事件等级自定义

## 群相关功能支持：

> - [x] 群专属头衔设置
> - [x] 群踢人

## 使用方式：

1、其实和SpringBoot的使用方式差不多一样,首先在Main函数里面加上@EnableQuickBot注解，然后在main函数里面写上QuickBotApplication.run(Main.class, args);(当前类的class);

```java
package org.exm;

import com.cbq.quickbot.annotation.EnableQuickBot;
import com.cbq.quickbot.bot.QuickBotApplication;

@EnableQuickBot
public class Main {
    public static void main(String[] args) {
        QuickBotApplication run = QuickBotApplication.run(Main.class, args);
        //System.out.println("不堵塞这里");
    }
}
```

2、之后就是基于当前类下使用注解，简单接收信息的使用方式如下：

```java
package org.exm.controller;

import com.cbq.quickbot.annotation.BotListen;
import com.cbq.quickbot.annotation.EventFilter;
import com.cbq.quickbot.annotation.GroupListen;
import com.cbq.quickbot.bot.GroupEvent;
import com.cbq.quickbot.entity.AT;
import com.cbq.quickbot.entity.Message;
import com.cbq.quickbot.entity.ReceiveMessage;
import lombok.extern.slf4j.Slf4j;
import org.exm.openai.GPTAnswer;
import org.exm.openai.OpenAI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@BotListen
public class QQBotListen {
    public static Map<Long, ArrayList<GPTAnswer>> gptAnswerMap = new HashMap<>();

    @GroupListen
    @EventFilter(atBot = true/*是否为at机器人才能触发*/,
            isCut = true, /*触发值事件之后，后续此类中其他可触发事件全部失效*/
            level = 0/*触发等级，等级越高谁就先触发，但是不保证发送先后顺序*/)
    public Message botListen(GroupEvent groupEvent) {
        log.info(groupEvent.getJsonText());
        ReceiveMessage receiveMessage = groupEvent.getReceiveMessage();
        String message1 = receiveMessage.getTextMessage();

        if (!gptAnswerMap.containsKey(receiveMessage.getSendQQ())) {
            gptAnswerMap.put(receiveMessage.getSendQQ(), new ArrayList<GPTAnswer>());
        }

        //将信息加入
        gptAnswerMap.get(receiveMessage.getSendQQ()).add(GPTAnswer.builder()
                .role("user")
                .content(message1)
                .build());
        OpenAI openAI = new OpenAI.Builder()
                .messages(gptAnswerMap.get(receiveMessage.getSendQQ()))
                .toReply();
        //将回复信息加入
        gptAnswerMap.get(receiveMessage.getSendQQ()).add(GPTAnswer.builder()
                .role("assistant")
                .content(openAI.getReplyMessage()).build());
        if(gptAnswerMap.get(receiveMessage.getSendQQ()).size()>40){
            gptAnswerMap.get(receiveMessage).remove(0);
        }

        Message message = new Message.Builder()
                .at(new AT(receiveMessage.getSendQQ()))
                .text(openAI.getReplyMessage())
                .build();

        return message;
    }


    @GroupListen
    @EventFilter(
            atBot = true/*是否为at机器人才能触发*/,
            rex = "测试",/*正则匹配如果通过那么久触发此条件*/
        	isCut = true,
            level = 1)
    public Message testListen(GroupEvent groupEvent) {
        log.info(groupEvent.getJsonText());
        String message1 = groupEvent.getReceiveMessage().getTextMessage();

        Message message = new Message.Builder()
                .at(new AT(2084069833l))
                .text("测试成功")
                .build();

        return message;
    }


}

```

