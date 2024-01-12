package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityOwnerNotificationSettingsBinding;
import com.example.bookingapp.databinding.ActivityRegisterScreenBinding;
import com.example.bookingapp.fragments.accommodations.NotificationOwnerDialogFragment;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.DTOs.UserPostDTO;
import com.example.bookingapp.model.DTOs.UserPutDTO;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OwnerNotificationSettingsActivity extends AppCompatActivity {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public UserService userService = retrofit.create(UserService.class);
  //  private String username;
    private static User user;
    private static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityOwnerNotificationSettingsBinding binding=ActivityOwnerNotificationSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //this.username=TokenManager.getLoggedInUser().username;
        binding.buttonApply.setOnClickListener(new View.OnClickListener() {
            boolean reservationRequestNotification=false;
            boolean reservationCancellationNotification=false;
            boolean ownerRatingNotification=false;
            boolean accommodationRatingNotification=false;
            boolean ownerRepliedToRequestNotification=false;
            @Override
            public void onClick(View view) {


                RadioButton selectedRadioButtonReservationReq = findViewById(binding.radioGroupReservationRequest.getCheckedRadioButtonId());
                String yesNo = selectedRadioButtonReservationReq.getText().toString();
                if(yesNo.equals("Yes")){
                    reservationRequestNotification=true;
                }

                RadioButton selectedRadioButtonCancellation = findViewById(binding.radioGroupReservationCancellation.getCheckedRadioButtonId());
                 yesNo = selectedRadioButtonCancellation.getText().toString();
                if(yesNo.equals("Yes")){
                    reservationCancellationNotification=true;
                }
                RadioButton selectedRadioButtonOwnerRating = findViewById(binding.radioGroupOwnerRating.getCheckedRadioButtonId());
                yesNo = selectedRadioButtonOwnerRating.getText().toString();
                if(yesNo.equals("Yes")){
                    ownerRatingNotification=true;
                }
                RadioButton selectedRadioButtonAccomodationRating = findViewById(binding.radioGroupAccommodationRating.getCheckedRadioButtonId());
                yesNo =  selectedRadioButtonAccomodationRating.getText().toString();
                if(yesNo.equals("Yes")){
                    accommodationRatingNotification=true;
                }


                Call<User> call = userService.findUserById(TokenManager.getLoggedInUser().getUsername());
             //   User user=null;

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            // Uspešna prijava
                            user=response.body();
                            UserPutDTO newUser=new UserPutDTO(user.firstName,user.lastName,user.password,user.address,user.phoneNumber,user.status,reservationRequestNotification,reservationCancellationNotification,ownerRatingNotification,accommodationRatingNotification,ownerRepliedToRequestNotification, user.token, false,user.favouriteAccommodations);

                            Call<User> call1 = userService.updateUser(newUser,user.username);
                            call1.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.isSuccessful()) {
                                        User createdUser = response.body();
                                        Toast.makeText(OwnerNotificationSettingsActivity.this, "Successfully updated notification", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OwnerNotificationSettingsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });

                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
                        Log.d("TAAAAAAGGGGGGGGGG ", "Error: " + t.getMessage());
                    }
                });



                Intent intent=new Intent(OwnerNotificationSettingsActivity.this, HomeScreenActivity.class);
                intent.putExtra("role","OWNER");
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

