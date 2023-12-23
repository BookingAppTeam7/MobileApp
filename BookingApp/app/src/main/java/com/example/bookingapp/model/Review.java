package com.example.bookingapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

;
public class Review implements Serializable {
    private Long id;
    private String userId;
    private ReviewEnum type;
    private String comment;
    private int grade;
    private String dateTime;

    public Review(Long id, String username, ReviewEnum reviewEnum, String content, int grade, String dateTime) {
        this.id=id;
        this.userId = username;
        this.type = reviewEnum;
        this.comment = content;
        this.grade = grade;
        this.dateTime=dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        // Parse the dateTime string into LocalDateTime
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
    }

    public void setDateTime(LocalDateTime dateTime) {
        // Format LocalDateTime to String and set the dateTime field
        this.dateTime = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String username) {
        this.userId = username;
    }

    public ReviewEnum getType() {
        return type;
    }

    public void setType(ReviewEnum reviewEnum) {
        this.type = reviewEnum;
    }

    public String getContent() {
        return comment;
    }

    public void setContent(String content) {
        this.comment = content;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
