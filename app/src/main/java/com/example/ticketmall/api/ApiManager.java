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

    // 获取票务总列表
    public static void getTicketList(HttpUtils.HttpCallback callback) {
        String url = BASE_URL + "/tickets";
        HttpUtils.getInstance().get(url, null, callback);
    }

    // 获取电影票务列表
    public static void getMovieList(HttpUtils.HttpCallback callback) {
        String url = BASE_URL + "/tickets/movie";
        HttpUtils.getInstance().get(url, null, callback);
    }

    // 获取演唱会票务列表
    public static void getConcertList(HttpUtils.HttpCallback callback) {
        String url = BASE_URL + "/tickets/concert";
        HttpUtils.getInstance().get(url, null, callback);
    }

    // 获取音乐节票务列表
    public static void getMusicFestivalList(HttpUtils.HttpCallback callback) {
        String url = BASE_URL + "/tickets/music_festival";
        HttpUtils.getInstance().get(url, null, callback);
    }

    // 获取脱口秀票务列表
    public static void getComedyShowList(HttpUtils.HttpCallback callback) {
        String url = BASE_URL + "/tickets/comedy_show";
        HttpUtils.getInstance().get(url, null, callback);
    }

    // 获取商品列表
    public static void getStuffList(HttpUtils.HttpCallback callback) {
        String url = BASE_URL + "/stuffs";
        HttpUtils.getInstance().get(url, null, callback);
    }

    // 其他接口...
}