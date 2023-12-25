package com.example.bookingapp.services;

import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationDetails;
import com.example.bookingapp.model.DTOs.AccommodationPostDTO;

import com.example.bookingapp.model.DTOs.AccommodationPutDTO;
import com.example.bookingapp.model.DTOs.UserGetDTO;

import com.example.bookingapp.model.enums.TypeEnum;


import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import retrofit2.http.PUT;
import retrofit2.http.Path;

import retrofit2.http.Path;
import retrofit2.http.Query;


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

    @PUT("accommodations/{id}")
    Call<Accommodation> update(@Body AccommodationPutDTO accommodation, @Path("id") Long id);

    @GET("accommodations/search")
    Call<List<AccommodationDetails>> search(
            @Query("city") String city,
            @Query("guests") int guests,
            @Query("arrivalString") String arrival,
            @Query("checkoutString") String checkout
    );


    @GET("accommodations/filter")
    Call<List<AccommodationDetails>> filter(
            //@Query("searched") String searched,
            @Query("assets") String assets,
            @Query("type") TypeEnum type,
            @Query("minTotalPrice") String minTotalPrice,
            @Query("maxTotalPrice") String maxTotalPrice
    );
}
