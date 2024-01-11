package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.ReservationsListAdapter;
import com.example.bookingapp.databinding.ActivityAccommodationsReservationsBinding;
import com.example.bookingapp.databinding.ActivityGuestsReservationsBinding;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.ReservationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GuestsReservationsActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);

    public ReservationService reservationService = retrofit.create(ReservationService.class);
    ReservationsListAdapter listAdapter;
    public ArrayList<Reservation> requests = new ArrayList<Reservation>();
    public ArrayList<Reservation> reservationsToShow= new ArrayList<Reservation>();
    //Accommodation accommodation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGuestsReservationsBinding binding = ActivityGuestsReservationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme


        Call call = reservationService.findByGuestId(TokenManager.getLoggedInUser().username);
        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {

                    for(Reservation r:response.body()){
                            reservationsToShow.add(r);
                    }

                    listAdapter = new ReservationsListAdapter(GuestsReservationsActivity.this, reservationsToShow);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Reservation selectedReservation = (Reservation) parent.getItemAtPosition(position);


                        }
                    });
                } else {
                    // Handle error
                    Log.e("GRESKA",String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("Error : ",t.getMessage());
                t.printStackTrace();
            }
        });


    }
}