package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bookingapp.databinding.ActivityLogInScreenBinding;
import com.example.bookingapp.model.JwtAuthenticationRequest;
import com.example.bookingapp.model.User;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                            saveJwtToken(response.body());
                            Intent intent = new Intent(LogInScreenActivity.this, HomeScreenActivity.class);
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


    private void saveJwtToken(User user) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", user.username);
        editor.putString("role",user.role.toString());
        editor.apply();
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