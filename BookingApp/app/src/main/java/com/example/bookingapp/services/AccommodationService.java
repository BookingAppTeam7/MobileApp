package com.example.bookingapp.services;

import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.AccommodationPostDTO;
import com.example.bookingapp.model.DTOs.UserGetDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccommodationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations")
    Call<Accommodation> create(@Body AccommodationPostDTO newAccommodation);

    @GET("accommodations/approved")
    Call<List<Accommodation>> findAllApproved();

    @GET("accommodations/owner/{ownerId}")
    Call<List<Accommodation>> findByOwnerId(@Path("ownerId") String ownerId);

    @GET("accommodations/{id}")
    Call<Accommodation> findById(@Path("id") Long id);
}
