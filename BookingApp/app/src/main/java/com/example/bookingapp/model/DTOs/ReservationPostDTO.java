package com.example.bookingapp.model.DTOs;

import com.example.bookingapp.model.TimeSlot;

public class ReservationPostDTO {
    public  Long accommodationId;
    public String userId;
    public TimeSlot timeSlot;
    public Long numberOfGuests;

    public ReservationPostDTO( Long accommodationId, String userId, TimeSlot timeSlot, Long numberOfGuests) {

        this.accommodationId = accommodationId;
        this.userId=userId;
        this.timeSlot=timeSlot;
        this.numberOfGuests=numberOfGuests;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getAccommodationId() {
        return accommodationId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }
}
