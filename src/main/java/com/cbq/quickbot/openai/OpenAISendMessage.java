package com.cbq.quickbot.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAISendMessage {
    private String model="gpt-3.5-turbo";
    private Float temperature= 0.9F;
    private Float presence_penalty= 0F;
    private Float top_p=0.9F;
    private Float frequency_penalty=0F;
    private Boolean stream= true;

    private ArrayList<GPTAnswer> messages;
}
