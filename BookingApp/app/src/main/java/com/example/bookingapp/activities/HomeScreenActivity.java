package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.fragments.accommodations.SearchBottomSheetFragment;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.ReviewType;
import com.example.bookingapp.model.TimeSlot;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import android.view.MenuItem;

public class HomeScreenActivity extends AppCompatActivity implements BottomSheetListener {
    private Animation slideInAnimation;
    private Animation slideOutAnimation;
    private boolean isDrawerOpen = false;
    ActivityHomeScreenBinding binding;
    AccommodationListAdapter listAdapter;
    ArrayList<Accommodation> accommodationArrayList = new ArrayList<Accommodation>();//initial
    ArrayList<Accommodation> accommodationsToShow=new ArrayList<>();
    ArrayList<Accommodation> searchedAccommodationArrayList = new ArrayList<Accommodation>();
    Accommodation accommodation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        int[] imageList = {R.drawable.accommodation1, R.drawable.accommodation2, R.drawable.accommodation1, R.drawable.accommodation2};
        Long[] idList = {1L, 2L, 3L, 4L};
        String[] nameList = {"Accommodation in Novi Sad", "Belgrade accommodation", "Prague accommodation", "Paris accommodation"};
        String[] descriptionList = {"Beautiful accommodation settled in Novi Sad", "Beautiful accommodation settled in Belgrade",
                "Beautiful accommodation settled in Prague", "Beautiful accommodation settled in Paris"};
        int[] minGuestsList={1,3,4,2};
        int[] maxGuestsList={5,8,8,5};
        double [] locationXList={45.26799224033295,44.78318632559004,50.102423832053915,48.870974300967234};
        double [] locationYList={19.830824522193232, 20.49925984639849, 14.480125374774326, 2.478835370652442};
        String [] locationStrList={"Novi Sad","Belgrade","Prague",
                "Paris"};
        double [] priceList={150.0,215.0,100.0,199.99};
        List<Review> reviewsList = new ArrayList<>();


        reviewsList.add(new Review(R.drawable.ic_user,"User1", ReviewType.OWNER, "Great experience!", 5));
        reviewsList.add(new Review(R.drawable.ic_user,"User2", ReviewType.ACCOMMODATION, "Nice place to stay.", 4));
        reviewsList.add(new Review(R.drawable.ic_user,"User3", ReviewType.OWNER, "Very helpful owner.", 5));
        reviewsList.add(new Review(R.drawable.ic_user,"User4", ReviewType.ACCOMMODATION, "Clean and comfortable.", 4));

        ArrayList<String> assets=new ArrayList<>();
        assets.add("TV");
        assets.add("WI-FI");
        assets.add("Free parking");
        assets.add("Air conditioner");

        ArrayList<TimeSlot> availability=new ArrayList<>();
        availability.add(new TimeSlot(1L, LocalDate.of(2023,12,3),LocalDate.of(2023,12,6)));
        availability.add(new TimeSlot(1L, LocalDate.of(2023,12,9),LocalDate.of(2023,12,15)));

        for (int i = 0; i < imageList.length; i++) {
            accommodation = new Accommodation(idList[i], nameList[i], descriptionList[i],minGuestsList[i],maxGuestsList[i] ,imageList[i],
                    locationStrList[i],locationXList[i],locationYList[i],priceList[i],reviewsList,assets,availability);
            accommodationArrayList.add(accommodation);
        }
        for(Accommodation a:accommodationArrayList){
            accommodationsToShow.add(a);
        }

        listAdapter = new AccommodationListAdapter(HomeScreenActivity.this, accommodationsToShow);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeScreenActivity.this, DetailedActivity.class);
                intent.putExtra("name", accommodationsToShow.get(position).getName());
                intent.putExtra("description", accommodationsToShow.get(position).getDescription());
                intent.putExtra("image", accommodationsToShow.get(position).getImage());
                intent.putExtra("location",accommodationsToShow.get(position).getLocation());
                intent.putExtra("locationX",accommodationsToShow.get(position).getLocationX());
                intent.putExtra("locationY",accommodationsToShow.get(position).getLocationY());
                intent.putExtra("price",accommodationsToShow.get(position).getPrice());
                intent.putExtra("reviewsList",new ArrayList<>(accommodationsToShow.get(position).getReviews()));
                intent.putExtra("assets",new ArrayList<>(accommodationsToShow.get(position).getAssets()));
                intent.putExtra("availability",new ArrayList<>(accommodationsToShow.get(position).getAvailability()));
                startActivity(intent);
            }
        });

        binding.btnFilters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FilterBottomSheetDialogFragment bottomSheetFragment = new FilterBottomSheetDialogFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBottomSheetFragment bottomSheetFragment = new SearchBottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

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

        // Postavljanje animacija na NavigationView pri zatvaranju
        // Postavljanje animacija na NavigationView pri zatvaranju
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                MenuItem logInMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_login);
                MenuItem registerMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_registration);
                MenuItem accomodationMenuItem=binding.navigationView.getMenu().findItem(R.id.menu_accommodation_approval);
                MenuItem aboutUsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_about_us);

                if (item.getItemId() == logInMenuItem.getItemId()) {
                    performLoginAction();
                    return true;
                } else if (item.getItemId() == registerMenuItem.getItemId()) {
                    performRegistrationAction();
                    return true;
                } else if (item.getItemId() == aboutUsMenuItem.getItemId()) {
                    performAboutUsAction();
                    return true;
                }else if(item.getItemId()==accomodationMenuItem.getItemId()){
                    performAccomodationAction();
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
        // TODO: Implement a function that checks which accommodations match the parameters.
        // Refresh all items on the main screen so it shows only ones that match the search
        Log.d("NUMBER OF ACCOMMODATIONS",String.valueOf(accommodationArrayList.size()));
        searchedAccommodationArrayList = searchAccommodations(accommodationArrayList, place, guests);
        listAdapter.updateData(searchedAccommodationArrayList);
    }

    public ArrayList<Accommodation> searchAccommodations(ArrayList<Accommodation> sourceList, String place, int guests) {
        ArrayList<Accommodation> retAccommodation = new ArrayList<>();
        for (Accommodation a : sourceList) {
            if (a.getLocation().equalsIgnoreCase(place) && guests>=a.getMinGuests() && guests<=a.getMaxGuests()) {
                retAccommodation.add(a);
            }
        }
        return retAccommodation;
    }

    public void performLoginAction(){
            Intent intent = new Intent(HomeScreenActivity.this, LogInScreenActivity.class);
            startActivity(intent);
        }
        public void performRegistrationAction(){
            Intent intent = new Intent(HomeScreenActivity.this, RegisterScreenActivity.class);
            startActivity(intent);
        }
        public void performAboutUsAction(){

        }

        public void performAccomodationAction(){
            Intent intent=new Intent(HomeScreenActivity.this,AccomodationApprovalActivity.class);
            startActivity(intent);
        }


}