package com.cbq.quickbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
//@JsonDeserialize(using =  SenderDeserialize.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sender {
    private Integer age;
    private String area;
    private String card;
    private String level;
    private String nickname;
    private String role;
    private String sex;
    private String title;
    private Long user_id;
}
