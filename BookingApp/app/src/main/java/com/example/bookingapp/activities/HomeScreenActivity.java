package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.adapters.NotificationListAdapter;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;
import com.example.bookingapp.fragments.accommodations.SearchBottomSheetFragment;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationDetails;
import com.example.bookingapp.model.Notification;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.TimeSlot;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.NotificationService;
import com.example.bookingapp.services.UserService;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import retrofit2.http.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class HomeScreenActivity extends AppCompatActivity implements BottomSheetListener, SensorEventListener {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public NotificationService notificationService = retrofit.create(NotificationService.class);
    private Animation slideInAnimation;
    private Animation slideOutAnimation;
    private boolean isDrawerOpen = false;
    ActivityHomeScreenBinding binding;
    AccommodationListAdapter listAdapter;
    List<Accommodation> accommodationArrayListCalled = new ArrayList<Accommodation>();//initial with service
    ArrayList<Accommodation> accommodationsToShow = new ArrayList<>();
    List<AccommodationDetails> searchedAccommodationArrayList = new ArrayList<>();
    Accommodation accommodation;
    String loggedInUsername;
    String loggedInRole;
    RoleEnum role = RoleEnum.UNAUTHENTICATED;
    private int numOfNotifications = 0;
    private boolean firstRun=true;
    private List<Long> alreadyShownNewNot=new ArrayList<>();
    private static String CHANNEL_ID = "Zero channel";
    private boolean ascendingOrder = false;

    private SensorManager sensorManager;
    private static final int SHAKE_THRESHOLD = 800;
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        if (intent != null) {
            loggedInUsername = intent.getStringExtra("username");
            loggedInRole = intent.getStringExtra("role");
            if (loggedInUsername != null)
                Log.e("USERNAME", loggedInUsername);
            if (loggedInRole != null)
                Log.e("ROLE", loggedInRole);
        }
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        this.listView = binding.listview;
        createNotificationChannel();

        if (loggedInUsername != null) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    callNotifications();
                    handler.postDelayed(this, 2000);
                }
            };
            // Start the initial task
            handler.postDelayed(runnable, 2000);
        }


        Call call = accommodationService.findAllApproved();
        call.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call, Response<List<Accommodation>> response) {
                if (response.isSuccessful()) {
                    accommodationArrayListCalled = response.body();
                    for (Accommodation a : accommodationArrayListCalled)
                        System.out.println(a);

                    for (Accommodation a : accommodationArrayListCalled) {
                        accommodationsToShow.add(a);
                        //Log.e("SMESTAJ",a.toString());
                    }

                    listAdapter = new AccommodationListAdapter(HomeScreenActivity.this, accommodationsToShow);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(HomeScreenActivity.this, DetailedActivity.class);
                            if (loggedInUsername != null)
                                intent.putExtra("username", loggedInUsername);
                            if (loggedInRole != null)
                                intent.putExtra("role", loggedInRole);
                            intent.putExtra("accommodationId", accommodationsToShow.get(position).getId());
                            intent.putExtra("name", accommodationsToShow.get(position).getName());
                            intent.putExtra("description", accommodationsToShow.get(position).getDescription());
                            if(accommodationsToShow.get(position).getImages().size()>0){
                                intent.putExtra("image", accommodationsToShow.get(position).getImages().get(0));
                            }
                            intent.putExtra("location", accommodationsToShow.get(position).getLocation().address + ", " + accommodationsToShow.get(position).getLocation().city);
                            intent.putExtra("locationX", accommodationsToShow.get(position).getLocation().x);
                            intent.putExtra("locationY", accommodationsToShow.get(position).getLocation().y);
                            intent.putExtra("assets", new ArrayList<>(accommodationsToShow.get(position).getAssets()));
                            intent.putExtra("priceList", new ArrayList<>(accommodationsToShow.get(position).getPrices()));
                            intent.putExtra("minGuests", accommodationsToShow.get(position).getMinGuests());
                            intent.putExtra("maxGuests", accommodationsToShow.get(position).getMaxGuests());
                            intent.putExtra("type", accommodationsToShow.get(position).getType().toString());
                            intent.putExtra("cancelDeadline", String.valueOf(accommodationsToShow.get(position).getCancellationDeadline()));
                            intent.putExtra("reservationConfirmation", String.valueOf(accommodationsToShow.get(position).getReservationConfirmation()));
                            intent.putExtra("ownerId", accommodationsToShow.get(position).getOwnerId());
                            if (loggedInUsername != null)
                                intent.putExtra("favouriteAccommodations", TokenManager.getLoggedInUser().favouriteAccommodations);
                            startActivity(intent);
                        }
                    });
                } else {
                    // Handle error
                    Log.e("GRESKA", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Accommodation>> call, Throwable t) {
                Log.e("GREEEESKA", t.getMessage());
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
        MenuItem notificationSettings = binding.navigationView.getMenu().findItem(R.id.menu_notification_settings);
        MenuItem logOut = binding.navigationView.getMenu().findItem(R.id.menu_logout);
        MenuItem addAccommodationMenuItem = binding.navigationView.getMenu().findItem(R.id.createAccommodation);
        MenuItem accommodationsRequestMenuItem = binding.navigationView.getMenu().findItem(R.id.accommodationRequests);
        MenuItem myReservationsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_my_reservations);
        MenuItem allUsersMenuItem = binding.navigationView.getMenu().findItem(R.id.allUsers);


        MenuItem reportUserMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_report_user);
        MenuItem ownerReviewsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_owner_reviews);

        MenuItem favouriteAccommodationsMenuItem = binding.navigationView.getMenu().findItem(R.id.favouriteAccommodations);

        MenuItem report1 = binding.navigationView.getMenu().findItem(R.id.report1);
        MenuItem report2 = binding.navigationView.getMenu().findItem(R.id.report2);

        MenuItem notifications = binding.navigationView.getMenu().findItem(R.id.notifications);

        if (loggedInRole == null) {//znaci da je neulogovan
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
            allUsersMenuItem.setVisible(false);
            reportUserMenuItem.setVisible(false);
            ownerReviewsMenuItem.setVisible(false);
            favouriteAccommodationsMenuItem.setVisible(false);
            report1.setVisible(false);
            report2.setVisible(false);
            notifications.setVisible(false);
        } else {
            if (loggedInRole.equals("GUEST")) {//za goste
                logInMenuItem.setVisible(false);
                registerMenuItem.setVisible(false);
                accomodationMenuItem.setVisible(false);
                aboutUsMenuItem.setVisible(true);
                myAccountItem.setVisible(true);
                notificationSettings.setVisible(true);
                logOut.setVisible(true);
                addAccommodationMenuItem.setVisible(false);
                accommodationsRequestMenuItem.setVisible(false);
                reportUserMenuItem.setVisible(true);
                myReservationsMenuItem.setVisible(true);
                allUsersMenuItem.setVisible(false);
                ownerReviewsMenuItem.setVisible(false);
                favouriteAccommodationsMenuItem.setVisible(true);
                report1.setVisible(false);
                report2.setVisible(false);
                notifications.setVisible(true);
            }
            if (loggedInRole.equals("OWNER")) {
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
                allUsersMenuItem.setVisible(false);
                reportUserMenuItem.setVisible(true);
                ownerReviewsMenuItem.setVisible(true);
                favouriteAccommodationsMenuItem.setVisible(false);
                report1.setVisible(true);
                report2.setVisible(true);
                notifications.setVisible(true);
            }
            if (loggedInRole.equals("ADMIN")) {
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
                allUsersMenuItem.setVisible(true);
                reportUserMenuItem.setVisible(false);
                ownerReviewsMenuItem.setVisible(false);
                favouriteAccommodationsMenuItem.setVisible(false);
                report1.setVisible(false);
                report2.setVisible(false);
                notifications.setVisible(false);
            }
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                MenuItem logInMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_login);
                MenuItem registerMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_registration);
                MenuItem accomodationMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_accommodation_approval);
                MenuItem aboutUsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_about_us);
                MenuItem myAccountItem = binding.navigationView.getMenu().findItem(R.id.menu_account);
                MenuItem addAccommodationMenuItem = binding.navigationView.getMenu().findItem(R.id.createAccommodation);
                MenuItem accommodationsRequestMenuItem = binding.navigationView.getMenu().findItem(R.id.accommodationRequests);
                MenuItem reportUserMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_report_user);
                MenuItem ownerReviewsMenuItem = binding.navigationView.getMenu().findItem(R.id.menu_owner_reviews);
                MenuItem favouriteAccommodationsMenuItem = binding.navigationView.getMenu().findItem(R.id.favouriteAccommodations);
                MenuItem notificationSettings = binding.navigationView.getMenu().findItem(R.id.menu_notification_settings);
                MenuItem logOut = binding.navigationView.getMenu().findItem(R.id.menu_logout);

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
                } else if (item.getItemId() == myAccountItem.getItemId()) {
                    TokenManager tokenManager = new TokenManager();
                    String jwtToken = TokenManager.getJwtToken();
                    performMyAccountAction("Bearer " + jwtToken);
                    return true;
                } else if (item.getItemId() == notificationSettings.getItemId()) {
                    User user = TokenManager.getLoggedInUser();
                    performNotificationSettingsAction(user.role, user);
                    return true;
                } else if (item.getItemId() == logOut.getItemId()) {
                    performLogOutAction();
                    return true;
                } else if (item.getItemId() == addAccommodationMenuItem.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, CreateAccommodationActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == accommodationsRequestMenuItem.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, AccomodationApprovalActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == myReservationsMenuItem.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, GuestsReservationsActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == allUsersMenuItem.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, UsersReviewActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == reportUserMenuItem.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, ReportUserActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == ownerReviewsMenuItem.getItemId()) {

                    Intent intent = new Intent(HomeScreenActivity.this, UserRatingsRequestsActivity.class);
                    intent.putExtra("username", TokenManager.getLoggedInUser().username);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == favouriteAccommodationsMenuItem.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, FavouriteAccommodationsActivity.class);
                    intent.putExtra("username", TokenManager.getLoggedInUser().username);
                    intent.putExtra("role", "GUEST");
                    intent.putExtra("favouriteAccommodations", TokenManager.getLoggedInUser().favouriteAccommodations);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == report1.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, Report1Activity.class);
                    intent.putExtra("username", TokenManager.getLoggedInUser().username);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == report2.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, Report2Activity.class);
                    intent.putExtra("username", TokenManager.getLoggedInUser().username);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == notifications.getItemId()) {
                    Intent intent = new Intent(HomeScreenActivity.this, NotificationsActivity.class);
                    intent.putExtra("username", TokenManager.getLoggedInUser().username);
                    startActivity(intent);
                    return true;
                }

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
        for (Accommodation a : accommodationArrayListCalled)
            Log.e("ARRIVAL", arrivalDate);
        Log.e("CHECKOUT", checkoutDate);
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
        Log.e("ARRIVAL", arrival.toString());
        Log.e("CHECKOUT", checkout.toString());
        Log.e("Formatted Arrival", dateFormat.format(arrival));
        Log.e("Formatted Checkout", dateFormat.format(checkout));
        Call<List<AccommodationDetails>> call = accommodationService.search(place, guests, dateFormat.format(arrival), dateFormat.format(checkout));
        call.enqueue(new Callback<List<AccommodationDetails>>() {
            @Override
            public void onResponse(Call<List<AccommodationDetails>> call, Response<List<AccommodationDetails>> response) {
                if (response.isSuccessful()) {
                    List<AccommodationDetails> accommodations = response.body();
                    Intent intent = new Intent(HomeScreenActivity.this, SearchedAccommodationsActivity.class);
                    intent.putExtra("username", loggedInUsername);
                    intent.putExtra("role", loggedInRole);
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
                Log.e("FAILURE!", t.getMessage());
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

    @Override
    public void onFilterReservationButtonClicked(String accName, String arrivalDate, String checkoutDate, ReservationStatusEnum status) {

    }

    public void performLoginAction() {
        Intent intent = new Intent(HomeScreenActivity.this, LogInScreenActivity.class);
        startActivity(intent);
    }

    public void performRegistrationAction() {
        Intent intent = new Intent(HomeScreenActivity.this, RegisterScreenActivity.class);
        startActivity(intent);
    }

    public void performAboutUsAction() {
    }

    public void performAccomodationAction() {
        Intent intent = new Intent(HomeScreenActivity.this, OwnersAccommodationActivity.class);
        startActivity(intent);
    }

    public void performMyAccountAction(@Header("Authorization") String authorizationHeader) {
        Intent intent = new Intent(HomeScreenActivity.this, AccountScreenActivity.class);
        startActivity(intent);
    }

    public void performNotificationSettingsAction(RoleEnum role, User user) {
        if (role.equals(RoleEnum.OWNER)) {
            Intent intent = new Intent(HomeScreenActivity.this, OwnerNotificationSettingsActivity.class);
            startActivity(intent);
        } else if (role.equals(RoleEnum.GUEST)) {
            Intent intent = new Intent(HomeScreenActivity.this, GuestNotificationSettingsActivity.class);
            startActivity(intent);
        } else {
            return;
        }
    }

    // Kod za dobavljanje JWT tokena
    private String getRoleFromToken() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        return preferences.getString("role", "");
    }

    private void performLogOutAction() {
        UserService userService = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
        Call<Void> call = userService.logout();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("LOGOUT", "LOGOOOUT");
                    loggedInRole = null;
                    Toast.makeText(HomeScreenActivity.this, "LOG OUT SUCCESSFULL", Toast.LENGTH_LONG);
                    TokenManager.setLoggedInUser(null);
                    Intent intent = new Intent(HomeScreenActivity.this, LogInScreenActivity.class);
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

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];

            // Check if the phone is tilted forward or backward
            if (Math.abs(x) > 3) {  // You might need to adjust this threshold
                // Scroll the ListView programmatically
                int firstVisibleItem = listView.getFirstVisiblePosition();
                int lastVisibleItem = listView.getLastVisiblePosition();

                if (x > 0) {
                    // Tilted forward, scroll up if the list is not at the top
                    if (firstVisibleItem > 0) {
                        listView.smoothScrollToPosition(firstVisibleItem - 1);
                    }
                } else {
                    // Tilted backward, scroll down if the list is not at the bottom
                    if (lastVisibleItem < listAdapter.getCount() - 1) {
                        listView.smoothScrollToPosition(lastVisibleItem + 1);
                    }
                }
            }

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 200) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = sensorEvent.values;
                float y = values[1];
                float z = values[2];

                float speed = Math.abs(y + z - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    ArrayList<Accommodation> newList = new ArrayList<>();
                    ascendingOrder = !ascendingOrder;
                    sortList(newList, Comparator.reverseOrder(), ascendingOrder);

                    accommodationsToShow.clear();
                    accommodationsToShow.addAll(newList);

                    listAdapter.notifyDataSetChanged();

                    Log.d("REZ", "shake detected w/ speed: " + speed);
                }

                last_y = y;
                last_z = z;
            }
        }
    }


