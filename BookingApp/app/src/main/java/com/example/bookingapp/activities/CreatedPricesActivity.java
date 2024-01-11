package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.PricesListAdapter;
import com.example.bookingapp.adapters.ReservationsListAdapter;
import com.example.bookingapp.databinding.ActivityAccommodationsReservationsBinding;
import com.example.bookingapp.databinding.ActivityCreatedPricesBinding;
import com.example.bookingapp.model.DTOs.PriceCardStringDTO;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.ReservationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class CreatedPricesActivity extends AppCompatActivity {


    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

    PricesListAdapter listAdapter;

    public ActivityCreatedPricesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityCreatedPricesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme


        Intent intent = getIntent();
        List<PriceCardStringDTO> pricesToShow = (List<PriceCardStringDTO>) intent.getSerializableExtra("pricesToShow");

        listAdapter = new PricesListAdapter(CreatedPricesActivity.this, pricesToShow);
        binding.listview.setAdapter(listAdapter);

    }
}