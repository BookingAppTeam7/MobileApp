package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivitySplashScreenBinding;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySplashScreenBinding binding= ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int SPLASH_TIME_OUT = 2000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LogInScreenActivity.class);
                startActivity(intent);
                finish();   //nema povratka,ovo poziva onDestroy() metodu
            }
        }, SPLASH_TIME_OUT);
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