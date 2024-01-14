package com.example.bookingapp.model.DTOs;

import java.time.LocalDateTime;

public class NotificationPostDTO {
    public String userId;
    public String type;
    public String content;

    public String time;

    public NotificationPostDTO(String userId, String type, String content, String time) {
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
