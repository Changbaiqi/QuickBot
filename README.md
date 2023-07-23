# QuickBot

[![img](https://camo.githubusercontent.com/76fdee18b974fc86f26e83b003f840012166ecf8bf835b0ca4211abaf6979340/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4a61766131372d70617373696e672d722e737667)](https://camo.githubusercontent.com/76fdee18b974fc86f26e83b003f840012166ecf8bf835b0ca4211abaf6979340/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4a61766131372d70617373696e672d722e737667) [![img](https://camo.githubusercontent.com/f81eec727df88e89897980ae2f9640bbb59b9d4d035cf740db885f782c7e8083/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c6963656e73652d417061636865322e302d626c75652e737667)](https://camo.githubusercontent.com/f81eec727df88e89897980ae2f9640bbb59b9d4d035cf740db885f782c7e8083/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c6963656e73652d417061636865322e302d626c75652e737667) [![img](https://camo.githubusercontent.com/2d19c3e0ed08f8d58d8c641ad04ddfeeee7c3078a046378ab2aea7aae65a6fe2/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4d6176656e332e382e312d6275696c64696e672d722e737667)](https://camo.githubusercontent.com/2d19c3e0ed08f8d58d8c641ad04ddfeeee7c3078a046378ab2aea7aae65a6fe2/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4d6176656e332e382e312d6275696c64696e672d722e737667)

---

Hello，这里是基于CQHttp的正向WebSocket写的Java使用框架。

>  目前处于开发阶段，功能还未完善~

## 使用方式：

1、其实和SpringBoot的使用方式差不多一样,首先在Main函数里面加上@EnableQuickBot注解，然后在main函数里面写上new QuickBotApplication(当前类的class);

```java
@EnableQuickBot
public class Main {
    public static void main(String[] args) {
        new QuickBotApplication(Main.class);
        System.out.println("Hello world!");
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
import com.cbq.quickbot.entity.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@BotListen
public class QQBotListen {

    @GroupListen(qqList = {784469488,439329252,782620752}/*触发回复的群列表*/)
    @EventFilter(atBot = true/*是否为at机器人才能触发*/,rex = "Hello"/*是否正则匹配，默认不写就是不触发正则匹配*/)
    public Message botListen(GroupEvent groupEvent){
        log.info(groupEvent.getJsonText());
        String message1 = groupEvent.getReceiveMessage().getTextMessage();
        Message message = new Message.Builder()
                .setMessage("你发送的信息为："+message1)
                .build();
        return message;
    }
}

```

