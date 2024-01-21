package com.example.bookingapp.model;

import com.example.bookingapp.model.enums.ReviewStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

;
public class Review implements  Serializable {

    public Long id;

    public String userId;

    public ReviewEnum type;
    public String comment;
    public int grade;
    public LocalDateTime dateTime;


    private Boolean deleted;

    public Long accommodationId;
    public String ownerId;

    private Boolean reported;

    public ReviewStatusEnum status;


    public Review( String userId, ReviewEnum type, String comment, int grade, LocalDateTime dateTime, Boolean deleted, Long accommodationId, String ownerId, Boolean reported,
                   ReviewStatusEnum status) {

        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.grade = grade;
        this.dateTime = dateTime;
        this.deleted = deleted;
        this.accommodationId = accommodationId;
        this.ownerId = ownerId;
        this.reported = reported;
        this.status=status;
    }
    public Review( Long id,String userId, ReviewEnum type, String comment, int grade, LocalDateTime dateTime, Boolean deleted, Long accommodationId, String ownerId, Boolean reported,
                   ReviewStatusEnum status) {
        this.id=id;
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.grade = grade;
        this.dateTime = dateTime;
        this.deleted = deleted;
        this.accommodationId = accommodationId;
        this.ownerId = ownerId;
        this.reported = reported;
        this.status=status;
    }

    public Review() {

    }



//    public Review(Long id, String userId, ReviewEnum type, String comment, int grade, LocalDateTime dateTime, Boolean deleted, Long accommodationId, String ownerId) {
//        this.id = id;
//        this.userId = userId;
//        this.type = type;
//        this.comment = comment;
//        this.grade = grade;
//        this.dateTime = dateTime;
//        this.deleted = deleted;
//        this.accommodationId = accommodationId;
//        this.ownerId = ownerId;
//    }


    public ReviewStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReviewStatusEnum status) {
        this.status = status;
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



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ReviewEnum getType() {
        return type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setType(ReviewEnum type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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

    public String getUserId() {
        return userId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
