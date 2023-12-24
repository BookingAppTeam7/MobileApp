package com.example.bookingapp.model.DTOs;

import com.example.bookingapp.model.enums.ReservationConfirmationEnum;
import com.example.bookingapp.model.enums.TypeEnum;

import java.util.List;

public class AccommodationPutDTO {
    public String name;
    public String description;
    public LocationPutDTO location;
    public int minGuests;
    public int maxGuests;
    public TypeEnum type;
    public List<String> assets;
    //public List<PriceCard> prices;
    public String ownerId;
    public int cancellationDeadline;
    public ReservationConfirmationEnum reservationConfirmation;
    // public List<Review> reviews;
    public List<String> images;


    public AccommodationPutDTO(String name, String description, LocationPutDTO location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, String ownerId,  int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation,List<String> images) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        //this.prices = prices;
        this.ownerId = ownerId;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        //this.reviews = reviews;
        this.images=images;
    }

    public AccommodationPutDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationPutDTO getLocation() {
        return location;
    }

    public void setLocation(LocationPutDTO location) {
        this.location = location;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public List<String> getAssets() {
        return assets;
    }

    public void setAssets(List<String> assets) {
        this.assets = assets;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public ReservationConfirmationEnum getReservationConfirmation() {
        return reservationConfirmation;
    }

    public void setReservationConfirmation(ReservationConfirmationEnum reservationConfirmation) {
        this.reservationConfirmation = reservationConfirmation;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
