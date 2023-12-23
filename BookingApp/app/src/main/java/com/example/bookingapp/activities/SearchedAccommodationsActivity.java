package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.SearchedAccommodationListAdapter;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;
import com.example.bookingapp.databinding.ActivitySearchedAccommodationsBinding;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.AccommodationDetails;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class SearchedAccommodationsActivity extends AppCompatActivity implements BottomSheetListener {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService=retrofit.create(AccommodationService.class);
    ActivitySearchedAccommodationsBinding binding;
    public List<AccommodationDetails> accommodationDetails=new ArrayList<>();
    private boolean isDrawerOpen = false;
    SearchedAccommodationListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchedAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String roleString = getRoleFromToken();
        Toast.makeText(SearchedAccommodationsActivity.this, "Role:  "+roleString, Toast.LENGTH_SHORT).show();

        Intent intent=this.getIntent();
        if(intent!=null){
            accommodationDetails=(ArrayList<AccommodationDetails>) getIntent().getSerializableExtra("accommodationsList");
            adapter=new SearchedAccommodationListAdapter(SearchedAccommodationsActivity.this,accommodationDetails);
            binding.listview.setAdapter(adapter);
            binding.listview.setClickable(false);//promeni posle na true
            //TODO: namesti da se prebaci na taj DetailedActivity na klik
            //promeni filter xml skroz
            //funkcionalnost na klik filtera
        }
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;


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
    public void onSearchButtonClicked(String place, int guests, String arrivalDate, String checkoutDate) {

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