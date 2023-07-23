package com.cbq.quickbot.config;

import com.cbq.quickbot.annotation.BotListen;
import com.cbq.quickbot.service.QuickBotService;
import com.cbq.quickbot.service.QuickBotWebSocket;
import jakarta.annotation.Resource;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.reflect.Method;
import java.util.Map;

@Configuration
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Resource
    QuickbotProperties quickbotProperties;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("测试是否正常启动");
//        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(BotListen.class);
//        for(Object o : beansWithAnnotation.values()){
//            System.out.println(o.toString());
//        }

    }



    public void loadHttp(){

        QuickBotWebSocket webSocket = new QuickBotWebSocket();
        webSocket.setOkHttpClient(new OkHttpClient.Builder().build());
        Request request = new Request.Builder()
                .url("ws://localhost:"+quickbotProperties.getPort())
                .build();
        webSocket.setWebSocket(webSocket.getOkHttpClient().newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("关闭");
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                super.onMessage(webSocket, text);

                System.out.println(text);
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                super.onOpen(webSocket, response);
                System.out.println("连接成功");
            }
        }));
    }



}
