package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeScreenBinding binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}