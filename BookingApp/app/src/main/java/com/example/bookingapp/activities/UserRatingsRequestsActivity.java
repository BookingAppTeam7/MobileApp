package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.ReviewListAdapter;
import com.example.bookingapp.adapters.UserListAdapter;
import com.example.bookingapp.databinding.ActivityUserRatingsRequestsBinding;
import com.example.bookingapp.databinding.ActivityUsersReviewBinding;
import com.example.bookingapp.model.DTOs.ReviewGetDTO;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.ReviewStatusEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.ReviewService;
import com.example.bookingapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRatingsRequestsActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

    public ReviewService reviewService = retrofit.create(ReviewService.class);

    ReviewListAdapter listAdapter;
    public ArrayList<ReviewGetDTO> reviewsToShow= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserRatingsRequestsBinding binding = ActivityUserRatingsRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme

        Intent intent=getIntent();

        Call call = reviewService.findByOwnerId(intent.getStringExtra("username"));
        call.enqueue(new Callback<List<ReviewGetDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewGetDTO>> call, Response<List<ReviewGetDTO>> response) {
                if (response.isSuccessful()) {

                    for(ReviewGetDTO r:response.body()){
                        if(r.getStatus()== ReviewStatusEnum.PENDING) {
                            reviewsToShow.add(r);
                        }
                    }

                    listAdapter = new ReviewListAdapter(UserRatingsRequestsActivity.this, reviewsToShow);
                    binding.listview.setAdapter(listAdapter);
                    //binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            ReviewGetDTO selectedReview = (ReviewGetDTO) parent.getItemAtPosition(position);

                        }
                    });
                } else {
                    // Handle error
                    Log.e("GRESKA",String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<ReviewGetDTO>> call, Throwable t) {
                Log.e("Error : ",t.getMessage());
                t.printStackTrace();
            }
        });


    }


}