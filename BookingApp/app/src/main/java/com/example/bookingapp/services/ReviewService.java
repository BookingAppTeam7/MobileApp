package com.example.bookingapp.services;

import com.example.bookingapp.model.DTOs.ReviewPostAccommodationDTO;
import com.example.bookingapp.model.DTOs.ReviewPostDTO;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.DTOs.UserPostDTO;
import com.example.bookingapp.model.DTOs.UserPutDTO;
import com.example.bookingapp.model.JwtAuthenticationRequest;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"

    })
    @POST("reviews/reviewsMobileApp")
    Call<Review> create(@Body ReviewPostDTO newReview);

    @POST("reviews/accommodations/reviewsMobileApp")
    Call<Review> createAccommodationReview(@Body ReviewPostAccommodationDTO newReview);

    @DELETE("reviews/{id}")
    Call<Void>delete(@Path("id") Long  id);


}
