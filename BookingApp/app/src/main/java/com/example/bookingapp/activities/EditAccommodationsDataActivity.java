package com.example.bookingapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityEditAccommodationsDataBinding;
import com.example.bookingapp.fragments.accommodations.AvailabilityFragment;
import com.example.bookingapp.fragments.accommodations.PriceCardFragment;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.PriceCardPostDTO;
import com.example.bookingapp.model.DTOs.TimeSlotPostDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.ReservationConfirmationEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.PriceCardService;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.io.Serializable;

import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditAccommodationsDataActivity extends AppCompatActivity {

    public List<PriceCardPostDTO> prices=new ArrayList<>();
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public PriceCardService priceCardService=retrofit.create(PriceCardService.class);

    Date startDateObject;
    Date endDateObject;

    public Accommodation accommodation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditAccommodationsDataBinding binding = ActivityEditAccommodationsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Accommodation");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme



        Long accommodationId = getIntent().getLongExtra("accommodationId", -1);

        Call<Accommodation> call = accommodationService.findById(accommodationId);

        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.isSuccessful()) {
                    EditAccommodationsDataActivity.this.accommodation = response.body();

                    EditText editTextName = findViewById(R.id.editTextName);
                    editTextName.setText(accommodation.name);
                    EditText editTextAddress = findViewById(R.id.editTextLocation);
                    editTextAddress.setText(accommodation.location.address);
                    EditText editTextCountry = findViewById(R.id.editTextCountry);
                    editTextCountry.setText(accommodation.location.country);
                    EditText editTextCity = findViewById(R.id.editTextCity);
                    editTextCity.setText(accommodation.location.city);
                    EditText editTextMinGuests = findViewById(R.id.editTextMinNumOfGusets);
                    editTextMinGuests.setText(String.valueOf(accommodation.minGuests));
                    EditText editTextMaxGuests = findViewById(R.id.editTextMaxNumOfGusets);
                    editTextMaxGuests.setText(String.valueOf(accommodation.maxGuests));

                    EditText editTextDescription = findViewById(R.id.editTextDescription);
                    editTextDescription.setText(accommodation.description);

                    EditText editTextCancellationDeadline = findViewById(R.id.editTextCancellationDeadLine);
                    editTextCancellationDeadline.setText(String.valueOf(accommodation.cancellationDeadline));

                    RadioGroup radioGroupReservationConfirmation = findViewById(R.id.radioGroupReservationConfirmation);

                    RadioButton radioButtonManual = findViewById(R.id.radioButtonManualConfirmation);
                    RadioButton radioButtonAutomatic = findViewById(R.id.radioButtonAutomaticConfirmation);

                    if (accommodation.getReservationConfirmation() == ReservationConfirmationEnum.MANUAL) {
                        radioButtonManual.setChecked(true);
                    } else {
                        radioButtonAutomatic.setChecked(true);
                    }

                    RadioGroup radioGroupType = findViewById(R.id.radioGroupAccommodationType);

                    RadioButton radioButtonRoom = findViewById(R.id.radioButtonRoom);
                    RadioButton radioButtonApartment = findViewById(R.id.radioButtonApartment);

                    if (accommodation.getType() == TypeEnum.ROOM) {
                        radioButtonRoom.setChecked(true);
                    } else {
                        radioButtonApartment.setChecked(true);
                    }

                    List<String> amenities = accommodation.assets;

                    LinearLayout amenitiesCheckListLeft = findViewById(R.id.amenitiesCheckListLeft);
                    LinearLayout amenitiesCheckListRight = findViewById(R.id.amenitiesCheckListRight);

                    for (String amenity : amenities) {
                        switch (amenity) {
                            case "WiFi":
                                CheckBox checkBoxWiFi = amenitiesCheckListLeft.findViewById(R.id.checkBoxWiFi);
                                checkBoxWiFi.setChecked(true);
                                break;
                            case "Free Parking":
                                CheckBox checkBoxParking = amenitiesCheckListLeft.findViewById(R.id.checkBoxParking);
                                checkBoxParking.setChecked(true);
                                break;
                            case "Air Conditioning":
                                CheckBox checkBoxAirConditioning = amenitiesCheckListRight.findViewById(R.id.checkBoxAirConditioning);
                                checkBoxAirConditioning.setChecked(true);
                                break;
                            case "Kitchen":
                                CheckBox checkBoxKitchen = amenitiesCheckListRight.findViewById(R.id.checkBoxKitchen);
                                checkBoxKitchen.setChecked(true);
                                break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
            }
        });

        Button buttonAddTimeSlot=binding.buttonAddTimeSlot;
        Button buttonAddPriceCard=binding.buttonAddPriceCard;
        ImageButton buttonEditPrices=binding.showPricesButton;

        TextView selectedDate=binding.selectedDate;

        EditText newPrice=binding.editTextPrice;

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

        buttonEditPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAccommodationsDataActivity.this, EditPriceCardsActivity.class);

                List<PriceCard> prices = EditAccommodationsDataActivity.this.accommodation.prices;

                intent.putExtra("prices", (Serializable) prices);

                startActivity(intent);
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

        TimeSlotPostDTO newTimeSlot=new TimeSlotPostDTO(startDateObject,endDateObject);
        PriceCardPostDTO newPriceCard=new PriceCardPostDTO(newTimeSlot,price,priceType);
        this.prices.add(newPriceCard);
        //treba setovati accommodation_id
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


            startDateObject = new Date(startDateString);
            endDateObject = new Date(endDateString);

            // Displaying the selected date range in the TextView
            selectedDate.setText(selectedDateRange);
        });

        // Showing the date picker dialog
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

}