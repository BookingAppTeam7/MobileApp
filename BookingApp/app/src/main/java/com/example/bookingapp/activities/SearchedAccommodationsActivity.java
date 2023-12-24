package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.SearchedAccommodationListAdapter;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;
import com.example.bookingapp.databinding.ActivitySearchedAccommodationsBinding;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationDetails;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchedAccommodationsActivity extends AppCompatActivity implements BottomSheetListener {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService=retrofit.create(AccommodationService.class);
    ActivitySearchedAccommodationsBinding binding;
    public List<AccommodationDetails> accommodationDetails=new ArrayList<>();
    public List<AccommodationDetails> accommodationDetailsParsed=new ArrayList<>();
    public List<AccommodationDetails> accommodationDetailsParsedToShow=new ArrayList<>();
    private Animation slideInAnimation;
    private Animation slideOutAnimation;
    private boolean isDrawerOpen = false;
    SearchedAccommodationListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchedAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String roleString = getRoleFromToken();
        Toast.makeText(SearchedAccommodationsActivity.this, "Role:  "+roleString, Toast.LENGTH_SHORT).show();
        accommodationDetails=(ArrayList<AccommodationDetails>) getIntent().getSerializableExtra("accommodationsList");
        Intent intent=this.getIntent();
        if(intent!=null){
            for(AccommodationDetails ad:accommodationDetails){
                Log.e("PRE ONAJ LOS",ad.toString());
                Long accommodationId=ad.getAccommodation().getId();
                Call<Accommodation> call = accommodationService.findById(accommodationId);

                call.enqueue(new Callback<Accommodation>() {
                    @Override
                    public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                        if (response.isSuccessful()) {
                            Accommodation accommodation = response.body();
                            ad.setAccommodation(accommodation);
                            Log.e("NOVI ACC",ad.toString());
                            accommodationDetailsParsed.add(ad);
                            accommodationDetailsParsedToShow.add(ad);
                        } else {
                            Log.e("Error", "Response Code: " + response.code());
                            try {
                                Log.e("Error Body", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Accommodation> call, Throwable t) {
                        Log.e("Failure", t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
            for(AccommodationDetails ad:accommodationDetails){
                accommodationDetailsParsedToShow.add(ad);
            }
            Log.e("PRIMLJENO",accommodationDetails.toString());
            adapter=new SearchedAccommodationListAdapter(SearchedAccommodationsActivity.this,accommodationDetails);
            binding.listview.setAdapter(adapter);
            binding.listview.setClickable(true);
            //TODO: promeni filter xml skroz
            //TODO: funkcionalnost na klik filtera

            binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Long accommodationId=accommodationDetails.get(position).getAccommodation().id;
                    Call<Accommodation> call = accommodationService.findById(accommodationId);

                    call.enqueue(new Callback<Accommodation>() {
                        @Override
                        public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                            if (response.isSuccessful()) {
                                Accommodation accommodation = response.body();
                                Intent intent = new Intent(SearchedAccommodationsActivity.this, DetailedActivity.class);
                                intent.putExtra("name", accommodation.getName());
                                intent.putExtra("description", accommodation.getDescription());
                                intent.putExtra("image", accommodation.getImages().get(0));
                                intent.putExtra("location",accommodation.getLocation().address+", "+accommodation.getLocation().city);
                                intent.putExtra("locationX",accommodation.getLocation().x);
                                intent.putExtra("locationY",accommodation.getLocation().y);
                                intent.putExtra("reviewsList",new ArrayList<>(accommodation.getReviews()));
                                intent.putExtra("assets",new ArrayList<>(accommodation.getAssets()));
                                intent.putExtra("priceList",new ArrayList<>(accommodation.getPrices()));
                                startActivity(intent);
                            } else {
                                Log.e("Error", "Response Code: " + response.code());
                                try {
                                    Log.e("Error Body", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<Accommodation> call, Throwable t) {
                            Log.e("Failure", t.getMessage());
                            t.printStackTrace();
                        }
                    });

                }
            });

        }
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;
        slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        binding.toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.navigationView.getVisibility() == View.VISIBLE) {
                    binding.navigationView.setVisibility(View.INVISIBLE);
                } else {
                    binding.navigationView.setAnimation(slideInAnimation);
                    binding.navigationView.setVisibility(View.VISIBLE);
                }


            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                MenuItem logInMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_login);
                MenuItem registerMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_registration);
                MenuItem accomodationMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_accommodation_approval);
                MenuItem aboutUsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_about_us);
                MenuItem myAccountItem = binding.navigationView.getMenu().findItem(R.id.menu_account);

//                if (roleString.equals("OWNER")) {
//                    // Prikazi navigaciju za vlasnika
//                    logInMenuItem.setVisible(false);
//                    registerMenuItem.setVisible(false);
//                    accomodationMenuItem.setVisible(true); // Prikazi opciju za vlasnika
//                    aboutUsMenuItem.setVisible(true);
//                } else
//                if(roleString.equals("GUEST")){
//                    // Prikazi navigaciju za gosta
//                    logInMenuItem.setVisible(false);
//                    registerMenuItem.setVisible(false);
//                    accomodationMenuItem.setVisible(false); // Sakrij opciju za vlasnika
//                    aboutUsMenuItem.setVisible(true);
//                }
//                else{
//                    logInMenuItem.setVisible(true);
//                    registerMenuItem.setVisible(true);
//                    accomodationMenuItem.setVisible(false); // Sakrij opciju za vlasnika
//                    aboutUsMenuItem.setVisible(true);
//                }

                if (item.getItemId() == logInMenuItem.getItemId()) {
                    performLoginAction();
                    return true;
                } else if (item.getItemId() == registerMenuItem.getItemId()) {
                    performRegistrationAction();
                    return true;
                } else if (item.getItemId() == aboutUsMenuItem.getItemId()) {
                    performAboutUsAction();
                    return true;
                } else if (item.getItemId() == accomodationMenuItem.getItemId()) {
                    performAccomodationAction();
                    return true;
                }
                else if(item.getItemId()==myAccountItem.getItemId()){
                    performMyAccountAction();
                    return true;
                }

                // Zatvori navigacijski izbornik
                binding.drawerLayout.closeDrawer(binding.navigationView);

                return true;
            }
        });

        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_IDLE) {
                    isDrawerOpen = binding.drawerLayout.isDrawerOpen(binding.navigationView.getRootView());
                    if (!isDrawerOpen) {
                        binding.navigationView.clearAnimation();
                    }
                }
            }
        });
        binding.btnFilters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FilterBottomSheetDialogFragment bottomSheetFragment = new FilterBottomSheetDialogFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                MenuItem logInMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_login);
                MenuItem registerMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_registration);
                MenuItem accomodationMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_accommodation_approval);
                MenuItem aboutUsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_about_us);
                MenuItem myAccountItem = binding.navigationView.getMenu().findItem(R.id.menu_account);

