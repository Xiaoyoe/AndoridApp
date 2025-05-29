package com.example.ticketmall.entity;

public class Stuff {
    private String name;
    private Integer imageResId; // 本地资源 ID
    private String imageUrl;    // 新增：网络图片 URL
    private Integer points;

    // 原有方法保持不变
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getImageResId() { return imageResId; }
    public void setImageResId(Integer imageResId) { this.imageResId = imageResId; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    // 新增：imageUrl 的 getter/setter
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}