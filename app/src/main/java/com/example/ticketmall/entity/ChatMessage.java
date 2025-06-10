package com.example.ticketmall.entity;


import java.util.Date;

public class ChatMessage {
    public static final int SENDER_USER = 0;
    public static final int SENDER_AI = 1;
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_COMPLETED = 1;

    private String message;
    private int sender;
    private int status;
    private Date timestamp;

    public ChatMessage(String message, int sender) {
        this.message = message;
        this.timestamp = new Date();
        this.sender = sender;
        this.status = STATUS_COMPLETED;
        if (sender == SENDER_USER) {
            // 用户发送消息后，AI 消息处于等待状态
            this.status = STATUS_PENDING;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getSender() {
        return sender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}