//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            float x = sensorEvent.values[0];
//
//            // Check if the phone is tilted forward or backward
//            if (Math.abs(x) > 3) {  // You might need to adjust this threshold
//                // Scroll the ListView programmatically
//                int firstVisibleItem = listView.getFirstVisiblePosition();
//                int lastVisibleItem = listView.getLastVisiblePosition();
//
//                if (x > 0) {
//                    // Tilted forward, scroll up if the list is not at the top
//                    if (firstVisibleItem > 0) {
//                        listView.smoothScrollToPosition(firstVisibleItem - 1);
//                    }
//                } else {
//                    // Tilted backward, scroll down if the list is not at the bottom
//                    if (lastVisibleItem < listAdapter.getCount() - 1) {
//                        listView.smoothScrollToPosition(lastVisibleItem + 1);
//                    }
//                }
//            }
//
//            long curTime = System.currentTimeMillis();
//            if ((curTime - lastUpdate) > 200) {
//                long diffTime = (curTime - lastUpdate);
//                lastUpdate = curTime;
//
//                float speed = Math.abs(x - last_x) / diffTime * 10000;
//
//                if (speed > SHAKE_THRESHOLD) {
//                    // Scroll the ListView programmatically
//                    int firstVisibleItem = listView.getFirstVisiblePosition();
//                    int lastVisibleItem = listView.getLastVisiblePosition();
//
//                    if (ascendingOrder) {
//                        // Scroll up if the list is not at the top
//                        if (firstVisibleItem > 0) {
//                            listView.smoothScrollToPosition(firstVisibleItem - 1);
//                        }
//                    } else {
//                        // Scroll down if the list is not at the bottom
//                        if (lastVisibleItem < listAdapter.getCount() - 1) {
//                            listView.smoothScrollToPosition(lastVisibleItem + 1);
//                        }
//                    }
//
//                    Log.d("REZ", "shake detected w/ speed: " + speed);
//                }
//
//                last_x = x;
//            }
//        }
//
//
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            long curTime = System.currentTimeMillis();
//            if ((curTime - lastUpdate) > 200) {
//                long diffTime = (curTime - lastUpdate);
//                lastUpdate = curTime;
//
//                float[] values = sensorEvent.values;
//                float x = values[0];
//                float y = values[1];
//                float z = values[2];
//
//                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
//
//                if (speed > SHAKE_THRESHOLD) {
//                    ArrayList<Accommodation> newList = new ArrayList<>();
//                    ascendingOrder = !ascendingOrder;
//                    sortList(newList, Comparator.reverseOrder(), ascendingOrder);
//
//                    accommodationsToShow.clear();
//                    accommodationsToShow.addAll(newList);
//
//                    listAdapter.notifyDataSetChanged();
//
//
//                    Log.d("REZ", "shake detected w/ speed: " + speed);
//                }
//
//                last_x = x;
//                last_y = y;
//                last_z = z;
//            }
//        }
//    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.i("REZ_ACCELEROMETER", String.valueOf(accuracy));
        }
    }

    public void sortList(ArrayList<Accommodation> newList, Comparator<? super Date> keyComparator, boolean ascending) {
        newList.addAll(accommodationsToShow.stream()
                .sorted((a1, a2) -> {
                    Date startDate1 = getEarliestStartDate(a1.getPrices());
                    Date startDate2 = getEarliestStartDate(a2.getPrices());
                    // Sort in descending order based on start date
                    if (startDate1 == null && startDate2 == null) {
                        return 0; // Both are considered equal
                    } else if (startDate1 == null) {
                        return 1; // Null is considered greater than non-null
                    } else if (startDate2 == null) {
                        return -1; // Non-null is considered greater than null
                    }
                    return ascending ? startDate1.compareTo(startDate2) : startDate2.compareTo(startDate1);

                })
                .collect(Collectors.toList()));
    }

    private Date getEarliestStartDate(List<PriceCard> priceCards) {
        return priceCards.stream()
                .map(PriceCard::getTimeSlot)
                .map(TimeSlot::getStartDate)
                .min(Comparator.naturalOrder())
                .orElse(null);  //lista je prazna
    }

    private void callNotifications() {
        Call<List<Notification>> call3 = notificationService.getNotificationsByUserId(loggedInUsername);
        call3.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call3, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    List<Notification> notifications = response.body();
                    if(firstRun){
                        firstRun=false;
                        return;
                    }
                    if (notifications.size() != numOfNotifications) {
                        numOfNotifications = notifications.size();
                        for (Notification not : notifications) {
                            if (!not.read) {
                                if(!alreadyShownNewNot.contains(not.getId())){
                                    if(not.getType().equals("RESERVATION_CREATED")){
                                        if(TokenManager.getLoggedInUser().reservationRequestNotification){
                                            showNotification(not.getContent());
                                            alreadyShownNewNot.add(not.getId());
                                        }
                                    }else if(not.getType().equals("RESERVATION_CANCELLED")){
                                        if(TokenManager.getLoggedInUser().reservationCancellationNotification){
                                            showNotification(not.getContent());
                                            alreadyShownNewNot.add(not.getId());
                                        }
                                    }else if(not.getType().equals("RESERVATION_APPROVED") || not.getType().equals("RESERVATION_REJECTED")){
                                        if(TokenManager.getLoggedInUser().ownerRepliedToRequestNotification){
                                            showNotification(not.getContent());
                                            alreadyShownNewNot.add(not.getId());
                                        }
                                    }else if(not.getType().equals("CREATED_REVIEW")){
                                        if(TokenManager.getLoggedInUser().accommodationRatingNotification || TokenManager.getLoggedInUser().ownerRatingNotification){
                                            showNotification(not.getContent());
                                            alreadyShownNewNot.add(not.getId());
                                        }
                                    }
                                    //showNotification(not.getContent());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e("GREEEESKA", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void showNotification(String content) {
        Intent intent = new Intent(this, HomeScreenActivity.class);

        // Create a PendingIntent for the notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New Notification")
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Get the notification manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Notify with a unique ID
        int notificationId = (int) System.currentTimeMillis(); // Use a unique ID for each notification
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}