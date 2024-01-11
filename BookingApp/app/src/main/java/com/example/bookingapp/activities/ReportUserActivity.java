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
import com.example.bookingapp.databinding.ActivityReportUserBinding;
import com.example.bookingapp.model.DTOs.ReviewPostDTO;
import com.example.bookingapp.model.DTOs.UserReportPostDTO;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.ReviewEnum;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.UserReport;
import com.example.bookingapp.model.enums.ReviewStatusEnum;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.ReviewService;
import com.example.bookingapp.services.UserReportService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

  public class ReportUserActivity extends AppCompatActivity {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public UserReportService reportService = retrofit.create(UserReportService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_report_user);
      ActivityReportUserBinding binding=ActivityReportUserBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      setContentView(binding.getRoot());

      Toolbar toolbar = binding.toolbar;

      setSupportActionBar(toolbar);

      getSupportActionBar().setTitle("Report user screen");

      getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
      toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme
      ///OVDE DODAJ
      Intent intent=getIntent();
//      binding.ownerUserName.setText(intent.getStringExtra("ownerId"));

      binding.buttonReportUser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          String reportedUserUserName= binding.userUserName.getText().toString();
          String reason = binding.CommentInput.getText().toString();

          String username= TokenManager.getLoggedInUser().username;

          UserReportPostDTO newUserReport=new UserReportPostDTO(username,reportedUserUserName,reason);

          Call<UserReport> call =reportService.create(newUserReport);
          call.enqueue(new Callback<UserReport>() {
            @Override
            public void onResponse(Call<UserReport> call, Response<UserReport> response) {
              String errorMessage = "Successfully reported!";
              if (response.isSuccessful()) {
               // Review createdReview = response.body();
                Toast.makeText(ReportUserActivity.this, "Successfully reported!", Toast.LENGTH_LONG).show();
              } else {
                //  String errorMessage = "Something went wrong...Please try later!";
                if (response.code() == 500 || response.code()==400) {
                  // Greška Server error, API nije prihvatio zahtev
                  errorMessage = "Bad Request: Invalid data. Please check your input.";
                  Toast.makeText(ReportUserActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
                Toast.makeText(ReportUserActivity.this, errorMessage, Toast.LENGTH_LONG).show();
              }
            }

            @Override
            public void onFailure(Call<UserReport> call, Throwable t) {
              //       Toast.makeText(RateOwnerActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
          });


          Intent intent=new Intent(ReportUserActivity.this, HomeScreenActivity.class);
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