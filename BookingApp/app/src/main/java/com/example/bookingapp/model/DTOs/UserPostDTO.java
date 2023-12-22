package com.example.bookingapp.model.DTOs;

import com.example.bookingapp.model.enums.RoleEnum;

public class UserPostDTO {
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public String passwordConfirmation;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public Boolean reservationRequestNotification;

    public Boolean reservationCancellationNotification;

    public Boolean ownerRatingNotification;

    public Boolean accommodationRatingNotification;

    //guest
    public Boolean ownerRepliedToRequestNotification;

    public Boolean deleted;

    public UserPostDTO(String firstName, String lastName, String username, String password, String passwordConfirmation, RoleEnum role, String address, String phoneNumber, Boolean reservationRequestNotification, Boolean reservationCancellationNotification, Boolean ownerRatingNotification, Boolean accommodationRatingNotification, Boolean ownerRepliedToRequestNotification, Boolean deleted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.reservationRequestNotification = reservationRequestNotification;
        this.reservationCancellationNotification = reservationCancellationNotification;
        this.ownerRatingNotification = ownerRatingNotification;
        this.accommodationRatingNotification = accommodationRatingNotification;
        this.ownerRepliedToRequestNotification = ownerRepliedToRequestNotification;
        this.deleted = deleted;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
