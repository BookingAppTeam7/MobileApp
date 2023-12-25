package com.example.bookingapp.model.DTOs;

public class LocationPutDTO {
    public String address;
    public String city;
    public String country;
    public double x;
    public double y;

    public LocationPutDTO(String address, String city, String country, double x, double y) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.x = x;
        this.y = y;
    }

    public LocationPutDTO() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
