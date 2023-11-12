package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityRegisterScreenBinding;

public class RegisterScreenActivity extends AppCompatActivity {

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