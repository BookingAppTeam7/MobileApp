package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityRateOwnerBinding;
import com.example.bookingapp.databinding.ActivityRegisterScreenBinding;
import com.example.bookingapp.model.DTOs.ReviewPostDTO;
import com.example.bookingapp.model.DTOs.UserPostDTO;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.ReviewEnum;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.ReviewStatusEnum;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.ReviewService;
import com.example.bookingapp.services.UserService;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RateOwnerActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public ReviewService reviewService = retrofit.create(ReviewService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rate_owner);
        ActivityRateOwnerBinding binding=ActivityRateOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Rate owner screen");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme

        binding.buttonRateOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ownerUserName= binding.ownerUserName.getText().toString();
                String comment = binding.CommentInput.getText().toString();
                RadioButton selectedRadioButton = findViewById(binding.radioGroupRole.getCheckedRadioButtonId());
                String gradeText = selectedRadioButton.getText().toString();
                int grade=Integer.parseInt(gradeText);
                RoleEnum roleEnum=RoleEnum.GUEST;

                String username= TokenManager.getLoggedInUser().username;

                ReviewPostDTO newReview=new ReviewPostDTO(username, ReviewEnum.OWNER,comment,grade,false,0L,ownerUserName, ReviewStatusEnum.PENDING);

                Call<Review> call = reviewService.create(newReview);
                call.enqueue(new Callback<Review>() {
                    @Override
                    public void onResponse(Call<Review> call, Response<Review> response) {
                        if (response.isSuccessful()) {
                            Review createdReview = response.body();
                            Toast.makeText(RateOwnerActivity.this, "Successfully rated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RateOwnerActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Review> call, Throwable t) {

                    }
                });

                Intent intent=new Intent(RateOwnerActivity.this, AccountScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}