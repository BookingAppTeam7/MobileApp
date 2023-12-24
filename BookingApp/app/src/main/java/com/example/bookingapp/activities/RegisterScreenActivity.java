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
import com.example.bookingapp.databinding.ActivityRegisterScreenBinding;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.PriceCardPostDTO;
import com.example.bookingapp.model.DTOs.UserPostDTO;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.model.enums.StatusEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterScreenActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public UserService userService = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityRegisterScreenBinding binding=ActivityRegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create Account Screen");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = binding.editTextFirstName.getText().toString();
                String lastName = binding.editTextLastName.getText().toString();
                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                String passwordConfirmation = binding.editTextPasswordConfirmation.getText().toString();
                String phoneNumber = binding.editTextPhoneNumber.getText().toString();
                String address = binding.editTextAddress.getText().toString();

                RadioButton selectedRadioButton = findViewById(binding.radioGroupRole.getCheckedRadioButtonId());
                String role = selectedRadioButton.getText().toString();

                //notifications

                boolean reservationRequestNotification=true;
                boolean reservationCancellationNotification=true;
                boolean ownerRatingNotification=true;
                boolean accommodationRatingNotification=true;
                boolean ownerRepliedToRequestNotification=true;


                RoleEnum roleEnum=RoleEnum.GUEST;
                Log.e("ULOGA",role);
                if(role.equals("Guest")){roleEnum=RoleEnum.GUEST;}
                if(role.equals("Owner")){roleEnum=RoleEnum.OWNER;}//////////
                UserPostDTO newUser=new UserPostDTO(firstName,lastName,username,password,passwordConfirmation,roleEnum,address,
                phoneNumber,reservationRequestNotification,reservationCancellationNotification,ownerRatingNotification,accommodationRatingNotification,ownerRepliedToRequestNotification,false);

                Call<User> call = userService.create(newUser);
                call.enqueue(new Callback<User>() {
                                 @Override
                                 public void onResponse(Call<User> call, Response<User> response) {
                                     if (response.isSuccessful()) {
                                         User createdUser = response.body();
                                         Toast.makeText(RegisterScreenActivity.this, "Successfully registered!Mail is sent...", Toast.LENGTH_SHORT).show();
                                     } else {
                                         Toast.makeText(RegisterScreenActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                     }
                                 }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

                Intent intent=new Intent(RegisterScreenActivity.this, AccountScreenActivity.class);
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