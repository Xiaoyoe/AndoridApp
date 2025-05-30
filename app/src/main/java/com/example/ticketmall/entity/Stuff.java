package com.example.ticketmall.entity;

import com.google.gson.annotations.SerializedName;

public class Stuff {
    private String name;

    @SerializedName("image_res_id") // 确保与JSON字段名匹配
    private String imageResId;

    @SerializedName("image_url")    // 确保与JSON字段名匹配
    private String imageUrl;

    private Integer points;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageResId() { return imageResId; }
    public void setImageResId(String imageResId) { this.imageResId = imageResId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}