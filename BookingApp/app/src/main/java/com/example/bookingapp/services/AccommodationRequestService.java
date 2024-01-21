package com.example.bookingapp.services;

import com.example.bookingapp.model.AccommodationRequest;
import com.example.bookingapp.model.enums.AccommodationRequestStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationRequestService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("accommodationsRequests/status/{status1}/{status2}")
    Call<List<AccommodationRequest>> findByStatus(
            @Path("status1") AccommodationRequestStatus status1,
            @Path("status2") AccommodationRequestStatus status2
    );

    @PUT("/accommodationsRequests/{id}/update-status")
    Call<AccommodationRequest> updateStatus(
            @Path("id") Long requestId,
            @Query("status") AccommodationRequestStatus status
    );




}
