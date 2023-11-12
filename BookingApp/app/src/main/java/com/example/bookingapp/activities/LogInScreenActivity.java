package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityLogInScreenBinding;

public class LogInScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLogInScreenBinding binding=ActivityLogInScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Log In Screen");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LogInScreenActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogInScreenActivity.this, RegisterScreenActivity.class);
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