package com.example.coolweather.android.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * author:Mark
 * date:2017/4/2
 * time:10:25
 */

public class HttpUtil {

    private static OkHttpClient client;

    private static OkHttpClient newInstance() {
        if (null == client) {
            synchronized (OkHttpClient.class) {
                if (null == client) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }

    public static void sendOkHttpRequest(String url, Callback callback) {
        client = newInstance();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
