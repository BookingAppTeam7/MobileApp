package com.example.bookingapp.services;

import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.AccommodationPostDTO;
import com.example.bookingapp.model.DTOs.PriceCardPostDTO;
import com.example.bookingapp.model.DTOs.PriceCardStringDTO;
import com.example.bookingapp.model.PriceCard;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PriceCardService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("priceCards/DatesString")
    Call<PriceCard> create(@Body PriceCardStringDTO newPriceCard);

    @PUT("priceCards/StringDates/{id}")
    Call<PriceCard> modify(@Body PriceCardStringDTO priceCard, @Path("id") Long id);
}
