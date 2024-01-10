package com.example.bookingapp.model.DTOs;

import com.example.bookingapp.model.ReviewEnum;
import com.example.bookingapp.model.enums.ReviewStatusEnum;

public class ReviewGetDTO {
    public Long id;

    public String userId;


    public ReviewEnum type;
    public String comment;
    public int grade;
    public String dateTime;

    public Long accommodationId;
    public String ownerId;

    private Boolean reported;

    public ReviewStatusEnum status;

    public ReviewGetDTO() {
    }

    public ReviewGetDTO(Long id, String userId, ReviewEnum type, String comment, int grade, String dateTime, Long accommodationId, String ownerId, Boolean reported, ReviewStatusEnum status) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.grade = grade;
        this.dateTime = dateTime;
        this.accommodationId = accommodationId;
        this.ownerId = ownerId;
        this.reported = reported;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ReviewEnum getType() {
        return type;
    }

    public void setType(ReviewEnum type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getReported() {
        return reported;
    }

    public void setReported(Boolean reported) {
        this.reported = reported;
    }

    public ReviewStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReviewStatusEnum status) {
        this.status = status;
    }
}
