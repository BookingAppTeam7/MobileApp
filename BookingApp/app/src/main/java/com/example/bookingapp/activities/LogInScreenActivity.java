package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bookingapp.databinding.ActivityLogInScreenBinding;
import com.example.bookingapp.model.JwtAuthenticationRequest;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.User;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInScreenActivity extends AppCompatActivity implements SensorEventListener {
    private static final String KEY_JWT_TOKEN = "jwtToken";

    private SensorManager sensorManager;
    private Sensor lightSensor;

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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (lightSensor != null) {
                // Registracija za osluškivanje promena vrednosti senzora
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Light sensor not available", Toast.LENGTH_SHORT).show();
            }
        }

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(LogInScreenActivity.this, "KLIK", Toast.LENGTH_SHORT).show();
                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                JwtAuthenticationRequest user = new JwtAuthenticationRequest(username, password);

                UserService userService = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
                Call<User> call = userService.loginUser(user);


                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            // Uspešna prijava
                            Toast.makeText(LogInScreenActivity.this, "Successfully logged in  "+response.body().getUsername(), Toast.LENGTH_SHORT).show();
                            saveJwtToken(response.body().getJwt());
                           // TokenManager tokenManager=new TokenManager();
                           // Log.d("token manageeer pa response body " , response.body().getJwt());
                         //   binding.textViewError.setText("Respones body pa njegov token--> " + response.body().getJwt());
                            //tokenManager.setLoggedInUser(response.body());
                            TokenManager.setJwtToken(response.body().getJwt());
                            TokenManager.setLoggedInUser(response.body());
                            //Toast.makeText(LogInScreenActivity.this,"Token je " + response.body().getJwt(),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LogInScreenActivity.this, HomeScreenActivity.class);
                            intent.putExtra("username",response.body().getUsername());
                            intent.putExtra("role",response.body().getRole().toString());
                            startActivity(intent);
                        } else {
                            binding.textViewError.setText("Wrong username or password!");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Greška u komunikaciji s poslužiteljem
                        binding.textViewError.setText("Error...");
                    }
                });
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


//    private void saveJwtToken(String user) {
//        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        editor.putString("userId", user.username);
//        editor.putString("role",user.role.toString());
//        editor.apply();
//    }
    private void saveJwtToken(String jwtToken) {
//        TokenManager tokenManager = new TokenManager();
//        tokenManager.setJwt(jwtToken);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_JWT_TOKEN, jwtToken);
        editor.apply();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
//            float lightValue = event.values[0];
//
//            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//            layoutParams.screenBrightness = lightValue / SensorManager.LIGHT_SUNLIGHT_MAX;
//
//            // Ovde postavljamo promene vrednosti u okviru ekrana
//            getWindow().setAttributes(layoutParams);
//            getWindow().getDecorView().requestLayout();
//        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            Log.d("LightSensor", "Light value: " + lightValue);
            float brightnessIncrement = 0.1f; // Možete prilagoditi vrednost
            float newBrightness = lightValue / SensorManager.LIGHT_SUNLIGHT_MAX + brightnessIncrement;

            // Ako je novi nivo svetlosti van opsega [0.0, 1.0], postavite ga na granice
            newBrightness = Math.max(0.0f, Math.min(1.0f, newBrightness));

            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.screenBrightness = newBrightness;

            // Postavljanje promenjenih vrednosti u okviru ekrana
            getWindow().setAttributes(layoutParams);
            updateColorsBasedOnBrightness(newBrightness);
        }
    }

    private void updateColorsBasedOnBrightness(float brightness) {
        // Prilagodite ove boje prema vašim potrebama
        int whiteColor = getResources().getColor(android.R.color.white);
        int blackColor = getResources().getColor(android.R.color.black);

        ActivityLogInScreenBinding binding = ActivityLogInScreenBinding.inflate(getLayoutInflater());

        // Postavljanje boje dugmadi
        if (brightness < 0.5f) {
            binding.loginButton.setBackgroundColor(whiteColor);
            binding.registerButton.setBackgroundColor(whiteColor);
        } else {
            binding.loginButton.setBackgroundColor(blackColor);
            binding.registerButton.setBackgroundColor(blackColor);
        }

        // Postavljanje boje input polja
        if (brightness < 0.5f) {
            binding.editTextUsername.setBackgroundColor(blackColor);
            binding.editTextPassword.setBackgroundColor(blackColor);
        } else {
            binding.editTextUsername.setBackgroundColor(whiteColor);
            binding.editTextPassword.setBackgroundColor(whiteColor);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        // kad je aktivnost u pozadini , zaustavimo senzor
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
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
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}