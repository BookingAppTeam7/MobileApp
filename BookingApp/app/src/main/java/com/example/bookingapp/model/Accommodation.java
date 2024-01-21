package com.example.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.bookingapp.model.enums.AccommodationStatusEnum;
import com.example.bookingapp.model.enums.ReservationConfirmationEnum;
import com.example.bookingapp.model.enums.TypeEnum;

import java.util.List;

public class Accommodation implements Parcelable {

    public Long id;
    public String name;
    public String description;
    public Location location;
    public int minGuests;
    public int maxGuests;

    public TypeEnum type;

    public List<String> assets;

    public List<PriceCard> prices;

    public String ownerId;

    public AccommodationStatusEnum status;
    public int cancellationDeadline;

    public ReservationConfirmationEnum reservationConfirmation;

    public List<Review> reviews;

    public List<String> images;

    private Boolean deleted;

    public Accommodation(Long id, String name, String description, Location location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, List<PriceCard> prices, String ownerId, AccommodationStatusEnum status, int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation, List<Review> reviews, List<String> images, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        this.prices = prices;
        this.ownerId = ownerId;
        this.status = status;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        this.reviews = reviews;
        this.images = images;
        this.deleted = deleted;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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

    public List<PriceCard> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceCard> prices) {
        this.prices = prices;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public AccommodationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatusEnum status) {
        this.status = status;
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        //dest.writeInt(image);
    }

    protected Accommodation(Parcel in) {
        // Čitanje ostalih atributa proizvoda iz Parcel objekta
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        //image = in.readInt();
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", minGuests=" + minGuests +
                ", maxGuests=" + maxGuests +
                ", type=" + type +
                ", assets=" + assets +
                ", prices=" + prices +
                ", ownerId='" + ownerId + '\'' +
                ", status=" + status +
                ", cancellationDeadline=" + cancellationDeadline +
                ", reservationConfirmation=" + reservationConfirmation +
                ", reviews=" + reviews +
                ", images=" + images +
                ", deleted=" + deleted +
                '}';
    }
//    private Long id;
//    private String name;
//    private String description;
//    private int minGuests;
//    private int maxGuests;
//    //private int image;
//   // private String Location;
//    private double locationX;
//    private double locationY;
//    private double price;
//    private List<Review> reviews;
//    private List<String> assets;
//    private List<TimeSlot> availability;
//    private List<String> images;
//    public Accommodation(Long id, String Name, String Description, int minGuests,int maxGuests,
//                         int image,String Location, double locationX, double locationY, double price,
//                         List<Review> reviews,List<String> assets,List<TimeSlot> availability){
//        this.id=id;
//        this.Name=Name;
//        this.Description=Description;
//        this.minGuests=minGuests;
//        this.maxGuests=maxGuests;
//        this.image=image;
//        this.Location=Location;
//        this.locationX=locationX;
//        this.locationY=locationY;
//        this.price=price;
//        this.reviews=reviews;
//        this.assets=assets;
//        this.availability=availability;
//    }
//    public Accommodation(){
//
//    }
//
//    public int getMinGuests() {
//        return minGuests;
//    }
//
//    public void setMinGuests(int minGuests) {
//        this.minGuests = minGuests;
//    }
//
//    public int getMaxGuests() {
//        return maxGuests;
//    }
//
//    public void setMaxGuests(int maxGuests) {
//        this.maxGuests = maxGuests;
//    }
//
//    public List<TimeSlot> getAvailability() {
//        return availability;
//    }
//
//    public void setAvailability(List<TimeSlot> availability) {
//        this.availability = availability;
//    }
//
//    public List<String> getAssets() {
//        return assets;
//    }
//
//    public void setAssets(List<String> assets) {
//        this.assets = assets;
//    }
//
//    public List<Review> getReviews() {
//        return reviews;
//    }
//
//    public void setReviews(List<Review> reviews) {
//        this.reviews = reviews;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    protected Accommodation(Parcel in) {
//        // Čitanje ostalih atributa proizvoda iz Parcel objekta
//        id = in.readLong();
//        Name = in.readString();
//        Description = in.readString();
//        image = in.readInt();
//    }
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(@NonNull Parcel dest, int flags) {
//        dest.writeLong(id);
//        dest.writeString(Name);
//        dest.writeString(Description);
//        dest.writeInt(image);
//    }
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return Name;
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }
//
//    public String getDescription() {
//        return Description;
//    }
//
//    public void setDescription(String description) {
//        Description = description;
//    }
//
//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }
//
//    public String getLocation() {
//        return Location;
//    }
//
//    public void setLocation(String location) {
//        Location = location;
//    }
//
//    public double getLocationX() {
//        return locationX;
//    }
//
//    public void setLocationX(double locationX) {
//        this.locationX = locationX;
//    }
//
//    public double getLocationY() {
//        return locationY;
//    }
//
//    public void setLocationY(double locationY) {
//        this.locationY = locationY;
//    }
//
//    @Override
//    public String toString() {
//        return "Accommodation{" +
//                "id=" + id +
//                ", Name='" + Name + '\'' +
//                ", Description='" + Description + '\'' +
//                ", image=" + image +
//                '}';
//    }
//
//    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
//        @Override
//        public Accommodation createFromParcel(Parcel in) {
//            return new Accommodation(in);
//        }
//
//        @Override
//        public Accommodation[] newArray(int size) {
//            return new Accommodation[size];
//        }
//    };
}
