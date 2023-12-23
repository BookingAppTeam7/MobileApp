package com.example.bookingapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.bookingapp.databinding.ActivityEditAccommodationsDataBinding;
import com.example.bookingapp.fragments.accommodations.AvailabilityFragment;
import com.example.bookingapp.fragments.accommodations.PriceCardFragment;

import java.util.Calendar;

public class EditAccommodationsDataActivity extends AppCompatActivity {

    private EditText editTextStartDate;
    private EditText editTextEndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditAccommodationsDataBinding binding = ActivityEditAccommodationsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button buttonAddPrice = binding.buttonAddPrice;
        Button buttonAddAvailability=binding.buttonAccommodationAvailability;
        buttonAddPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPriceDialog();
            }
        });
        buttonAddAvailability.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showAddAvailabilityDialog();
            }
        });
    }
    private void showAddPriceDialog() {
//        PriceCardFragment priceCardFragment = new PriceCardFragment();
//        priceCardFragment.show(getFragmentManager(), priceCardFragment.getTag());
    }
    private void showAddAvailabilityDialog(){
        AvailabilityFragment availabilityFragment=new AvailabilityFragment();
        availabilityFragment.show(getFragmentManager(), availabilityFragment.getTag());

    }

}