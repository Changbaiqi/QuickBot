package com.cbq.quickbot.utils;

import com.cbq.quickbot.bot.QuickBotApplication;

import java.io.*;

public class IOUtil {

    public static BufferedInputStream getResourceBuffInput(String file) {
        InputStream resourceAsStream = IOUtil.class.getClass().getResourceAsStream(file);
        BufferedInputStream myStream = new BufferedInputStream(resourceAsStream);
        return myStream;
    }

    public static String getResourceText(Class quickBotApplicationClazz,String textFilePath) {
        InputStream resourceAsStream = quickBotApplicationClazz.getResourceAsStream("/"+textFilePath);
        BufferedInputStream bis = new BufferedInputStream(resourceAsStream);
        InputStreamReader isr = new InputStreamReader(bis);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer stringBuffer = new StringBuffer();

        String res = null;
        try {
            while (((res = br.readLine()) != null)) {
                stringBuffer.append(res);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuffer.toString();
    }
}
