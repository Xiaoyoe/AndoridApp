package com.example.ticketmall.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * HTTP 网络请求工具类（单例模式）
 * 封装 OkHttp 的常用请求方法（GET/POST），支持 JSON 和表单请求
 */
public class HttpUtils {
    private static final String TAG = "HttpUtils";
    private static final long CONNECT_TIMEOUT = 30; // 连接超时时间（秒）
    private static final long READ_TIMEOUT = 60;    // 读取超时时间（秒）
    private static final long WRITE_TIMEOUT = 30;   // 写入超时时间（秒）

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static HttpUtils instance;
    private final OkHttpClient client;
    private final Gson gson;
    private final Handler mainHandler; // 用于在主线程回调

    private HttpUtils() {
        // 配置 OkHttpClient
        client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        gson = new Gson();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    // 获取单例实例
    public static synchronized HttpUtils getInstance() {
        if (instance == null) {
            instance = new HttpUtils();
        }
        return instance;
    }

    /**
     * 发送 POST 请求（JSON 格式）
     * @param url 请求 URL
     * @param json 请求 JSON 字符串
     * @param callback 请求回调
     */
    public void postJson(String url, String json, final HttpCallback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        sendRequest(request, callback);
    }

    /**
     * 发送 POST 请求（表单格式）
     * @param url 请求 URL
     * @param params 请求参数（键值对）
     * @param callback 请求回调
     */
    public void postForm(String url, Map<String, String> params, final HttpCallback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody body = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        sendRequest(request, callback);
    }

    /**
     * 发送 GET 请求
     * @param url 请求 URL
     * @param params 请求参数（键值对，会拼接到 URL 后面）
     * @param callback 请求回调
     */
    public void get(String url, Map<String, String> params, final HttpCallback callback) {
        // 拼接参数到 URL
        StringBuilder urlBuilder = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            // 移除最后一个 &
            if (urlBuilder.length() > 0 && urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);
            }
        }

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .get()
                .build();

        sendRequest(request, callback);
    }

    /**
     * 发送通用请求
     */
    private void sendRequest(Request request, final HttpCallback callback) {
        Log.d(TAG, "Request URL: " + request.url());
        Log.d(TAG, "Request Headers: " + request.headers());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                // 切换到主线程回调
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onFailure("网络请求失败：" + e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final int code = response.code();
                    final String responseData = response.body().string();
                    Log.d(TAG, "Response Code: " + code);
                    Log.d(TAG, "Response Data: " + responseData);

                    // 切换到主线程回调
                    mainHandler.post(() -> {
                        if (callback != null) {
                            if (response.isSuccessful()) {
                                callback.onSuccess(responseData);
                            } else {
                                callback.onFailure("请求失败，状态码：" + code + "，信息：" + response.message());
                            }
                        }
                    });
                } catch (final Exception e) {
                    Log.e(TAG, "Parse response error: " + e.getMessage());
                    // 切换到主线程回调
                    mainHandler.post(() -> {
                        if (callback != null) {
                            callback.onFailure("解析响应失败：" + e.getMessage());
                        }
                    });
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    /**
     * HTTP 请求回调接口
     */
    public interface HttpCallback {
        void onSuccess(String response);
        void onFailure(String errorMsg);
    }

    /**
     * 将对象转换为 JSON 字符串
     */
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 将 JSON 字符串转换为对象
     */
    public <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "JSON parsing error: " + e.getMessage());
            return null;
        }
    }
}