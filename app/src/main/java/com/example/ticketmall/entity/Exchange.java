package com.example.ticketmall.entity;

public class Exchange {
    private int id;
    private int userId;
    private int points;
    private String stuffName;
    private String createTime;
    private String imageUrl;

    // 默认构造方法
    public Exchange() {
    }

    // 带参构造方法
    public Exchange(int id, int userId, int points, String stuffName, String createTime, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.points = points;
        this.stuffName = stuffName;
        this.createTime = createTime;
        this.imageUrl = imageUrl;
    }

    // Getter and Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getStuffName() {
        return stuffName;
    }

    public void setStuffName(String stuffName) {
        this.stuffName = stuffName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 重写toString方法，便于打印对象
    @Override
    public String toString() {
        return "Exchange{" +
                "id=" + id +
                ", userId=" + userId +
                ", points=" + points +
                ", stuffName='" + stuffName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
