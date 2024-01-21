package com.example.bookingapp.services;

import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.ReservationPostDTO;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.enums.ReservationStatusEnum;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reservations")
    Call<Reservation> createReservation(@Body RequestBody requestBody);

    @GET("reservations/accommodation/{id}")
    Call<List<Reservation>> findByAccommodationId(@Path("id") Long id);

    @PUT("reservations/reject/{id}")
    Call<Void> rejectReservation(@Path("id") Long id);

    @PUT("reservations/cancel/{id}")
    Call<Void> cancelReservation(@Path("id") Long id);

    @PUT("reservations/confirm/{id}")
    Call<Void> confirmReservation(@Path("id") Long id);

    @GET("reservations/user/{id}")
    Call<List<Reservation>> findByGuestId(@Path("id") String id);
    @GET("reservations/searchFilter")
    Call<List<Reservation>> searchFilter(
            @Query("accName") String accName,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("status") ReservationStatusEnum status
    );
    @DELETE("reservations/{id}")
    Call<Void> deleteReservation(@Path("id") Long id);

    @GET("reservations")
    Call<List<Reservation>> findAll();
}
