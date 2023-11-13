package com.example.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Accommodation implements Parcelable {
    private Long id;
    private String Name;
    private String Description;
    private int image;

    public Accommodation(Long id, String Name, String Description, int image){
        this.id=id;
        this.Name=Name;
        this.Description=Description;
        this.image=image;
    }
    public Accommodation(){

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
