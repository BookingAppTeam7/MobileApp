package com.example.bookingapp.services;

import com.example.bookingapp.model.DTOs.NotificationPostDTO;
import com.example.bookingapp.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("notifications")
    Call<Notification> createNotification(@Body NotificationPostDTO newNotification);

    @GET("notifications")
    Call<List<Notification>> getAllNotifications();

    @GET("notifications/{id}")
    Call<Notification> getNotificationById(@Path("id") Long id);

    @GET("notifications/user/{id}")
    Call<List<Notification>> getNotificationsByUserId(@Path("id") String userId);

    @PUT("notifications/read/{id}")
    Call<Void> readNotification(@Path("id") Long id);
    @DELETE("notifications/{id}")
    Call<Void> deleteNotification(@Path("id") Long id);
}
