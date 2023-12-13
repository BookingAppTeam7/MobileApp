package com.example.bookingapp.model;

import com.example.bookingapp.model.enums.PriceTypeEnum;

public class PriceCard {
    public Long id;

    public TimeSlot timeSlot;
    public double price;
    public PriceTypeEnum type;

    private Boolean deleted;
}
