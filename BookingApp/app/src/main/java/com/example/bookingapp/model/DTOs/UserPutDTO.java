package com.example.bookingapp.model.DTOs;

import com.example.bookingapp.model.enums.StatusEnum;

public class UserPutDTO {

    public String firstName;
    public String lastName;
    //  public String username;
    public String password;
    // public RoleEnum role;

    public String address;
    public String phoneNumber;

    public StatusEnum status;
    public Boolean reservationRequestNotification;

    public Boolean reservationCancellationNotification;

    public Boolean ownerRatingNotification;

    public Boolean accommodationRatingNotification;

    //guest
    public Boolean ownerRepliedToRequestNotification;
    public Boolean deleted;
    public String token;

    public UserPutDTO(String firstName, String lastName, String password, String address, String phoneNumber, StatusEnum status, Boolean reservationRequestNotification,
                      Boolean reservationCancellationNotification, Boolean ownerRatingNotification,
                      Boolean accommodationRatingNotification, Boolean ownerRepliedToRequestNotification,
                      String token, Boolean deleted) {
        this.firstName = firstName;
        this.lastName = lastName;
        //  this.username = username;
        this.password = password;

        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.reservationRequestNotification = reservationRequestNotification;
        this.reservationCancellationNotification = reservationCancellationNotification;
        this.ownerRatingNotification = ownerRatingNotification;
        this.accommodationRatingNotification = accommodationRatingNotification;
        this.ownerRepliedToRequestNotification = ownerRepliedToRequestNotification;
        this.token=token;
        this.deleted=deleted;
    }

    public Boolean getReservationRequestNotification() {
        return reservationRequestNotification;
    }

    public void setReservationRequestNotification(Boolean reservationRequestNotification) {
        this.reservationRequestNotification = reservationRequestNotification;
    }

    public Boolean getReservationCancellationNotification() {
        return reservationCancellationNotification;
    }

    public void setReservationCancellationNotification(Boolean reservationCancellationNotification) {
        this.reservationCancellationNotification = reservationCancellationNotification;
    }

    public Boolean getOwnerRatingNotification() {
        return ownerRatingNotification;
    }

    public void setOwnerRatingNotification(Boolean ownerRatingNotification) {
        this.ownerRatingNotification = ownerRatingNotification;
    }

    public Boolean getAccommodationRatingNotification() {
        return accommodationRatingNotification;
    }

    public void setAccommodationRatingNotification(Boolean accommodationRatingNotification) {
        this.accommodationRatingNotification = accommodationRatingNotification;
    }

    public Boolean getOwnerRepliedToRequestNotification() {
        return ownerRepliedToRequestNotification;
    }

    public void setOwnerRepliedToRequestNotification(Boolean ownerRepliedToRequestNotification) {
        this.ownerRepliedToRequestNotification = ownerRepliedToRequestNotification;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

//    public String getUsername() {
//        return username;
//    }

    public String getPassword() {
        return password;
    }
//
// //   public RoleEnum getRole() {
//        return role;
//    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//    public void setUsername(String username) {
//        this.username = username;
//    }

    public void setPassword(String password) {
        this.password = password;
    }
//
//  //  public void setRole(RoleEnum role) {
//        this.role = role;
//    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

}
