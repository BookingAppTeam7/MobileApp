package com.example.bookingapp.model.DTOs;

import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.model.enums.StatusEnum;

public class UserGetDTO {
    public String firstName;
    public String lastName;
    public String username;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public StatusEnum status;
    public Boolean deleted;
    public String token;

    public Boolean reservationRequestNotification;

    public Boolean reservationCancellationNotification;

    public Boolean ownerRatingNotification;

    public Boolean accommodationRatingNotification;
    //guest

    public Boolean ownerRepliedToRequestNotification;

    public String jwt;
}
