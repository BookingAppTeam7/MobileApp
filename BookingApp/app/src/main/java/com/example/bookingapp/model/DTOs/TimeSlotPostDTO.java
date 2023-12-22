package com.example.bookingapp.model.DTOs;

import java.time.LocalDate;
import java.util.Date;

public class TimeSlotPostDTO {
    private Date startDate;
    private Date endDate;



    public TimeSlotPostDTO(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
