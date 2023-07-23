package com.cbq.quickbot.service;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

public class QuickBotWebSocket {
    private OkHttpClient okHttpClient;
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
}
