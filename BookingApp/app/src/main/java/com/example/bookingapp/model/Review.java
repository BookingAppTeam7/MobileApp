package com.example.bookingapp.model;

import java.io.Serializable;

;
public class Review implements Serializable {
    private int image;
    private String username;
    private ReviewType reviewType;
    private String content;
    private int grade;
    //private DateTime dateTime;

    public Review(int image,String username, ReviewType reviewType, String content, int grade) {
        this.image=image;
        this.username = username;
        this.reviewType = reviewType;
        this.content = content;
        this.grade = grade;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
