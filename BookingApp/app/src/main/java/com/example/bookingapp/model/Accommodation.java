package com.example.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Accommodation implements Parcelable {
    private Long id;
    private String Name;
    private String Description;
    private int minGuests;
    private int maxGuests;
    private int image;
    private String Location;
    private double locationX;
    private double locationY;
    private double price;
    private List<Review> reviews;
    private List<String> assets;
    private List<TimeSlot> availability;
    public Accommodation(Long id, String Name, String Description, int minGuests,int maxGuests,
                         int image,String Location, double locationX, double locationY, double price,
                         List<Review> reviews,List<String> assets,List<TimeSlot> availability){
        this.id=id;
        this.Name=Name;
        this.Description=Description;
        this.minGuests=minGuests;
        this.maxGuests=maxGuests;
        this.image=image;
        this.Location=Location;
        this.locationX=locationX;
        this.locationY=locationY;
        this.price=price;
        this.reviews=reviews;
        this.assets=assets;
        this.availability=availability;
    }
    public Accommodation(){

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

    public List<TimeSlot> getAvailability() {
        return availability;
    }

    public void setAvailability(List<TimeSlot> availability) {
        this.availability = availability;
    }

    public List<String> getAssets() {
        return assets;
    }

    public void setAssets(List<String> assets) {
        this.assets = assets;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    protected Accommodation(Parcel in) {
        // Čitanje ostalih atributa proizvoda iz Parcel objekta
        id = in.readLong();
        Name = in.readString();
        Description = in.readString();
        image = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(Name);
        dest.writeString(Description);
        dest.writeInt(image);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", image=" + image +
                '}';
    }

    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
        @Override
        public Accommodation createFromParcel(Parcel in) {
            return new Accommodation(in);
        }

        @Override
        public Accommodation[] newArray(int size) {
            return new Accommodation[size];
        }
    };
}
