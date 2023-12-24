package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.adapters.PriceCardListAdapter;
import com.example.bookingapp.databinding.ActivityEditPriceCardsBinding;
import com.example.bookingapp.databinding.ActivityOwnersAccommodationBinding;
import com.example.bookingapp.model.PriceCard;

import java.util.List;

public class EditPriceCardsActivity extends AppCompatActivity {

    ActivityEditPriceCardsBinding binding;
    List<PriceCard> prices;

    PriceCardListAdapter priceCardListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPriceCardsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit PriceCards");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme


        Intent intent = getIntent();
        if (intent != null) {
            this.prices = (List<PriceCard>) intent.getSerializableExtra("prices");

        }
        priceCardListAdapter = new PriceCardListAdapter(EditPriceCardsActivity.this, prices);
        ListView listView = binding.listview;
        listView.setAdapter(priceCardListAdapter);


    }
}