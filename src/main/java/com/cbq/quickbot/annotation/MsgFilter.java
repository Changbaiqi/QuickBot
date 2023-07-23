package com.cbq.quickbot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 接受消息过滤
 * @author 长白崎
 * @date 2023/7/4 3:45
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgFilter {
    /**
     * 可以写正则表达式，用于消息过滤
     * @return
     */
    String rex();
}
