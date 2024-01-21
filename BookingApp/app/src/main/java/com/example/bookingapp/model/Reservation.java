package com.example.bookingapp.model;

import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Reservation {
    public Long id;

    public Accommodation accommodation;

    public User user;

    public TimeSlot timeSlot;

    public ReservationStatusEnum status;

    public Long numberOfGuests;
    public double price;
    public PriceTypeEnum priceType;
    public Reservation(){}

    public Reservation(Long id, TimeSlot timeSlot,ReservationStatusEnum status, Accommodation accommodation, Long numberOfGuests, User user, double price, PriceTypeEnum priceType) {
        this.id = id;
        this.timeSlot=timeSlot;
        this.status=status;
        this.accommodation=accommodation;
        this.numberOfGuests=numberOfGuests;
        this.user=user;
        this.price=price;
        this.priceType=priceType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PriceTypeEnum getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceTypeEnum priceType) {
        this.priceType = priceType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
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

    public ReservationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = status;
    }
}
