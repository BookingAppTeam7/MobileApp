package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.fragments.accommodations.SearchBottomSheetFragment;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationDetails;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.TimeSlot;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.UserService;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwt;
import retrofit2.http.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class HomeScreenActivity extends AppCompatActivity implements BottomSheetListener {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService=retrofit.create(AccommodationService.class);
    private Animation slideInAnimation;
    private Animation slideOutAnimation;
    private boolean isDrawerOpen = false;
    ActivityHomeScreenBinding binding;
    AccommodationListAdapter listAdapter;
   // ArrayList<Accommodation> accommodationArrayList = new ArrayList<Accommodation>();//initial
    List<Accommodation> accommodationArrayListCalled = new ArrayList<Accommodation>();//initial with service
    ArrayList<Accommodation> accommodationsToShow=new ArrayList<>();
    List<AccommodationDetails> searchedAccommodationArrayList = new ArrayList<>();
    Accommodation accommodation;
    String loggedInUsername;
    String loggedInRole;
    RoleEnum role=RoleEnum.UNAUTHENTICATED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=this.getIntent();
        if(intent!=null){
            loggedInUsername=intent.getStringExtra("username");
            loggedInRole=intent.getStringExtra("role");
            if(loggedInUsername!=null)
                Log.e("USERNAME",loggedInUsername);
            if(loggedInRole!=null)
                Log.e("ROLE",loggedInRole);
        }
        //String roleString = getRoleFromToken();
        //Toast.makeText(HomeScreenActivity.this, "Role:  "+roleString, Toast.LENGTH_SHORT).show();


        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        //int[] imageList = {R.drawable.accommodation1, R.drawable.accommodation2, R.drawable.accommodation1, R.drawable.accommodation2};

        //
        Log.e("PRE","AAAAAAAAAAAAAAAAAAAAAA");
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
                        accommodationsToShow.add(a);
                        Log.e("SMESTAJ",a.toString());
                    }

                    listAdapter = new AccommodationListAdapter(HomeScreenActivity.this, accommodationsToShow);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(HomeScreenActivity.this, DetailedActivity.class);
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
                           intent.putExtra("reviewsList",new ArrayList<>(accommodationsToShow.get(position).getReviews()));
                            intent.putExtra("assets",new ArrayList<>(accommodationsToShow.get(position).getAssets()));
                            intent.putExtra("priceList",new ArrayList<>(accommodationsToShow.get(position).getPrices()));
                            intent.putExtra("minGuests",accommodationsToShow.get(position).getMinGuests());
                            intent.putExtra("maxGuests",accommodationsToShow.get(position).getMaxGuests());
                            intent.putExtra("type",accommodationsToShow.get(position).getType().toString());
                            intent.putExtra("cancelDeadline",String.valueOf(accommodationsToShow.get(position).getCancellationDeadline()));
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
        /////////////////////////////PODESAVANJE NAVBARA
        MenuItem logInMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_login);
        MenuItem registerMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_registration);
        MenuItem accomodationMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_accommodation_approval);
        MenuItem aboutUsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_about_us);
        MenuItem myAccountItem = binding.navigationView.getMenu().findItem(R.id.menu_account);
        MenuItem notificationSettings=binding.navigationView.getMenu().findItem(R.id.menu_notification_settings);
        MenuItem logOut=binding.navigationView.getMenu().findItem(R.id.menu_logout);
        MenuItem addAccommodationMenuItem=binding.navigationView.getMenu().findItem(R.id.createAccommodation);
        MenuItem accommodationsRequestMenuItem=binding.navigationView.getMenu().findItem(R.id.accommodationRequests);

        MenuItem rateOwnerItem=binding.navigationView.getMenu().findItem(R.id.menu_rate_owner);

        MenuItem myReservationsMenuItem=binding.navigationView.getMenu().findItem(R.id.menu_my_reservations);

        if(loggedInRole==null){//znaci da je neulogovan
            logInMenuItem.setVisible(true);
            registerMenuItem.setVisible(true);
            accomodationMenuItem.setVisible(false);
            aboutUsMenuItem.setVisible(true);
            myAccountItem.setVisible(false);
            notificationSettings.setVisible(false);
            logOut.setVisible(false);
            addAccommodationMenuItem.setVisible(false);
            accommodationsRequestMenuItem.setVisible(false);
            myReservationsMenuItem.setVisible(false);
        }else{
            if(loggedInRole.equals("GUEST")){//za goste
                logInMenuItem.setVisible(false);
                registerMenuItem.setVisible(false);
                accomodationMenuItem.setVisible(false);
                aboutUsMenuItem.setVisible(true);
                myAccountItem.setVisible(true);
                notificationSettings.setVisible(true);
                logOut.setVisible(true);
                addAccommodationMenuItem.setVisible(false);
                accommodationsRequestMenuItem.setVisible(false);

                rateOwnerItem.setVisible(true);

                myReservationsMenuItem.setVisible(true);

            }
            if(loggedInRole.equals("OWNER")){
                logInMenuItem.setVisible(false);
                registerMenuItem.setVisible(false);
                accomodationMenuItem.setVisible(true);
                aboutUsMenuItem.setVisible(true);
                myAccountItem.setVisible(true);
                notificationSettings.setVisible(true);
                logOut.setVisible(true);
                addAccommodationMenuItem.setVisible(true);
                accommodationsRequestMenuItem.setVisible(false);
                myReservationsMenuItem.setVisible(false);
            }
            if(loggedInRole.equals("ADMIN")){
                logInMenuItem.setVisible(false);
                registerMenuItem.setVisible(false);
                accomodationMenuItem.setVisible(false);
                aboutUsMenuItem.setVisible(true);
                myAccountItem.setVisible(true);
                notificationSettings.setVisible(false);
                logOut.setVisible(true);
                addAccommodationMenuItem.setVisible(false);
                accommodationsRequestMenuItem.setVisible(true);
                myReservationsMenuItem.setVisible(false);

            }
        }

        ///////////////////////////////////
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                MenuItem logInMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_login);
                MenuItem registerMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_registration);
                MenuItem accomodationMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_accommodation_approval);
                MenuItem aboutUsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_about_us);
                MenuItem myAccountItem = binding.navigationView.getMenu().findItem(R.id.menu_account);
                MenuItem addAccommodationMenuItem=binding.navigationView.getMenu().findItem(R.id.createAccommodation);
                MenuItem accommodationsRequestMenuItem=binding.navigationView.getMenu().findItem(R.id.accommodationRequests);

                MenuItem rateOwnerItem=binding.navigationView.getMenu().findItem(R.id.menu_rate_owner);
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



                MenuItem notificationSettings=binding.navigationView.getMenu().findItem(R.id.menu_notification_settings);
                MenuItem logOut=binding.navigationView.getMenu().findItem(R.id.menu_logout);

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
                    TokenManager tokenManager = new TokenManager();
                   // String jwtToken = tokenManager.getLoggedInUser().getJwt();
                    String jwtToken = TokenManager.getJwtToken();

                    performMyAccountAction("Bearer "+jwtToken);
                    return true;
                }else if(item.getItemId()==notificationSettings.getItemId()){
                    User user=TokenManager.getLoggedInUser();
                    performNotificationSettingsAction(user.role,user);
                    return  true;
                }else if(item.getItemId()==logOut.getItemId()){
                    performLogOutAction();
                    return true;
                }
                else if(item.getItemId()==addAccommodationMenuItem.getItemId()){
                    Intent intent = new Intent(HomeScreenActivity.this, CreateAccommodationActivity.class);
                    startActivity(intent);
                    return true;
                }

                else if(item.getItemId()==accommodationsRequestMenuItem.getItemId()){
                    Intent intent = new Intent(HomeScreenActivity.this, AccomodationApprovalActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==rateOwnerItem.getItemId()){
                    Intent intent = new Intent(HomeScreenActivity.this, RateOwnerActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId()==myReservationsMenuItem.getItemId()){
                    Intent intent = new Intent(HomeScreenActivity.this, GuestsReservationsActivity.class);
                    startActivity(intent);
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
        for(Accommodation a:accommodationArrayListCalled)
            Log.e("DRUGI POZIV",a.toString());
        Log.e("ARRIVAL",arrivalDate);
        Log.e("CHECKOUT",checkoutDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date arrival;
        Date checkout;

        try {
            arrival = dateFormat.parse(arrivalDate + " 00:00:00");
            checkout = dateFormat.parse(checkoutDate + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
            return; // Handle the parsing error as needed
        }
        Log.e("ARRIVAL",arrival.toString());
        Log.e("CHECKOUT",checkout.toString());
        Log.e("Formatted Arrival", dateFormat.format(arrival));
        Log.e("Formatted Checkout", dateFormat.format(checkout));
        Call<List<AccommodationDetails>> call = accommodationService.search(place, guests, dateFormat.format(arrival), dateFormat.format(checkout));
        call.enqueue(new Callback<List<AccommodationDetails>>() {
            @Override
            public void onResponse(Call<List<AccommodationDetails>> call, Response<List<AccommodationDetails>> response) {
                if (response.isSuccessful()) {
                    List<AccommodationDetails> accommodations = response.body();
                    //for(AccommodationDetails ad:accommodations)
                        Log.e("USPEH222!!!",accommodations.toString());
                    Intent intent = new Intent(HomeScreenActivity.this, SearchedAccommodationsActivity.class);
                    intent.putExtra("username",loggedInUsername);
                    intent.putExtra("role",loggedInRole);
                    intent.putExtra("accommodationsList", new ArrayList<>(accommodations));
                    startActivity(intent);

                } else {
                    Log.e("ERROR", "Response Code: " + response.code());
                    try {
                        Log.e("ERROR BODY", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationDetails>> call, Throwable t) {
                Log.e("FAILURE!",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onFilterButtonClicked(TypeEnum selectedType, String joined, String minTotalPrice, String maxTotalPrice) {

    }

    @Override
    public void onReservationButtonClicked(int guests, String arrivalDate, String checkoutDate) {

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
            Intent intent=new Intent(HomeScreenActivity.this, OwnersAccommodationActivity.class);
            startActivity(intent);
        }

        public void performMyAccountAction(@Header("Authorization") String authorizationHeader){
            Intent intent=new Intent(HomeScreenActivity.this,AccountScreenActivity.class);
            startActivity(intent);
        }

    public void performNotificationSettingsAction(RoleEnum role,User user){
        if(role.equals(RoleEnum.OWNER)){
            Intent intent=new Intent(HomeScreenActivity.this,OwnerNotificationSettingsActivity.class);
            startActivity(intent);
        }else if(role.equals(RoleEnum.GUEST)){
            Intent intent=new Intent(HomeScreenActivity.this,GuestNotificationSettingsActivity.class);
            startActivity(intent);
        }else{
            return;
        }

    }




    // Kod za dohvaćanje JWT tokena
    private String getRoleFromToken() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        return preferences.getString("role", "");
    }

    private void   performLogOutAction(){
        UserService userService = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
        Call<Void> call = userService.logout();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("LOGOUT","LOGOOOUT");
                    loggedInRole=null;
                  Toast.makeText(HomeScreenActivity.this,"LOG OUT SUCCESSFULL",Toast.LENGTH_LONG);
                  TokenManager.setLoggedInUser(null);
                    Intent intent = new Intent(HomeScreenActivity.this,LogInScreenActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAAAAAAGGGGGGGGGG ", "Error: " + t.getMessage());
            }
        });

    }



}