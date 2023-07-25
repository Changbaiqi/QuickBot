package com.cbq.quickbot.annotation;

import java.lang.annotation.*;

/**
 * @description: 用于群请求触发事件
 * @author 长白崎
 * @date 2023/7/25 14:15
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RequestGroupListen {

}
