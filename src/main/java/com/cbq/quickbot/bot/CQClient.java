package com.cbq.quickbot.bot;


import com.alibaba.fastjson2.JSONObject;
import com.cbq.quickbot.handler.GroupEventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CQClient {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private WebSocket webSocket;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public static class Builder extends WebSocketListener {
        private CQClient client;
        private QuickBotApplication application;
        private Request.Builder builder;

        public Builder(){
            client = new CQClient();
            builder = new Request.Builder();
            client.setOkHttpClient(new OkHttpClient());
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            //super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            //super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            //super.onFailure(webSocket, t, response);
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                //JSONObject jsonObject = JSONObject.parseObject(text);
                Map<String,Object> jsonObject = objectMapper.readValue(text,Map.class);
                //过滤不符合规范的数据
                if(!jsonObject.containsKey("post_type"))
                    return;
                //过滤心跳包
                if (jsonObject.get("post_type").equals("meta_message"))
                    return;
                //如果是群消息
                if(jsonObject.get("post_type").equals("message") && jsonObject.get("message_type").equals("group")){
                    new GroupEventHandler(text,application);
                }
            }catch (Exception e){
                System.out.println(e);
            }
            //super.onMessage(webSocket, text);
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            //super.onMessage(webSocket, bytes);
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            //super.onOpen(webSocket, response);
        }

        public Builder setURL(String url){
            builder = builder.url(url);
            return this;
        }

        public Builder application(QuickBotApplication application){
            this.application = application;
            return this;
        }


        public CQClient build(){
            client.setWebSocket(client.getOkHttpClient().newWebSocket(builder.build(), this));
            return client;
        }



    }
}