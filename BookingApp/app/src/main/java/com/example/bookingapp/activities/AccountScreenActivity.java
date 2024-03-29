package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityAccountScreenBinding;
import com.example.bookingapp.databinding.ActivityRegisterScreenBinding;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.DTOs.UserPutDTO;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.User;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.UserService;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountScreenActivity extends AppCompatActivity {

    private Animation slideInAnimation;
    private Animation slideOutAnimation;
    private boolean isDrawerOpen = false;

    private String username;
    private static  UserGetDTO user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityAccountScreenBinding binding = ActivityAccountScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.username= TokenManager.getLoggedInUser().username;

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);


        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;
        slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);


        UserService userService = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
        Call<UserGetDTO> call = userService.findById(this.username);

        call.enqueue(new Callback<UserGetDTO>() {
            @Override
            public void onResponse(Call<UserGetDTO> call, Response<UserGetDTO> response) {
                if (response.isSuccessful()) {
                    // Uspešna prijava
                    user=response.body();
                    EditText editTextFirstName = findViewById(R.id.editTextFirstName);
                    editTextFirstName.setText(user.firstName);
                    EditText editTextLastName = findViewById(R.id.editTextLastName);
                    editTextLastName.setText(user.lastName);
                    EditText editTextUsername = findViewById(R.id.editTextUsername);
                    editTextUsername.setText(user.username);
                    EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
                    editTextPhoneNumber.setText(user.phoneNumber);
                    EditText editTextAddress = findViewById(R.id.editTextAddress);
                    editTextAddress.setText(user.address);

                }
            }
            @Override
            public void onFailure(Call<UserGetDTO> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAAAAAAGGGGGGGGGG ", "Error: " + t.getMessage());
            }
        });
        ;

        binding.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountScreenActivity.this,"BRISANJEEE",Toast.LENGTH_LONG).show();
                UserService userService = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
                Call<Void> call = userService.delete(TokenManager.getLoggedInUser().username);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        Toast.makeText(AccountScreenActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AccountScreenActivity.this, "Cannot delete user", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        binding.saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountScreenActivity.this,"SUCCESSFULLY UPDATED",Toast.LENGTH_LONG).show();
                UserService userService = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
                User user=TokenManager.getLoggedInUser();
                EditText editTextFirstName = findViewById(R.id.editTextFirstName);
                EditText editTextLastName = findViewById(R.id.editTextLastName);
                EditText editTextUsername = findViewById(R.id.editTextUsername);
                EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
                EditText editTextAddress = findViewById(R.id.editTextAddress);
                EditText editPassword=findViewById(R.id.editTextPassword);
                EditText editPasswordConfirmation=findViewById(R.id.editTextPasswordConfirmation);


// Sada možete dobiti tekst iz svakog polja
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String username = editTextUsername.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String address = editTextAddress.getText().toString();
                String password=editPassword.getText().toString();
                String confirmationPassword=editPasswordConfirmation.getText().toString();
                UserPutDTO userPut=new UserPutDTO(firstName,lastName,
                        password,address,phoneNumber,user.status,user.reservationRequestNotification,user.reservationCancellationNotification,user.ownerRatingNotification,user.accommodationRatingNotification,
                        user.ownerRatingNotification,user.getToken(),user.getDeleted(),user.getFavouriteAccommodations());
                Call<User> call = userService.updateUser(userPut, TokenManager.getLoggedInUser().username);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(AccountScreenActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(AccountScreenActivity.this, "Cannot update user", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



        binding.toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.navigationView.getVisibility() == View.VISIBLE) {
                    binding.navigationView.setVisibility(View.INVISIBLE);
                } else {
                    binding.navigationView.setAnimation(slideInAnimation);
                    binding.navigationView.setVisibility(View.VISIBLE);
                }


            }
        });

        // Postavljanje animacija na NavigationView pri zatvaranju
        // Postavljanje animacija na NavigationView pri zatvaranju
        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_IDLE) {
                    isDrawerOpen = binding.drawerLayout.isDrawerOpen(binding.navigationView.getRootView());
                    if (!isDrawerOpen) {
                        binding.navigationView.clearAnimation();
                    }
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                MenuItem homeMenuItem = binding.navigationView.getMenu().findItem(R.id.home);
                MenuItem notificationsMenuItem = binding.navigationView.getMenu().findItem(R.id.notifications);
                MenuItem myReservationsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_reservations);
                MenuItem myAccountMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_account);
                MenuItem logOutMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_logout);
                MenuItem createAccommodationMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_create_accommodation);

                if (item.getItemId() == homeMenuItem.getItemId()) {
                    performHomeAction();
                    return true;
                } else if (item.getItemId() == notificationsMenuItem.getItemId()) {
                    performNotificationsAction();
                    return true;
                } else if (item.getItemId() == myReservationsMenuItem.getItemId()) {
                    performMyReservationsAction();
                    return true;
                } else if (item.getItemId() == myAccountMenuItem.getItemId()) {
                    performMyAccountAction();
                    return true;
                } else if (item.getItemId() == logOutMenuItem.getItemId()) {
                    performLogOutAction();
                    return true;
                }else if(item.getItemId()==createAccommodationMenuItem.getItemId()){
                    performCreateAccommodationAction();
                    return true;
                }

                // Zatvori navigacijski izbornik
                binding.drawerLayout.closeDrawer(binding.navigationView);

                return true;
            }
        });
    }

        public void performHomeAction(){
            Intent intent=new Intent(AccountScreenActivity.this,HomeScreenActivity.class);
            startActivity(intent);
        }

        public void performNotificationsAction(){

        }
        public void performMyReservationsAction(){

        }

        public void performMyAccountAction(){
            Intent intent=new Intent(AccountScreenActivity.this,AccountScreenActivity.class);
            startActivity(intent);
            finish();

        }
        public void performLogOutAction(){
            Intent intent=new Intent(AccountScreenActivity.this,HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }

        public void performCreateAccommodationAction(){
        Intent intent=new Intent(AccountScreenActivity.this,CreateAccommodationActivity.class);
        startActivity(intent);
        finish();
    }

    private String getRoleFromToken() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        return preferences.getString("role", "");
    }

    private String getUserIdFromToken() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        return preferences.getString("userId", "");
    }


}