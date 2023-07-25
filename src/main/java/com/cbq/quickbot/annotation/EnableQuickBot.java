package com.cbq.quickbot.annotation;

import com.cbq.quickbot.spring.QuickBotAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({QuickBotAutoConfiguration.class})
public @interface EnableQuickBot {
}
