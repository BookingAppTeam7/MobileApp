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
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityEditAccommodationsDataBinding;
import com.example.bookingapp.fragments.accommodations.AvailabilityFragment;
import com.example.bookingapp.fragments.accommodations.PriceCardFragment;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.AccommodationPostDTO;
import com.example.bookingapp.model.DTOs.AccommodationPutDTO;
import com.example.bookingapp.model.DTOs.LocationPostDTO;
import com.example.bookingapp.model.DTOs.LocationPutDTO;
import com.example.bookingapp.model.DTOs.PriceCardPostDTO;
import com.example.bookingapp.model.DTOs.PriceCardStringDTO;
import com.example.bookingapp.model.DTOs.TimeSlotPostDTO;
import com.example.bookingapp.model.DTOs.TimeSlotStringDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.TokenManager;
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

    public List<PriceCard> prices=new ArrayList<>();
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public PriceCardService priceCardService=retrofit.create(PriceCardService.class);

    String startDateObject;
    String endDateObject;

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
        Button saveChanges=binding.buttonCreate;

        TextView selectedDate=binding.selectedDate;

        EditText newPrice=binding.editTextPrice;

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataAndUpdate();
            }
        });

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
                intent.putExtra("accommodationId", accommodationId);

                startActivity(intent);
            }
        });
    }

    private void getDataAndUpdate() {

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

        RadioGroup radioGroupReservationConfirmation = findViewById(R.id.radioGroupReservationConfirmation);
        selectedRadioButtonId = radioGroupReservationConfirmation.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedConfirmationType = selectedRadioButton.getText().toString();
            if(selectedConfirmationType=="MANUAL"){
                reservationConfirmation=ReservationConfirmationEnum.MANUAL;
            }
            else {
                reservationConfirmation=ReservationConfirmationEnum.AUTOMATIC;
            }
        } else {
            System.out.println("Nijedan tip potvrde rezervacije nije odabran");
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

        AccommodationPutDTO updatedAccommodation = new AccommodationPutDTO();
        updatedAccommodation.name = name;
        updatedAccommodation.location = new LocationPutDTO(address,city,country,0.0,0.0);
        updatedAccommodation.description=description;
        updatedAccommodation.minGuests=Integer.parseInt(minNumOfGuests);
        updatedAccommodation.maxGuests=Integer.parseInt(maxNumOfGuests);
        updatedAccommodation.type=type;
        updatedAccommodation.ownerId= TokenManager.getLoggedInUser().username;
        updatedAccommodation.cancellationDeadline=Integer.parseInt(cancellationDeadline);
        updatedAccommodation.images=new ArrayList<>();
        updatedAccommodation.assets=assets;
        updatedAccommodation.setReservationConfirmation(reservationConfirmation);

        Call<Accommodation> call = this.accommodationService.update(updatedAccommodation,EditAccommodationsDataActivity.this.accommodation.id);
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(EditAccommodationsDataActivity.this, "Successfully created accommodation!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditAccommodationsDataActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.e("Retrofit", "Error:", t);
                Toast.makeText(EditAccommodationsDataActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
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

        TimeSlotStringDTO newTimeSlot=new TimeSlotStringDTO(startDateObject,endDateObject);
        PriceCardStringDTO newPriceCard=new PriceCardStringDTO(newTimeSlot,price,priceType);
        newPriceCard.setAccommodationId(accommodation.id);


        Call<PriceCard> call = this.priceCardService.create(newPriceCard);
        call.enqueue(new Callback<PriceCard>() {
            @Override
            public void onResponse(Call<PriceCard> call, Response<PriceCard> response) {
                if (response.isSuccessful()) {
                    PriceCard createdPriceCard = response.body();
                    EditAccommodationsDataActivity.this.prices.add(createdPriceCard);
                    prices = EditAccommodationsDataActivity.this.accommodation.prices;
                    Toast.makeText(EditAccommodationsDataActivity.this, "Successfully created Price Card!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditAccommodationsDataActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PriceCard> call, Throwable t) {
                Log.e("Retrofit", "Error:", t);
                Toast.makeText(EditAccommodationsDataActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
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