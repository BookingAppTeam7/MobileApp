package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.adapters.AccomodationApprovalListAdapter;
import com.example.bookingapp.adapters.PendingReservationsListAdapter;
import com.example.bookingapp.databinding.ActivityAccommodationsReservationsBinding;
import com.example.bookingapp.databinding.ActivityEditAccommodationsDataBinding;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationRequest;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationRequestService;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.ReservationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccommodationsReservationsActivity extends AppCompatActivity {


    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);

    public ReservationService reservationService = retrofit.create(ReservationService.class);
    PendingReservationsListAdapter listAdapter;
    public ArrayList<Reservation> requests = new ArrayList<Reservation>();
    public ArrayList<Reservation> reservationsToShow= new ArrayList<Reservation>();
    //Accommodation accommodation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAccommodationsReservationsBinding binding = ActivityAccommodationsReservationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Accommodation");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme


        Long accommodationId = getIntent().getLongExtra("accommodationId", -1);

        Call call = reservationService.findByAccommodationId(accommodationId);
        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {

                    for(Reservation r:response.body()){
                        if(r.status== ReservationStatusEnum.PENDING) {
                            reservationsToShow.add(r);
                        }
                    }

                    listAdapter = new PendingReservationsListAdapter(AccommodationsReservationsActivity.this, reservationsToShow);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Reservation selectedReservation = (Reservation) parent.getItemAtPosition(position);

//                            Intent intent = new Intent(AccommodationsReservationsActivity.this, EditAccommodationsDataActivity.class);
//                            intent.putExtra("accommodationId", accommodationId);
//                            startActivity(intent);
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