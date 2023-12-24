package com.example.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccommodationDetails implements Parcelable {
    private Accommodation accommodation;
    private double totalPrice;
    private double unitPrice;
    private double averageRating;

    public AccommodationDetails(Accommodation accommodation, double totalPrice, double unitPrice, double averageRating) {
        this.accommodation = accommodation;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
        this.averageRating = averageRating;
    }
    // Parcelable implementation for AccommodationDetails
    protected AccommodationDetails(Parcel in) {
        accommodation = in.readParcelable(Accommodation.class.getClassLoader());
        totalPrice = in.readDouble();
        unitPrice = in.readDouble();
        averageRating = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(accommodation, flags);
        dest.writeDouble(totalPrice);
        dest.writeDouble(unitPrice);
        dest.writeDouble(averageRating);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<AccommodationDetails> CREATOR = new Creator<AccommodationDetails>() {
        @Override
        public AccommodationDetails createFromParcel(Parcel in) {
            return new AccommodationDetails(in);
        }

        @Override
        public AccommodationDetails[] newArray(int size) {
            return new AccommodationDetails[size];
        }
    };
    @Override
    public String toString() {
        return "AccommodationDetails{" +
                "accommodation=" + accommodation +
                ", totalPrice=" + totalPrice +
                ", unitPrice=" + unitPrice +
                ", averageRating=" + averageRating +
                '}';
    }

    public AccommodationDetails() {
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
