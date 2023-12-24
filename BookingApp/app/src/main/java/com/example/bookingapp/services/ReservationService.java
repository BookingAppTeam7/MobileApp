package com.example.bookingapp.services;

import com.example.bookingapp.model.DTOs.ReservationPostDTO;
import com.example.bookingapp.model.Reservation;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reservations")
    Call<Reservation> createReservation(@Body RequestBody requestBody);
}
