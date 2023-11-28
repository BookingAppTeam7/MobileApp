package com.example.bookingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityDetailedBinding;

public class DetailedActivity extends AppCompatActivity {
    ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=this.getIntent();
        if(intent!=null){
            String name=intent.getStringExtra("name");
            String description=intent.getStringExtra("description");
            int image=intent.getIntExtra("image",R.drawable.accommodation1);

            binding.detailName.setText(name);
            binding.detailDescription.setText(description);
            binding.detailImage.setImageResource(image);
        }

        binding.googleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}