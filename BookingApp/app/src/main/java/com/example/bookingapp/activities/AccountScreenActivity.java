package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityAccountScreenBinding;
import com.example.bookingapp.databinding.ActivityRegisterScreenBinding;

public class AccountScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAccountScreenBinding binding=ActivityAccountScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        //getSupportActionBar().setTitle("My Account");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
//        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme
    }
}