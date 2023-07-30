package com.cbq.quickbot.utils;

import com.alibaba.fastjson2.JSONObject;
import com.cbq.quickbot.bot.QuickBotApplication;
import com.cbq.quickbot.entity.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.Map;

public class ConfigUtil {
    public static Config getConfig(Class applicationClazz,String configFilePath){
        ObjectMapper objectMapper = new ObjectMapper();

        String configText = IOUtil.getResourceText(applicationClazz,configFilePath);
        try {
            Config config = objectMapper.readValue(configText, Config.class);

            return config;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
