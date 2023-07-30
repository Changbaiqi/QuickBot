package com.cbq.quickbot.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface PrivateListen {
    long[] qqList() default {};
}
