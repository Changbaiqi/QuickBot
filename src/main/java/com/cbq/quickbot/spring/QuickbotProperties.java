package com.cbq.quickbot.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "quick.bot")
public class QuickbotProperties {
    private Integer port;

}
