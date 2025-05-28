package com.example.ticketmall.api;

import com.example.ticketmall.entity.User;
import com.example.ticketmall.utils.HttpUtils;

/**
 * API 接口管理类
 * 封装所有业务接口请求
 */
public class ApiManager {
    private static final String BASE_URL = "http://10.0.2.2:5000"; // 服务端基础 URL

    // 登录接口
    public static void login(String username, String password, HttpUtils.HttpCallback callback) {
        String url = BASE_URL + "/auth/login";

        // 构建请求参数对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // 转换为 JSON 并发送请求
        String json = HttpUtils.getInstance().toJson(user);
        HttpUtils.getInstance().postJson(url, json, callback);
    }

    // 注册接口
// 注册接口
    public static void register(String username, String password, String email, HttpUtils.HttpCallback callback) {
        String url = BASE_URL + "/auth/register";

        // 构建请求参数对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // 转换为 JSON 并发送请求
        String json = HttpUtils.getInstance().toJson(user);
        HttpUtils.getInstance().postJson(url, json, callback);
    }

    // 其他接口...
}