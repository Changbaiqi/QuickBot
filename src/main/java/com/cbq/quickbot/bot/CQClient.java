package com.cbq.quickbot.bot;


import com.cbq.quickbot.handler.GroupEventHandler;
import com.cbq.quickbot.handler.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Slf4j
public class CQClient {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private WebSocket webSocket;

    private Boolean recFlag = true;

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
            initializeWebSocket();
        }

        private void reconnect() {
            // 断开后重新连接的逻辑
            // 可以等待一段时间后重新调用 initializeWebSocket()
            // 或者根据你的需求进行处理
            build();
        }

        //初始化ws连接
        private void initializeWebSocket() {
            client = new CQClient();
            builder = new Request.Builder();
            client.setOkHttpClient(new OkHttpClient.Builder()
                    .addInterceptor(new RetryInterceptor(20,2000)).build());
            client.recFlag = true;
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
            System.out.format("\33[31;1m连接CQ失败，正在尝试重连...\33[0m%n");
            if(client.recFlag)
                reconnect();
            client.recFlag= false;
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
                    if (jsonObject.get("post_type").equals("meta_event"))
                        return;
                    //打印信息
                    log.info(text);
                    //如果是请求类型
                    if(jsonObject.get("post_type").equals("request")){
                        new RequestHandler(text,application);
                    }
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
            System.out.format("\33[32;1m连接CQ成功...\33[0m%n");
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
