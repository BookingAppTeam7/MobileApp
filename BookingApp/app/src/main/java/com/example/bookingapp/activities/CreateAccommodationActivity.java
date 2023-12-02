package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityCreateAccommodationBinding;
import com.example.bookingapp.fragments.accommodations.AvailabilityFragment;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.fragments.accommodations.PriceCardFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CreateAccommodationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreateAccommodationBinding binding = ActivityCreateAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create Accommodation");



        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme

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
        PriceCardFragment priceCardFragment = new PriceCardFragment();
        priceCardFragment.show(getFragmentManager(), priceCardFragment.getTag());
    }

    private void showAddAvailabilityDialog(){
        AvailabilityFragment availabilityFragment=new AvailabilityFragment();
        availabilityFragment.show(getFragmentManager(), availabilityFragment.getTag());

    }


}