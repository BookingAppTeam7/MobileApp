package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityAccountScreenBinding;
import com.example.bookingapp.databinding.ActivityRegisterScreenBinding;
import com.google.android.material.navigation.NavigationView;

public class AccountScreenActivity extends AppCompatActivity {

    private Animation slideInAnimation;
    private Animation slideOutAnimation;
    private boolean isDrawerOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityAccountScreenBinding binding=ActivityAccountScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);


        DrawerLayout drawerLayout =binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;
        slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);

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




        //getSupportActionBar().setTitle("My Account");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
//        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme
    }


}