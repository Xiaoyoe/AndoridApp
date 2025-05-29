package com.example.ticketmall.data;

import com.example.ticketmall.api.ApiManager;
import com.example.ticketmall.entity.Stuff;
import com.example.ticketmall.entity.Ticket;
import com.example.ticketmall.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppData {

    // 获取电影票务列表
    public static void getMovieList(final DataCallback<List<Ticket>> callback) {
        ApiManager.getMovieList(new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Ticket>>() {}.getType();
                List<Ticket> movieList = gson.fromJson(response, type);
                callback.onSuccess(movieList);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onFailure(errorMsg);
            }
        });
    }

    // 获取演唱会票务列表
    public static void getConcertList(final DataCallback<List<Ticket>> callback) {
        ApiManager.getConcertList(new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Ticket>>() {}.getType();
                List<Ticket> concertList = gson.fromJson(response, type);
                callback.onSuccess(concertList);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onFailure(errorMsg);
            }
        });
    }

    // 获取音乐节票务列表
    public static void getMusicFestivalList(final DataCallback<List<Ticket>> callback) {
        ApiManager.getMusicFestivalList(new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Ticket>>() {}.getType();
                List<Ticket> musicFestivalList = gson.fromJson(response, type);
                callback.onSuccess(musicFestivalList);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onFailure(errorMsg);
            }
        });
    }

    // 获取脱口秀票务列表
    public static void getComedyShowList(final DataCallback<List<Ticket>> callback) {
        ApiManager.getComedyShowList(new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Ticket>>() {}.getType();
                List<Ticket> comedyShowList = gson.fromJson(response, type);
                callback.onSuccess(comedyShowList);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onFailure(errorMsg);
            }
        });
    }

    // 获取商品列表
    public static void getStuffList(final DataCallback<List<Stuff>> callback) {
        ApiManager.getStuffList(new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Stuff>>() {}.getType();
                List<Stuff> stuffList = gson.fromJson(response, type);
                callback.onSuccess(stuffList);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onFailure(errorMsg);
            }
        });
    }

    // 数据回调接口
    public interface DataCallback<T> {
        void onSuccess(T data);
        void onFailure(String errorMsg);
    }
}