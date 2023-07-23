package com.cbq.quickbot.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Config {
    //端口号
    private Integer port;
    //ip地址
    private String ip;

}
