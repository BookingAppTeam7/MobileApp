package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivitySplashScreenBinding;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (isConnectedToNetwork()) {
            int SPLASH_TIME_OUT = 5000;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, CreateAccommodationActivity.class);
                    startActivity(intent);
                    finish();   // Nema povratka, ovo poziva onDestroy() metodu
                }
            }, SPLASH_TIME_OUT);
        } else {
            Toast.makeText(this, "Device is not connected to the internet !", Toast.LENGTH_SHORT).show();

            showConnectToInternetDialog();
        }
    }

        private boolean isConnectedToNetwork() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }
            return false;
    }

    private void showConnectToInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Device is not connected to the internet");
        builder.setMessage("Do you want to connect now?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
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