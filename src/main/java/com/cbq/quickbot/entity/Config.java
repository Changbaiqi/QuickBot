package com.cbq.quickbot.entity;

import com.cbq.quickbot.openai.OpenAI;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    //端口号
    @JsonProperty("port")
    private Integer port;
    //ip地址
    @JsonProperty("ip")
    private String ip;
    //断线重连次数
    @JsonProperty("reconnectionNumber")
    private Integer reconnectionNumber;
    @JsonProperty("openAI")
    private OpenAIConfig openAI;

}
