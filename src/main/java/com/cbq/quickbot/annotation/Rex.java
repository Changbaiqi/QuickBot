package com.cbq.quickbot.annotation;

import com.cbq.quickbot.entity.action.REXMODEL;

public @interface Rex {
    String rex() default "";
    REXMODEL model() default REXMODEL.ALL;
}
