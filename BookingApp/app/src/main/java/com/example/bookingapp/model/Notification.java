package com.example.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class Notification implements Parcelable {
    public Long id;
    public String userId;
    public String type;
    public String content;
    public String dateTime;
    public boolean read;

    public Notification(Long id, String userId, String type, String content, String dateTime, boolean read) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.dateTime = dateTime;
        this.read=read;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(content);
        dest.writeString(dateTime);
    }
    protected Notification(Parcel in){
        userId=in.readString();
        content=in.readString();
        dateTime=in.readString();
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", read=" + read +
                '}';
    }
}
