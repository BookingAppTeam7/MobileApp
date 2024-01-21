package com.example.bookingapp.services;

import com.example.bookingapp.model.DTOs.ReviewPostAccommodationDTO;

import com.example.bookingapp.model.DTOs.ReviewGetDTO;

import com.example.bookingapp.model.DTOs.ReviewPostDTO;
import com.example.bookingapp.model.DTOs.ReviewPutDTO;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.DTOs.UserPostDTO;
import com.example.bookingapp.model.DTOs.UserPutDTO;
import com.example.bookingapp.model.JwtAuthenticationRequest;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.User;

import java.util.List;

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

    @GET("reviews/accommodation/pending/{accommodationId}")
    Call<List<Review>> findPendingByAccommodationId(@Path("accommodationId") Long accommodationId);

    @GET("reviews/mobile/owner/{ownerId}")
    Call<List<ReviewGetDTO>> findByOwnerId(@Path("ownerId") String ownerId);

    @GET("reviews/mobile/accommodation/{accommodationId}")
    Call<List<ReviewGetDTO>> findByAccommodationId(@Path("accommodationId") Long accommodationId);

    @PUT("reviews/{id}/update-status/approve")
    Call<Void> approve(@Path("id") Long reviewId);

    @PUT("reviews/mobileApps/{id}")
    Call<Void> update(@Body ReviewPutDTO reviewPutDTO, @Path("id") Long reviewId);

    @PUT("reviews/{id}/update-status/reject")
    Call<Void> reject(@Path("id") Long reviewId);
}
