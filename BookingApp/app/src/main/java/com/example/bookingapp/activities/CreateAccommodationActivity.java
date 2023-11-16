package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityCreateAccommodationBinding;
import com.google.android.material.navigation.NavigationView;

public class CreateAccommodationActivity extends AppCompatActivity {

    private Animation slideInAnimation;
    private Animation slideOutAnimation;

    private boolean isDrawerOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ActivityCreateAccommodationBinding binding=ActivityCreateAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawerLayout = binding.drawerLayout;
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
                MenuItem createAccommodationItem = binding.navigationView.getMenu().findItem(R.id.menu_create_accommodation);
                MenuItem logOutItem = binding.navigationView.getMenu().findItem(R.id.menu_logout);

                if (item.getItemId() == homeMenuItem.getItemId()) {
                    performHomeAction();
                    return true;
                }
                else if(item.getItemId()==createAccommodationItem.getItemId()){
                    performCreateAccommodationAction();
                }
                else if(item.getItemId()==logOutItem.getItemId()){
                    performLogOutAction();
                }
                // Zatvori navigacijski izbornik
                binding.drawerLayout.closeDrawer(binding.navigationView);

                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    public void performLogOutAction(){
        Intent intent = new Intent(CreateAccommodationActivity.this, HomeScreenActivity.class);
        startActivity(intent);
    }
    public void performHomeAction(){
        Intent intent = new Intent(CreateAccommodationActivity.this, HomeScreenActivity.class);
        startActivity(intent);
    }
    public void performCreateAccommodationAction(){
        Intent intent=new Intent(CreateAccommodationActivity.this,CreateAccommodationActivity.class);
        finish();
    }



}