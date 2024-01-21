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
import com.example.bookingapp.fragments.accommodations.FilterReservationBottomSheetFragment;
import com.example.bookingapp.fragments.accommodations.ReservationBottomSheetFragment;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.ReservationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GuestsReservationsActivity extends AppCompatActivity implements BottomSheetListener {

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
                    reservationsToShow.clear();

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
                    listAdapter.notifyDataSetChanged();
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

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterReservationBottomSheetFragment bottomSheetFragment = new FilterReservationBottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(),bottomSheetFragment.getTag());
            }
        });
    }

    @Override
    public void onSearchButtonClicked(String place, int guests, String arrivalDate, String checkoutDate) {

    }

    @Override
    public void onFilterButtonClicked(TypeEnum selectedType, String joined, String minTotalPrice, String maxTotalPrice) {

    }

    @Override
    public void onReservationButtonClicked(int guests, String arrivalDate, String checkoutDate) {

    }

    @Override
    public void onFilterReservationButtonClicked(String accName, String arrivalDate, String checkoutDate, ReservationStatusEnum status) {
        String fullArrivalDate=arrivalDate+" 00:00:00";
        String fullCheckoutDate=checkoutDate+" 00:00:00";

        Call<List<Reservation>> call = reservationService.searchFilter(accName, fullArrivalDate, fullCheckoutDate, status);
        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> reservations = response.body();
                    for(Reservation r:reservations){
                        Log.e("REZERVACIJA",r.toString());
                    }
                    reservationsToShow.clear();
                    reservationsToShow.addAll(reservations);
                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.e("NEKA GRESKA ","USAO U ELSE");
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("FAILURE","FAILURE");
            }
        });
    }
}