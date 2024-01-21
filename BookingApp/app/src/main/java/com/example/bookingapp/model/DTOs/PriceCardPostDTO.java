package com.example.bookingapp.model.DTOs;

import com.example.bookingapp.model.enums.PriceTypeEnum;

import java.io.Serializable;

public class PriceCardPostDTO implements Serializable {
    public TimeSlotPostDTO timeSlot;
    public double price;
    public PriceTypeEnum type;

    public Long accommodationId;


    public PriceCardPostDTO(TimeSlotPostDTO timeSlot, double price, PriceTypeEnum type) {
        this.timeSlot = timeSlot;
        this.price = price;
        this.type = type;
    }

    public TimeSlotPostDTO getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlotPostDTO timeSlot) {
        this.timeSlot = timeSlot;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PriceTypeEnum getType() {
        return type;
    }

    public void setType(PriceTypeEnum type) {
        this.type = type;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }
}
