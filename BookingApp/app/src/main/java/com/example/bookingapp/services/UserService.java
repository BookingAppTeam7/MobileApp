package com.example.bookingapp.services;

import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.AccommodationPostDTO;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.DTOs.UserPostDTO;
import com.example.bookingapp.model.JwtAuthenticationRequest;
import com.example.bookingapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("users")
    Call<User> create(@Body UserPostDTO newUser);

    @POST("login")
    Call<User> loginUser(@Body JwtAuthenticationRequest user);

    @GET("username/{username}")
    Call<UserGetDTO> findById(@Path("username") String username);
}
