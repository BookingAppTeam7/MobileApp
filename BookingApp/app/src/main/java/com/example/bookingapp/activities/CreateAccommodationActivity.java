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
import com.example.bookingapp.model.DTOs.PriceCardPostDTO;
import com.example.bookingapp.model.DTOs.TimeSlotPostDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.TimeSlot;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.ReservationConfirmationEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.PriceCardService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateAccommodationActivity extends AppCompatActivity implements PriceCardFragment.PriceCardListener {

    public List<PriceCardPostDTO>prices=new ArrayList<>();
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public PriceCardService priceCardService=retrofit.create(PriceCardService.class);
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

    @Override
    public void onPriceCardSaved(Date startDate, Date endDate, PriceTypeEnum priceType,double price) {
        TimeSlotPostDTO newTimeSlot=new TimeSlotPostDTO(startDate,endDate);
        PriceCardPostDTO newPriceCard=new PriceCardPostDTO(newTimeSlot,price,priceType);
        this.prices.add(newPriceCard);
        //treba setovati accommodation_id
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

        Call<Accommodation> call = this.accommodationService.create(newAccommodation);
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.isSuccessful()) {
                    Accommodation createdAccommodation = response.body();
                    for(PriceCardPostDTO p: prices){
                        p.setAccommodationId(createdAccommodation.id);
                        createPriceCard(p);
                    }
                    Toast.makeText(CreateAccommodationActivity.this, "Successfully created accommodation!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccommodationActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.e("Retrofit", "Error:", t);
                Toast.makeText(CreateAccommodationActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void createPriceCard(PriceCardPostDTO newPriceCard){

        Call<PriceCard> call = this.priceCardService.create(newPriceCard);
        Long accommodationId=newPriceCard.accommodationId;
        call.enqueue(new Callback<PriceCard>() {
            @Override
            public void onResponse(Call<PriceCard> call, Response<PriceCard> response) {
                if (response.isSuccessful()) {
                    PriceCard createdPriceCard = response.body();
                    Toast.makeText(CreateAccommodationActivity.this, "Successfully created Price Card!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccommodationActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PriceCard> call, Throwable t) {
                Log.e("Retrofit", "Error:", t);
                Toast.makeText(CreateAccommodationActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
            }
        });

        }

    private void showAddPriceDialog() {
        PriceCardFragment priceCardFragment = new PriceCardFragment();
        priceCardFragment.show(getFragmentManager(), priceCardFragment.getTag());
    }



}