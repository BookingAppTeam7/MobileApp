package com.example.bookingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.adapters.ReviewListAdapter;
import com.example.bookingapp.databinding.ActivityDetailedBinding;
import com.example.bookingapp.fragments.accommodations.AvailabilityBottomSheetDialogFragment;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.TimeSlot;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailedActivity extends AppCompatActivity {
    ActivityDetailedBinding binding;
    private String location;
    private double locationX;
    private double locationY;
    private double price;
    private List<PriceCard> priceList;
    ReviewListAdapter listAdapter;
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
}