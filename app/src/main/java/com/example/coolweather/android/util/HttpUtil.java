package com.example.coolweather.android.util;

import android.text.TextUtils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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

    public static void sendOkHttpRequest(String url, RequestBody body, String method, Callback callback) {
        client = newInstance();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (null != body) {
            builder.method((TextUtils.isEmpty(method) ? "GET" : "POST"), body);
        }
        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }
}
