package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityGuestNotificationSettingsBinding;
import com.example.bookingapp.databinding.ActivityOwnerNotificationSettingsBinding;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.DTOs.UserPutDTO;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.User;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GuestNotificationSettingsActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public UserService userService = retrofit.create(UserService.class);
    private static User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGuestNotificationSettingsBinding binding=ActivityGuestNotificationSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        GuestNotificationSettingsActivity.this.user= TokenManager.getLoggedInUser();

        binding.buttonApply.setOnClickListener(new View.OnClickListener() {
            boolean reservationRequestNotification=false;
            boolean reservationCancellationNotification=false;
            boolean ownerRatingNotification=false;
            boolean accommodationRatingNotification=false;
            boolean ownerRepliedToRequestNotification=false;
            @Override
            public void onClick(View view) {


                RadioButton selectedRadioButtonOwnerReplied = findViewById(binding.radioGroupOwnerReplied.getCheckedRadioButtonId());
                String yesNo = selectedRadioButtonOwnerReplied.getText().toString();
                if(yesNo.equals("Yes")){
                    ownerRepliedToRequestNotification=true;
                }



                UserPutDTO newUser=new UserPutDTO(GuestNotificationSettingsActivity.this.user.firstName,user.lastName,user.password,user.address,user.phoneNumber,user.status,reservationRequestNotification,reservationCancellationNotification,ownerRatingNotification,accommodationRatingNotification,ownerRepliedToRequestNotification, user.token, false);

                Call<User> call = userService.updateUser(newUser,user.username);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User createdUser = response.body();
                            Toast.makeText(GuestNotificationSettingsActivity.this, "Successfully updated notification", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GuestNotificationSettingsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

                Intent intent=new Intent(GuestNotificationSettingsActivity.this, HomeScreenActivity.class);
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