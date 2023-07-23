package com.cbq.quickbot.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Map;

@Data
public class ReceiveMessage {
    private String post_type;
    private String meta_event_type;
    private Long time;
    private Long self_id;
    private  String sub_type;
    private String message;

    private Long message_seq;
    private Sender sender;
    private Long message_id;

    private Long group_id;
    private Integer font;
    private String raw_message;
    private Long user_id;
    private String anonymous;

    private String message_type;
}
