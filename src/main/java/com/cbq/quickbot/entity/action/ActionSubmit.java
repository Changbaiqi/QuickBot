package com.cbq.quickbot.entity.action;

import com.cbq.quickbot.bot.CQClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 用于信息提交json格式规范化
 * @author 长白崎
 * @date 2023/7/25 0:47
 * @version 1.0
 */
public class ActionSubmit {
    private String action;
    private Map<String,Object> params;

    public String getJson(){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String,Object> result = new HashMap<>();
            result.put("action",action);
            result.put("params",params);
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static class Builder{
        private ActionSubmit actionSubmit;
        public Builder(){
            actionSubmit = new ActionSubmit();
            actionSubmit.params = new HashMap<>();
        }
        public Builder setAciton(String action){
            actionSubmit.action = action;
            return this;
        }
        public Builder addParam(String key,Object value){
            actionSubmit.params.put(key,value);
            return this;
        }

        public ActionSubmit build(){
            return actionSubmit;
        }
    }
}
