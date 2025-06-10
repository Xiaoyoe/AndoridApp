package com.example.ticketmall.entity;

import java.util.Date;

public class ChatMessage {
    public static final int SENDER_USER = 0;
    public static final int SENDER_AI = 1;

    private String message;
    private int sender;
    public int status; // 新增：消息状态，比如 0 表示发送中，1 表示发送成功，2 表示发送失败

    public ChatMessage(String message, int sender, int status) {
        this.message = message;
        this.sender = sender;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getSender() {
        return sender;
    }


    public int getStatus() {
        return status;
    }
}