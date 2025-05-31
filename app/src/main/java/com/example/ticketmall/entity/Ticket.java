package com.example.ticketmall.entity;

import java.io.Serializable;

public class Ticket implements Serializable {

    private Integer id;
    private String type;  // 修改为String类型
    private Integer userId;
    private String title;

    /**
     * 评分
     */
    private String score;

    private Integer imageResId;
    private String content1;
    private String content2;

    /**
     * 单价
     */
    private Double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // 修改getter和setter为String类型
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getImageResId() {
        return imageResId;
    }

    public void setImageResId(Integer imageResId) {
        this.imageResId = imageResId;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", score='" + score + '\'' +
                ", imageResId=" + imageResId +
                ", content1='" + content1 + '\'' +
                ", content2='" + content2 + '\'' +
                ", price=" + price +
                '}';
    }
}