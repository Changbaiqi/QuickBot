package com.cbq.quickbot.openai;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GPTAnswer {
    //可以是user，可以是assistant
    private String role;
    private String content;

    public static void setATRI(){

    }

}
