package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.ReservationsListAdapter;
import com.example.bookingapp.adapters.UserListAdapter;
import com.example.bookingapp.databinding.ActivityAccommodationsReservationsBinding;
import com.example.bookingapp.databinding.ActivityUsersReviewBinding;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.StatusEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.ReservationService;
import com.example.bookingapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsersReviewActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

    public UserService userService = retrofit.create(UserService.class);

    UserListAdapter listAdapter;
    public ArrayList<UserGetDTO> usersToShow= new ArrayList<UserGetDTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUsersReviewBinding binding = ActivityUsersReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme


        Call call = userService.findAll();
        call.enqueue(new Callback<List<UserGetDTO>>() {
            @Override
            public void onResponse(Call<List<UserGetDTO>> call, Response<List<UserGetDTO>> response) {
                if (response.isSuccessful()) {

                    for(UserGetDTO u:response.body()){
                        usersToShow.add(u);
                    }

                    listAdapter = new UserListAdapter(UsersReviewActivity.this, usersToShow);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            User selectedUser = (User) parent.getItemAtPosition(position);

                        }
                    });
                } else {
                    // Handle error
                    Log.e("GRESKA",String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<UserGetDTO>> call, Throwable t) {
                Log.e("Error : ",t.getMessage());
                t.printStackTrace();
            }
        });


    }

}