package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;
import com.example.bookingapp.databinding.ActivityOwnersAccommodationBinding;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OwnersAccommodationActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService=retrofit.create(AccommodationService.class);
    private Animation slideInAnimation;
    private Animation slideOutAnimation;
    private boolean isDrawerOpen = false;
    ActivityOwnersAccommodationBinding binding;
    AccommodationListAdapter listAdapter;
    // ArrayList<Accommodation> accommodationArrayList = new ArrayList<Accommodation>();//initial
    List<Accommodation> accommodationArrayListCalled = new ArrayList<Accommodation>();//initial with service
    ArrayList<Accommodation> accommodationsToShow=new ArrayList<>();
    Accommodation accommodation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOwnersAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        Toast.makeText(this, TokenManager.getLoggedInUser().getUsername(), Toast.LENGTH_SHORT).show();

        Call call = accommodationService.findByOwnerId(TokenManager.getLoggedInUser().getUsername());
        call.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call, Response<List<Accommodation>> response) {
                if (response.isSuccessful()) {
                    accommodationArrayListCalled = response.body();
                    for(Accommodation a:accommodationArrayListCalled)
                        System.out.println(a);

                    for(Accommodation a:accommodationArrayListCalled){
                        accommodationsToShow.add(a);
                    }

                    listAdapter = new AccommodationListAdapter(OwnersAccommodationActivity.this, accommodationsToShow);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Accommodation selectedAccommodation = (Accommodation) parent.getItemAtPosition(position);

                            // Dohvati accommodationId iz odabranog objekta
                            Long accommodationId = selectedAccommodation.id;

                            Intent intent = new Intent(OwnersAccommodationActivity.this, EditAccommodationsDataActivity.class);
                            intent.putExtra("accommodationId", accommodationId);
                            startActivity(intent);
                        }
                    });
                } else {
                    // Handle error
                    Log.e("GRESKA",String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Accommodation>> call, Throwable t) {
                Log.e("GREEEESKA",t.getMessage());
                t.printStackTrace();
            }
        });
        //



    }
}