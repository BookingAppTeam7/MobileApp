package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityRateAccommodationBinding;
import com.example.bookingapp.databinding.ActivityRateOwnerBinding;
import com.example.bookingapp.model.DTOs.ReviewPostAccommodationDTO;
import com.example.bookingapp.model.DTOs.ReviewPostDTO;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.ReviewEnum;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.enums.ReviewStatusEnum;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.ReviewService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RateAccommodationActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public ReviewService reviewService = retrofit.create(ReviewService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rate_accommodation);
        ActivityRateAccommodationBinding binding=ActivityRateAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Rate accommodation screen");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme
        ///OVDE DODAJ



        binding.buttonRateAccommodation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                Long resId=intent.getLongExtra("reservationId",0L);
                Log.d("DDDDDDD",resId.toString());


                String comment = binding.CommentInput.getText().toString();
                RadioButton selectedRadioButton = findViewById(binding.radioGroupRole.getCheckedRadioButtonId());
                String gradeText = selectedRadioButton.getText().toString();
                int grade=Integer.parseInt(gradeText);
                RoleEnum roleEnum=RoleEnum.GUEST;

                String username= TokenManager.getLoggedInUser().username;

                ReviewPostAccommodationDTO newReview=new ReviewPostAccommodationDTO(username,ReviewEnum.ACCOMMODATION,
                        comment,grade,false,false,
                        intent.getLongExtra("accommodationId",0L),intent.getStringExtra("ownerIdA"),
                        ReviewStatusEnum.PENDING,resId);

                Call<Review> call = reviewService.createAccommodationReview(newReview);
                call.enqueue(new Callback<Review>() {
                    @Override
                    public void onResponse(Call<Review> call, Response<Review> response) {
                        String errorMessage = "Successfully rated!";
                        if (response.isSuccessful()) {
                            Review createdReview = response.body();
                            Toast.makeText(RateAccommodationActivity.this, "Successfully rated!", Toast.LENGTH_LONG).show();
                        } else {
                            //  String errorMessage = "Something went wrong...Please try later!";
                            if (response.code() == 400) {
                                // Greška Bad Request, API nije prihvatio zahtev
                                errorMessage = "Bad Request: Invalid data. Please check your input.";
                                Toast.makeText(RateAccommodationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                // Ostale greške


                                // Provera da li odgovor sadrži detalje o grešci
                                if (response.errorBody() != null) {
                                    try {
                                        // Čitanje detalja o grešci iz response body-ja
                                        errorMessage = response.errorBody().string();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Ispisivanje greške

                            }
                            Toast.makeText(RateAccommodationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Review> call, Throwable t) {
                     //   Toast.makeText(RateAccommodationActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

                    }
                });


                intent=new Intent(RateAccommodationActivity.this, HomeScreenActivity.class);
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