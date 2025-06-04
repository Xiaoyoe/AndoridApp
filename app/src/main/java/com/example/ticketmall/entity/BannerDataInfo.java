package com.example.ticketmall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BannerDataInfo implements Serializable {
    private int id;             // 轮播图ID
    private String title;       // 标题
    @SerializedName("image_url")
    private String imageUrl;    // 图片URL
    @SerializedName("image_res_id")
    private String imageResId;  // 图片资源ID

    public BannerDataInfo() {
        // 无参构造函数，用于JSON反序列化
    }

    public BannerDataInfo(int id, String title, String imageUrl, String imageResId) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.imageResId = imageResId;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        // 如果有资源ID，则构建完整URL
        if (imageResId != null && !imageResId.isEmpty()) {
            return "http://10.0.2.2:5000/carousels/images/" + imageResId;
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    @Override
    public String toString() {
        return "BannerDataInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + getImageUrl() + '\'' +
                ", imageResId='" + imageResId + '\'' +
                '}';
    }
}