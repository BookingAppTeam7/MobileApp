package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.example.bookingapp.model.DTOs.PriceCardStringDTO;
import com.example.bookingapp.model.DTOs.TimeSlotPostDTO;
import com.example.bookingapp.model.DTOs.TimeSlotStringDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.TimeSlot;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.ReservationConfirmationEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.PriceCardService;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
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

public class CreateAccommodationActivity extends AppCompatActivity {

    public List<PriceCardStringDTO>prices=new ArrayList<>();
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public PriceCardService priceCardService=retrofit.create(PriceCardService.class);

    String startDateObject;
    String endDateObject;

    Accommodation createdAccommodation;

    public String ownerId;
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

        Button buttonAddTimeSlot = binding.buttonAddTimeSlot;
        Button buttonCreate = binding.buttonCreate;
        Button buttonAddPriceCard=binding.buttonAddPriceCard;
        ImageButton buttonShowPrices=binding.showPricesButton;

        TextView selectedDate=binding.selectedDate;

        EditText newPrice=binding.editTextPrice;

        this.ownerId= TokenManager.getLoggedInUser().username;

        buttonAddTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTimeSlotDialog(selectedDate);
            }
        });

        buttonAddPriceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePriceCard();
            }
        });


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CreateAccommodation", "Button clicked");
                getDataAndCreate();
            }
        });

        buttonShowPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriceCardFragment priceCardFragment = new PriceCardFragment(CreateAccommodationActivity.this.prices);

                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, priceCardFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }


    public void savePriceCard() {

        int price=Integer.parseInt(((EditText) findViewById(R.id.editTextPrice)).getText().toString());
        PriceTypeEnum priceType=PriceTypeEnum.PERUNIT;

        RadioGroup radioGroupPriceType = findViewById(R.id.radioGroupPriceType);
        int selectedRadioButtonId = radioGroupPriceType.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedPriceType = selectedRadioButton.getText().toString();
            if(selectedPriceType.equals("GUEST")){
                priceType=PriceTypeEnum.PERGUEST;
            }

        }

        TimeSlotStringDTO newTimeSlot=new TimeSlotStringDTO(this.startDateObject,this.endDateObject);
        PriceCardStringDTO newPriceCard=new PriceCardStringDTO(newTimeSlot,price,priceType);
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
                    CreateAccommodationActivity.this.createdAccommodation = response.body();

                    for(PriceCardStringDTO p: CreateAccommodationActivity.this.prices){
                        p.setAccommodationId(CreateAccommodationActivity.this.createdAccommodation.id);
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

    public void createPriceCard(PriceCardStringDTO newPriceCard){

        Call<PriceCard> call = this.priceCardService.create(newPriceCard);
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

    private void showAddTimeSlotDialog(TextView selectedDate) {

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");

        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {

            // Retrieving the selected start and end dates
            Long startDate = selection.first;
            Long endDate = selection.second;

            // Formating the selected dates as strings
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String startDateString = sdf.format(new Date(startDate));
            String endDateString = sdf.format(new Date(endDate));

            // Creating the date range string
            String selectedDateRange = startDateString + " - " + endDateString;


            startDateObject = startDateString;
            endDateObject = endDateString;

            // Displaying the selected date range in the TextView
            selectedDate.setText(selectedDateRange);
        });

        // Showing the date picker dialog
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }
}
