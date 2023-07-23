package com.cbq.quickbot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventFilter {
    /**
     * 是否atQQ机器人才触发词此事件
     * @return
     */
    boolean atBot() default false;

    /**
     * 正则匹配触发
     * @return
     */
    String rex() default "";

    /**
     * 是否截断事件触发，比如有与此事件拥有相同触发条件的事件，如果这个事件触发了那么久不会触发其他任何事件
     * @return
     */
    boolean isCut() default false;

    /**
     * 触发等级，如果有与之相同触发条件的事件，将会通过等级顺序先后触发
     * @return
     */
    int level() default 0;
}
