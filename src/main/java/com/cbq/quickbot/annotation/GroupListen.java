package com.cbq.quickbot.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface GroupListen {
    long[] qqList() default {};
}
