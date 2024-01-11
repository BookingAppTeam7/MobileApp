package com.example.bookingapp.services;

import com.example.bookingapp.model.DTOs.UserReportPostDTO;
import com.example.bookingapp.model.UserReport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserReportService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"

    })

    @POST("userReports")
    Call<UserReport> create(@Body UserReportPostDTO newUserReport);

    @GET("userReports")
    Call<List<UserReport>> findAll();

    @PUT("userReports/report/{id}")
    Call<UserReport> report(@Path("id") Long requestId);

    @PUT("userReports/ignore/{id}")
    Call<UserReport> ignoreReport(@Path("id") Long requestId);

    @GET("userReports/user/{id}")
    Call<List<UserReport>> findByUser(@Path("id") String userId);


}
