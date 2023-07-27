package com.cbq.quickbot.openai;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenAI {
    private OpenAISendMessage openAISendMessage;

    private String replyMessage;

    private  ArrayList<String> authorizationList;
    private Integer authIndex=0;

    public OpenAI(){
        this.openAISendMessage = new OpenAISendMessage();
    }

    private void toReply(){

        String run = run(authIndex);
        while (run.equals("")){
            ++authIndex;
            if(authIndex<authorizationList.size())
                run = run(authIndex);
            else {
                this.replyMessage = "所有token失效";
                break;
            }
        }

        this.replyMessage = run;
    }

    private String run(int authIndex){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,JSONObject.toJSONString(openAISendMessage));
        Request request = new Request.Builder()
                .url("https://ai.fakeopen.com/v1/chat/completions")
                .method("POST", body)
                .addHeader("Authorization", "Bearer "+authorizationList.get(authIndex))
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "ai.fakeopen.com")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            Pattern pattern = Pattern.compile("data: ([\\s\\S]*?)\n");
            Matcher matcher = pattern.matcher(response.body().string());
            StringBuffer stringBuffer = new StringBuffer();
            while(matcher.find()){
                String group = matcher.group(1);
                try {
                    JSONObject jsonObject = JSONObject.parseObject(group);
                    JSONArray choices = jsonObject.getJSONArray("choices");
                    for (int i = 0; i < choices.size(); ++i) {
                        JSONObject words = choices.getJSONObject(i);
                        JSONObject delta = words.getJSONObject("delta");
                        String content = delta.getString("content");
                        if(content!=null)
                            stringBuffer.append(content);
                    }
                }catch (Exception e){
                }
            }

            //System.out.println(stringBuffer.toString());
            return stringBuffer.toString();
            //System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getReplyMessage() {
        return replyMessage;
    }





    public static class Builder{
        private OpenAI openAI;
        public Builder(){
            openAI = new OpenAI();
        }

       public Builder model(String model){
            openAI.openAISendMessage.setModel(model);
            return this;
        }

        public Builder temperature(Float temperature){
            openAI.openAISendMessage.setTemperature(temperature);
            return this;
        }

        public Builder presence_penalty(Float presence_penalty){
            openAI.openAISendMessage.setPresence_penalty( presence_penalty);
            return this;
        }

        public Builder top_p(Float top_p){
            openAI.openAISendMessage.setTop_p(top_p);
            return this;
        }
        public Builder frequency_penalty(Float frequency_penalty){
            openAI.openAISendMessage.setFrequency_penalty(frequency_penalty);
            return this;
        }

        public Builder stream(Boolean stream){
            openAI.openAISendMessage.setStream(stream);
            return this;
        }

        public Builder authorizationList(ArrayList<String> authorizationList){
            openAI.authorizationList = authorizationList;
            return this;
        }

        public Builder messages(ArrayList<GPTAnswer> messages){
            openAI.openAISendMessage.setMessages(messages);
            return this;
        }


        public OpenAI toReply(){
            openAI.toReply();
            return openAI;
        }
    }
}
