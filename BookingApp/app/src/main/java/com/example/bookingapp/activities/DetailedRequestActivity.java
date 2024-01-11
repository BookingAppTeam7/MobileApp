package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityDetailedRequest2Binding;

import com.example.bookingapp.fragments.accommodations.PriceCardFragment;
import com.example.bookingapp.fragments.accommodations.PriceCardRequestFragment;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationRequest;
import com.example.bookingapp.model.DTOs.PriceCardStringDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.enums.AccommodationRequestStatus;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationRequestService;
import com.example.bookingapp.services.AccommodationService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailedRequestActivity extends AppCompatActivity {

    public static ActivityDetailedRequest2Binding binding;

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public AccommodationRequestService requestService = retrofit.create(AccommodationRequestService.class);

    public Long requestId;

    public List<PriceCard>prices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDetailedRequest2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();


        TextView minGuestsTextView =findViewById(R.id.minGuestsLabel);
        TextView maxGuestsTextView = findViewById(R.id.maxGuestsLabel);
        TextView deadlineTextView = findViewById(R.id.cancellationDeadlineLabel);
        TextView locationTextView=findViewById(R.id.locationLabel);
        TextView typeTextView=findViewById(R.id.typeLabel);
        TextView reservationConfirmationTextView=findViewById(R.id.reservationConfirmationLabel);


        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String image = intent.getStringExtra("image");
        String location = intent.getStringExtra("location");
        ArrayList<String> assets = intent.getStringArrayListExtra("assets");
        int minGuests = intent.getIntExtra("minGuests", 0);
        int maxGuests = intent.getIntExtra("maxGuests", 0);
        int cancellationDeadline = intent.getIntExtra("cancellationDeadline", 0);
        String type=intent.getStringExtra("typeOfAccommodation");
        String reservationConfirmation=intent.getStringExtra("reservationConfirmation");

        Long originalAccommodationId=intent.getLongExtra("originalAccommodation",0L);
        Long unapprovedAccommodationId=intent.getLongExtra("unapprovedAccommodation",0L);
        Long pricesId=intent.getLongExtra("pricesId",0L);
        DetailedRequestActivity.this.requestId=intent.getLongExtra("requestId",0L);

        minGuestsTextView.setText("Min Guests: " + minGuests);
        maxGuestsTextView.setText("Max Guests: " + maxGuests);
        deadlineTextView.setText("Cancellation deadline : "+cancellationDeadline);
        locationTextView.setText("Location : "+location);
        typeTextView.setText("Type : "+type);
        reservationConfirmationTextView.setText("Reservation confirmation : "+reservationConfirmation);

        TextView nameTextView = findViewById(R.id.detailName);
        nameTextView.setText("Name : "+name);

        Button showPricesButton=binding.availability;
        Button approveRequest=binding.approveButton;
        Button rejectRequest=binding.rejectButton;

        Call<Accommodation> call = accommodationService.findById(pricesId);

        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.isSuccessful()) {
                    DetailedRequestActivity.this.prices= response.body().prices;

                }
            }
            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                // Obrada greške prilikom poziva
                t.printStackTrace();
            }
        });

        showPricesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DetailedRequestActivity.this,EditPriceCardsActivity.class);
                intent.putExtra("accommodationId",unapprovedAccommodationId);
                intent.putExtra("prices", (Serializable) DetailedRequestActivity.this.prices);
                startActivity(intent);
//                PriceCardRequestFragment priceCardFragment = new PriceCardRequestFragment(DetailedRequestActivity.this.prices);
//
//                getSupportFragmentManager().beginTransaction()
//                        .replace(android.R.id.content, priceCardFragment)
//                        .addToBackStack(null)
//                        .commit();

            }
        });

        approveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                approveRequest();
            }
        });

        rejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectRequest();
            }
        });





    }


    private void approveRequest() {

        Call<AccommodationRequest> call = requestService.updateStatus(requestId, AccommodationRequestStatus.APPROVED);
        call.enqueue(new Callback<AccommodationRequest>() {
            @Override
            public void onResponse(Call<AccommodationRequest> call, Response<AccommodationRequest> response) {
                if (response.isSuccessful()) {
                    AccommodationRequest result = response.body();
                    Toast.makeText(DetailedRequestActivity.this,"Sucessfully approved!",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AccommodationRequest> call, Throwable t) {
                // Ovde obradite grešku u slučaju neuspjeha poziva
            }
        });
    }

    private void rejectRequest() {

        Call<AccommodationRequest> call = requestService.updateStatus(requestId, AccommodationRequestStatus.REJECTED);
        call.enqueue(new Callback<AccommodationRequest>() {
            @Override
            public void onResponse(Call<AccommodationRequest> call, Response<AccommodationRequest> response) {
                if (response.isSuccessful()) {
                    AccommodationRequest result = response.body();
                    Toast.makeText(DetailedRequestActivity.this,"Request rejected!",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AccommodationRequest> call, Throwable t) {
                // Ovde obradite grešku u slučaju neuspjeha poziva
            }
        });
    }
}