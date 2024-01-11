package com.example.bookingapp.model.DTOs;

import java.io.Serializable;

public class TimeSlotStringDTO implements Serializable {

    String startDate;
    String endDate;

    public TimeSlotStringDTO(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
