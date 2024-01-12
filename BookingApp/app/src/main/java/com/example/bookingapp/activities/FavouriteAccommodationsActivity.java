package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.databinding.ActivityFavouriteAccommodationsBinding;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavouriteAccommodationsActivity extends AppCompatActivity {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService=retrofit.create(AccommodationService.class);
    ActivityFavouriteAccommodationsBinding binding;
    AccommodationListAdapter listAdapter;
    ArrayList<Accommodation> accommodationsToShow=new ArrayList<>();
    List<Accommodation> accommodationArrayListCalled = new ArrayList<Accommodation>();
    String loggedInUsername;
    String loggedInRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFavouriteAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        loggedInUsername=intent.getStringExtra("username");
        loggedInRole=intent.getStringExtra("role");
        Log.e("ROLE",loggedInRole);
        String favouriteAccommodations=intent.getStringExtra("favouriteAccommodations");
        Log.e("USERNAME",loggedInUsername);
        Log.e("FAVOURITE ACCS",favouriteAccommodations);


        Call call = accommodationService.findAllApproved();
        call.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call, Response<List<Accommodation>> response) {
                if (response.isSuccessful()) {
                    accommodationArrayListCalled = response.body();
                    for(Accommodation a:accommodationArrayListCalled)
                        System.out.println(a);


                    Log.e("DUZINA",String.valueOf(accommodationArrayListCalled.size()));
                    for(Accommodation a:accommodationArrayListCalled){
                        if(isInGuestFavourite(a.id,favouriteAccommodations)){
                            accommodationsToShow.add(a);
                            Log.e("SMESTAJ",a.toString());
                        }
                    }

                    listAdapter = new AccommodationListAdapter(FavouriteAccommodationsActivity.this, accommodationsToShow);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(FavouriteAccommodationsActivity.this, DetailedActivity.class);
                            if(loggedInUsername!=null)
                                intent.putExtra("username",loggedInUsername);
                            if(loggedInRole!=null)
                                intent.putExtra("role",loggedInRole);
                            intent.putExtra("accommodationId",accommodationsToShow.get(position).getId());
                            intent.putExtra("name", accommodationsToShow.get(position).getName());
                            intent.putExtra("description", accommodationsToShow.get(position).getDescription());
                            intent.putExtra("image", "putanja");
                            intent.putExtra("location",accommodationsToShow.get(position).getLocation().address+", "+accommodationsToShow.get(position).getLocation().city);
                            intent.putExtra("locationX",accommodationsToShow.get(position).getLocation().x);
                            intent.putExtra("locationY",accommodationsToShow.get(position).getLocation().y);
                            //intent.putExtra("reviewsList",new ArrayList<>(accommodationsToShow.get(position).getReviews()));
                            intent.putExtra("assets",new ArrayList<>(accommodationsToShow.get(position).getAssets()));
                            intent.putExtra("priceList",new ArrayList<>(accommodationsToShow.get(position).getPrices()));
                            intent.putExtra("minGuests",accommodationsToShow.get(position).getMinGuests());
                            intent.putExtra("maxGuests",accommodationsToShow.get(position).getMaxGuests());
                            intent.putExtra("type",accommodationsToShow.get(position).getType().toString());
                            intent.putExtra("cancelDeadline",String.valueOf(accommodationsToShow.get(position).getCancellationDeadline()));
                            intent.putExtra("reservationConfirmation",String.valueOf(accommodationsToShow.get(position).getReservationConfirmation()));
                            intent.putExtra("ownerId",accommodationsToShow.get(position).getOwnerId());
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
    }
    public boolean isInGuestFavourite(Long id,String favouriteAccommodations){
        String idStr = id.toString();
        String[] array = favouriteAccommodations.split(",");

        for (String str : array) {
            if (str.trim().equals(idStr)) {
                return true;
            }
        }

        return false;
    }
}