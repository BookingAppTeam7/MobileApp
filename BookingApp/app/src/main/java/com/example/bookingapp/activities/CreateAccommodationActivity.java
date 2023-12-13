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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityCreateAccommodationBinding;
import com.example.bookingapp.fragments.accommodations.AvailabilityFragment;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.fragments.accommodations.PriceCardFragment;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.AccommodationPostDTO;
import com.example.bookingapp.model.DTOs.LocationPostDTO;
import com.example.bookingapp.model.enums.ReservationConfirmationEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        Button buttonCreate = binding.buttonCreate;

        buttonAddPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPriceDialog();
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CreateAccommodation", "Button clicked");
                getDataAndCreate();
            }
        });

    }

    private void getDataAndCreate() {
        String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        String address = ((EditText) findViewById(R.id.editTextLocation)).getText().toString();
        String city = ((EditText) findViewById(R.id.editTextCity)).getText().toString();
        String country = ((EditText) findViewById(R.id.editTextCountry)).getText().toString();
        String minNumOfGuests = ((EditText) findViewById(R.id.editTextMinNumOfGusets)).getText().toString();
        String maxNumOfGuests = ((EditText) findViewById(R.id.editTextMaxNumOfGusets)).getText().toString();
        String cancellationDeadline = ((EditText) findViewById(R.id.editTextCancellationDeadLine)).getText().toString();
        String description = ((EditText) findViewById(R.id.editTextDescription)).getText().toString();
        TypeEnum type=TypeEnum.APARTMENT;
        ReservationConfirmationEnum reservationConfirmation=ReservationConfirmationEnum.MANUAL;

        //OWNER ID dobaviti iz tokena
        String ownerId="tamara@gmail.com";

        RadioGroup radioGroupAccommodationType = findViewById(R.id.radioGroupAccommodationType);
        int selectedRadioButtonId = radioGroupAccommodationType.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedAccommodationType = selectedRadioButton.getText().toString();
            if(selectedAccommodationType=="ROOM"){
                type=TypeEnum.ROOM;
            }
            else if(selectedAccommodationType=="VIP_ROOM"){
                type=TypeEnum.VIP_ROOM;
            }
            System.out.println("Odabrani tip smještaja: " + selectedAccommodationType);
        } else {
            System.out.println("Nijedan tip smještaja nije odabran");
        }

        List<String> assets=new ArrayList<>();

        LinearLayout amenitiesCheckList = findViewById(R.id.amenitiesCheckList);

        LinearLayout amenitiesCheckListLeft = findViewById(R.id.amenitiesCheckListLeft);
        LinearLayout amenitiesCheckListRight = findViewById(R.id.amenitiesCheckListRight);

        CheckBox checkBoxWiFi = amenitiesCheckListLeft.findViewById(R.id.checkBoxWiFi);
        CheckBox checkBoxParking = amenitiesCheckListLeft.findViewById(R.id.checkBoxParking);
        CheckBox checkBoxAirConditioning = amenitiesCheckListRight.findViewById(R.id.checkBoxAirConditioning);
        CheckBox checkBoxKitchen = amenitiesCheckListRight.findViewById(R.id.checkBoxKitchen);

        boolean isWiFiChecked = checkBoxWiFi.isChecked();
        if(isWiFiChecked){
            assets.add("Free Wi-Fi");
        }
        boolean isParkingChecked = checkBoxParking.isChecked();
        if(isParkingChecked){
            assets.add("Free Parking");
        }
        boolean isAirConditioningChecked = checkBoxAirConditioning.isChecked();
        if(isAirConditioningChecked){
            assets.add("Air conditioner");
        }
        boolean isKitchenChecked = checkBoxKitchen.isChecked();
        if(isKitchenChecked){
            assets.add("Kitchen");
        }

        AccommodationPostDTO newAccommodation = new AccommodationPostDTO();
        newAccommodation.name = name;
        newAccommodation.location = new LocationPostDTO(address,city,country,0.0,0.0);
        newAccommodation.description=description;
        newAccommodation.minGuests=Integer.parseInt(minNumOfGuests);
        newAccommodation.maxGuests=Integer.parseInt(maxNumOfGuests);
        newAccommodation.type=type;
        newAccommodation.ownerId=ownerId;
        newAccommodation.cancellationDeadline=Integer.parseInt(cancellationDeadline);
        newAccommodation.images=new ArrayList<>();
        newAccommodation.assets=assets;

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        AccommodationService accommodationService = retrofit.create(AccommodationService.class);
        Call<Accommodation> call = accommodationService.create(newAccommodation);
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.isSuccessful()) {
                    Accommodation createdAccommodation = response.body();
                    Toast.makeText(CreateAccommodationActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    // Ovde rukujte sa uspešnim odgovorom
                } else {
                    // Ovde rukujte sa neuspešnim odgovorom
                    Toast.makeText(CreateAccommodationActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                // Ovde rukujte sa greškom
                Log.e("Retrofit", "Error:", t);
                Toast.makeText(CreateAccommodationActivity.this, "Greskaaa!", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void showAddPriceDialog() {
        PriceCardFragment priceCardFragment = new PriceCardFragment();
        priceCardFragment.show(getFragmentManager(), priceCardFragment.getTag());
    }



}