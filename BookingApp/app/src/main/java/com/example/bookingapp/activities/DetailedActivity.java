package com.example.bookingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.adapters.ReviewListAdapter;
import com.example.bookingapp.databinding.ActivityDetailedBinding;
import com.example.bookingapp.fragments.accommodations.AvailabilityBottomSheetDialogFragment;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.fragments.accommodations.ReservationBottomSheetFragment;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.ReservationPostDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.TimeSlot;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.ReservationService;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailedActivity extends AppCompatActivity implements BottomSheetListener {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public ReservationService reservationService=retrofit.create(ReservationService.class);
    ActivityDetailedBinding binding;
    private String location;
    private double locationX;
    private double locationY;
    private double price;
    private List<PriceCard> priceList;
    ReviewListAdapter listAdapter;
    String loggedInUsername;
    String loggedInRole;
    public Long accommodationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button reservation=binding.reservation;

        Intent intent=this.getIntent();
        if(intent!=null){
            loggedInUsername=intent.getStringExtra("username");
            loggedInRole=intent.getStringExtra("role");
            accommodationId=intent.getLongExtra("accommodationId",0);
            String name=intent.getStringExtra("name");
            String description=intent.getStringExtra("description");
            int image=intent.getIntExtra("image",R.drawable.accommodation1);
            location=intent.getStringExtra("location");
            locationX=intent.getDoubleExtra("locationX",0.0);
            locationY=intent.getDoubleExtra("locationY",0.0);
            price=intent.getDoubleExtra("price",0.0);
            binding.detailName.setText(name);
            binding.detailDescription.setText(description);
            binding.detailImage.setImageResource(image);
            List<Review> reviewsList = (ArrayList<Review>) getIntent().getSerializableExtra("reviewsList");
            List<String> assetsList=(ArrayList<String>) getIntent().getSerializableExtra("assets");
            String allAssets=" ";
            for(String s:assetsList){
                allAssets+=s;
                allAssets+=",";
            }
            allAssets=allAssets.substring(0,allAssets.length()-1);
            binding.assets.setText("Assets:"+allAssets);

            priceList = (List<PriceCard>) getIntent().getSerializableExtra("priceList");
            if(loggedInRole!=null){
                if(loggedInRole.equals("GUEST"))
                    reservation.setVisibility(View.VISIBLE);
                else
                    reservation.setVisibility(View.INVISIBLE);
            }else{
                reservation.setVisibility(View.INVISIBLE);
            }


            listAdapter = new ReviewListAdapter(DetailedActivity.this, reviewsList);
            binding.listReviewView.setAdapter(listAdapter);
            binding.listReviewView.setClickable(false);
        }

        binding.googleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, MapsActivity.class);
                intent.putExtra("location",location);
                intent.putExtra("locationX",locationX);
                intent.putExtra("locationY",locationY);
                startActivity(intent);
            }
        });

        binding.availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvailabilityBottomSheetDialogFragment bottomSheetFragment =
                        AvailabilityBottomSheetDialogFragment.newInstance(DatesToListOfStrings(priceList));
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
        binding.reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationBottomSheetFragment reservationBottomSheetFragment=
                        ReservationBottomSheetFragment.newInstance();
                reservationBottomSheetFragment.show(getSupportFragmentManager(),reservationBottomSheetFragment.getTag());
            }
        });
    }
    public String DatesToListOfStrings(List<PriceCard> priceList){
        StringBuilder result = new StringBuilder();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault());

        for (PriceCard p:priceList) {  //koristi se Date kao i na backendu
            String formattedStartDate = dateFormat.format(Date.from(p.getTimeSlot().getStartDate().toInstant()));
//
            String formattedEndDate =  dateFormat.format(Date.from(p.getTimeSlot().getEndDate().toInstant()));
//
            result.append(formattedStartDate)
                    .append(" - ")
                    .append(formattedEndDate)
                    .append("\n")
                    .append("Price:")
                    .append(p.price+"$ ")
                    .append(p.type);
        }

        return result.toString();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSearchButtonClicked(String place, int guests, String arrivalDate, String checkoutDate) {

    }

    @Override
    public void onFilterButtonClicked(TypeEnum selectedType, String joined, String minTotalPrice, String maxTotalPrice) {

    }

    @Override
    public void onReservationButtonClicked(int guests, String arrivalDate, String checkoutDate) {
        //samo ovo jos
        Log.e("ARRIVAL",arrivalDate);
        Log.e("CHECKOUT",checkoutDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date arrival;
        Date checkout;

        try {
            arrival = dateFormat.parse(arrivalDate + "T00:00:00");
            checkout = dateFormat.parse(checkoutDate + "T00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
            return; // Handle the parsing error as needed
        }
        Log.e("ARRIVAL",arrival.toString());
        Log.e("CHECKOUT",checkout.toString());
        Log.e("Formatted Arrival", dateFormat.format(arrival));
        Log.e("Formatted Checkout", dateFormat.format(checkout));
        Log.e("GUESTS",String.valueOf(guests));

        String formattedArrivalDate = dateFormat.format(arrival);
        String formattedCheckoutDate = dateFormat.format(checkout);

        // Manually create a JSON object with the desired format
        String requestBody = String.format(
                "{\"accommodationId\": %d, \"userId\": \"%s\", \"timeSlot\": {\"id\": %d, \"startDate\": \"%s\", \"endDate\": \"%s\"}, \"numberOfGuests\": %d}",
                accommodationId, loggedInUsername, 99L, formattedArrivalDate, formattedCheckoutDate, guests);

        // Make the API call with the manually created JSON object
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);
        Call<Reservation> call = reservationService.createReservation(body);

        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful()) {
                    Log.e("USPESNO NAPRAVIO REZERVACIJU","POGLEDAJ BAZU");
                    Toast.makeText(DetailedActivity.this,"Successfully created reservation!",Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Reservation Error", "Error: " + response.message());
                    String errorMessage = "Unknown error";
                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        if (errorBody.has("error")) {
                            errorMessage = errorBody.getString("error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Show a Toast with the error message
                    Toast.makeText(DetailedActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                // Handle failure
                Log.e("Reservation Failure", "Error: " + t.getMessage());
            }
        });
    }
}
