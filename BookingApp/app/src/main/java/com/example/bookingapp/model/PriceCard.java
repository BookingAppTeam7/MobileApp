package com.example.bookingapp.model;

import com.example.bookingapp.model.enums.PriceTypeEnum;

import java.io.Serializable;
import java.util.Date;

public class PriceCard implements Serializable {
    public Long id;

    public TimeSlot timeSlot;
    public double price;
    public PriceTypeEnum type;

    private Boolean deleted;

    public PriceCard(Long id, TimeSlot timeSlot, double price, PriceTypeEnum type, Boolean deleted) {
        this.id = id;
        this.timeSlot = timeSlot;
        this.price = price;
        this.type = type;
        this.deleted = deleted;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