//                if (roleString.equals("OWNER")) {
//                    // Prikazi navigaciju za vlasnika
//                    logInMenuItem.setVisible(false);
//                    registerMenuItem.setVisible(false);
//                    accomodationMenuItem.setVisible(true); // Prikazi opciju za vlasnika
//                    aboutUsMenuItem.setVisible(true);
//                } else
//                if(roleString.equals("GUEST")){
//                    // Prikazi navigaciju za gosta
//                    logInMenuItem.setVisible(false);
//                    registerMenuItem.setVisible(false);
//                    accomodationMenuItem.setVisible(false); // Sakrij opciju za vlasnika
//                    aboutUsMenuItem.setVisible(true);
//                }
//                else{
//                    logInMenuItem.setVisible(true);
//                    registerMenuItem.setVisible(true);
//                    accomodationMenuItem.setVisible(false); // Sakrij opciju za vlasnika
//                    aboutUsMenuItem.setVisible(true);
//                }

                if (item.getItemId() == logInMenuItem.getItemId()) {
                    performLoginAction();
                    return true;
                } else if (item.getItemId() == registerMenuItem.getItemId()) {
                    performRegistrationAction();
                    return true;
                } else if (item.getItemId() == aboutUsMenuItem.getItemId()) {
                    performAboutUsAction();
                    return true;
                } else if (item.getItemId() == accomodationMenuItem.getItemId()) {
                    performAccomodationAction();
                    return true;
                }
                else if(item.getItemId()==myAccountItem.getItemId()){
                    performMyAccountAction();
                    return true;
                }

                // Zatvori navigacijski izbornik
                binding.drawerLayout.closeDrawer(binding.navigationView);

                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }
    @Override
    public void onSearchButtonClicked(String place, int guests, String arrivalDate, String checkoutDate) {

    }

    @Override
    public void onFilterButtonClicked(TypeEnum selectedType, String joined, String minTotalPrice, String maxTotalPrice) {
        //TODO: Present results to cards
        String searchedListJson = new Gson().toJson(accommodationDetailsParsed);
        Log.e("PRE JSON",accommodationDetailsParsed.toString());
        Log.e("LIST",searchedListJson);
        //Log.e("JOINED",joined);
        //Log.e("SELECTED TYPE",selectedType.toString());
        Log.e("MINTOTALPRICE",minTotalPrice);
        Log.e("MAXTOTALPRICE",maxTotalPrice);
        Call<List<AccommodationDetails>> call = accommodationService.filter(joined, selectedType, minTotalPrice, maxTotalPrice);

        call.enqueue(new Callback<List<AccommodationDetails>>() {
            @Override
            public void onResponse(Call<List<AccommodationDetails>> call, Response<List<AccommodationDetails>> response) {
                if (response.isSuccessful()) {
                    List<AccommodationDetails> result = response.body();
                    Log.e("FILTER RESULT", result != null ? result.toString() : "null");
                    for(AccommodationDetails ad:result)
                        Log.e("USPEOOOOO!!!",ad.toString());

                    accommodationDetails.clear();
                    accommodationDetails.addAll(result);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.e("FILTER ERROR", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationDetails>> call, Throwable t) {
                Log.e("FILTER FAILURE", "Error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onReservationButtonClicked(int guests, String arrivalDate, String checkoutDate) {

    }

    public void performLoginAction(){
        Intent intent = new Intent(SearchedAccommodationsActivity.this, LogInScreenActivity.class);
        startActivity(intent);
    }
    public void performRegistrationAction(){
        Intent intent = new Intent(SearchedAccommodationsActivity.this, RegisterScreenActivity.class);
        startActivity(intent);
    }
    public void performAboutUsAction(){

    }

    public void performAccomodationAction(){
        Intent intent=new Intent(SearchedAccommodationsActivity.this, CreateAccommodationActivity.class);
        startActivity(intent);
    }

    public void performMyAccountAction(){
        Intent intent=new Intent(SearchedAccommodationsActivity.this,AccountScreenActivity.class);
        startActivity(intent);
    }
    private String getRoleFromToken() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        return preferences.getString("role", "");
    }
}