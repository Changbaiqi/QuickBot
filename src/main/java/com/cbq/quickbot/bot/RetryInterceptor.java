package com.cbq.quickbot.bot;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class RetryInterceptor implements Interceptor {
    private int maxAttempts;  // 最大重试次数
    private int retryDelayMillis;  // 重试间隔时间

    public RetryInterceptor(int maxAttempts, int retryDelayMillis) {
        this.maxAttempts = maxAttempts;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        int tryCount = 0;
        Response response = null;
        IOException exception = null;

        while (tryCount < maxAttempts) {
            try {
                response = chain.proceed(chain.request());
                return response;
            } catch (IOException e) {
                exception = e;
                tryCount++;
                log.error("第{}次连接失败",tryCount);
                try {
                    Thread.sleep(retryDelayMillis);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (response == null && exception != null) {
            throw exception;
        }

        return response;
    }
}