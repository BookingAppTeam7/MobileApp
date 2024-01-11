package com.example.bookingapp.model.DTOs;

import com.example.bookingapp.model.ReviewEnum;
import com.example.bookingapp.model.enums.ReviewStatusEnum;

import java.time.LocalDateTime;

public class ReviewPostAccommodationDTO {
    public String userId;
    public ReviewEnum type;
    public String comment;
    public int grade;


    public Boolean deleted;
    public Boolean reported;

    public Long accommodationId;
    public String ownerId;
    public ReviewStatusEnum status;
    public Long reservationId;

    public ReviewPostAccommodationDTO(String userId, ReviewEnum type, String comment, int grade, Boolean deleted, Boolean reported, Long accommodationId, String ownerId,
                                            ReviewStatusEnum status, Long reservationId) {
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.grade = grade;
        this.deleted = deleted;
        this.reported = reported;
        this.accommodationId = accommodationId;
        this.ownerId = ownerId;
        this.status=status;
        this.reservationId=reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public ReviewStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReviewStatusEnum status) {
        this.status = status;
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



    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getReported() {
        return reported;
    }

    public void setReported(Boolean reported) {
        this.reported = reported;
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
}
