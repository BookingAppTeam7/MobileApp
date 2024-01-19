package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookingapp.BuildConfig;
import com.example.bookingapp.R;
import com.example.bookingapp.adapters.ReviewListAdapter;
import com.example.bookingapp.databinding.ActivityDetailedBinding;
import com.example.bookingapp.fragments.accommodations.AvailabilityBottomSheetDialogFragment;
import com.example.bookingapp.fragments.accommodations.ReservationBottomSheetFragment;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.NotificationPostDTO;
import com.example.bookingapp.model.DTOs.ReviewGetDTO;
import com.example.bookingapp.model.Notification;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.TimeSlot;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.model.enums.ReviewStatusEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.NotificationService;
import com.example.bookingapp.services.ReservationService;
import com.example.bookingapp.services.ReviewService;
import com.example.bookingapp.services.UserService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    public AccommodationService accommodationService=retrofit.create(AccommodationService.class);
    public UserService userService=retrofit.create(UserService.class);
    public ReviewService reviewService=retrofit.create(ReviewService.class);
    public NotificationService notificationService=retrofit.create(NotificationService.class);
    ActivityDetailedBinding binding;
    private String location;
    private double locationX;
    private double locationY;
    private double price;
    public String reservationConfirmation;
    private List<PriceCard> priceList;
    ReviewListAdapter listAdapter;
    String loggedInUsername;
    String loggedInRole;
    public Long accommodationId;

    public double reservationPrice;
    public PriceTypeEnum reservationPriceType;
    public double averageGradeOwner;
    public String favouriteAccommodations;
    public String firstImage="";
    public ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button reservation=binding.reservation;
        favouriteAccommodations="";
        Intent intent=this.getIntent();
        if(intent!=null){
            loggedInUsername=intent.getStringExtra("username");
            loggedInRole=intent.getStringExtra("role");
            accommodationId=intent.getLongExtra("accommodationId",0);
            String name=intent.getStringExtra("name");
            String description=intent.getStringExtra("description");
            //int image=intent.getIntExtra("image",R.drawable.accommodation1);
            location=intent.getStringExtra("location");
            locationX=intent.getDoubleExtra("locationX",0.0);
            locationY=intent.getDoubleExtra("locationY",0.0);
            price=intent.getDoubleExtra("price",0.0);
            int minGuests=intent.getIntExtra("minGuests",0);
            int maxGuests=intent.getIntExtra("maxGuests",0);
            String type=intent.getStringExtra("type");
            String cancel=intent.getStringExtra("cancelDeadline");
            reservationConfirmation=intent.getStringExtra("reservationConfirmation");
            favouriteAccommodations=intent.getStringExtra("favouriteAccommodations");
            firstImage=intent.getStringExtra("image");
            imageView=binding.detailImage;
            if(firstImage!=null){
                if(!firstImage.isEmpty()){
                    String imagesrc=firstImage.split("/")[5];
                    setImageFromPath(imagesrc);
                }
            }else{
                binding.detailImage.setImageResource(R.drawable.accommodation1);
            }
            Log.e("RESERVATION CONFIRMATION",reservationConfirmation);
            binding.detailName.setText(name);
            binding.detailDescription.setText(description);
            //binding.detailImage.setImageResource(image);
            binding.minMaxGuests.setText("Guests: "+String.valueOf(minGuests)+" - "+String.valueOf(maxGuests)+"    Type:"+type);
            binding.cancelDeadline.setText("Cancel deadline (in days):"+cancel);
           // binding.averageGradeOwner.setText("Owners average grade: " + String.valueOf(calculateOwnersAverageGrade(accommodationId)));
            //List<Review> reviewsList = (ArrayList<Review>) getIntent().getSerializableExtra("reviewsList");

            List<String> assetsList=(ArrayList<String>) getIntent().getSerializableExtra("assets");
            setTextRateOwner(accommodationId);
            setTextRateAccommodation(accommodationId);
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
            Button addToFavourites=binding.addToFavourites;
            Button removeFromFavourites=binding.removeFromFavourites;
            addToFavourites.setVisibility(View.INVISIBLE);
            removeFromFavourites.setVisibility(View.INVISIBLE);
            if(loggedInUsername!=null){
                if(loggedInRole.equals("GUEST")){
                    if(isInGuestFavourite(accommodationId,favouriteAccommodations)){
                        addToFavourites.setVisibility(View.INVISIBLE);
                        removeFromFavourites.setVisibility(View.VISIBLE);
                    }else{
                        addToFavourites.setVisibility(View.VISIBLE);
                        removeFromFavourites.setVisibility(View.INVISIBLE);
                    }
                }
            }


            List<ReviewGetDTO> reviewGetDTOS=new ArrayList<>();
            binding.owner.setText(getIntent().getStringExtra("ownerId"));

            Call call = reviewService.findByAccommodationId(getIntent().getLongExtra("accommodationId",0L));
            call.enqueue(new Callback<List<ReviewGetDTO>>() {
                @Override
                public void onResponse(Call<List<ReviewGetDTO>> call, Response<List<ReviewGetDTO>> response) {
                    if (response.isSuccessful()) {

                        for(ReviewGetDTO r:response.body()){
                            if(r.getStatus()== ReviewStatusEnum.APPROVED) {
                                reviewGetDTOS.add(r);
                            }
                        }

                        listAdapter = new ReviewListAdapter(DetailedActivity.this, reviewGetDTOS);
                        binding.listReviewView.setAdapter(listAdapter);
                        binding.listReviewView.setClickable(false);
                    } else {
                        // Handle error
                        Log.e("GRESKA",String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<List<ReviewGetDTO>> call, Throwable t) {
                    Log.e("Error : ",t.getMessage());
                    t.printStackTrace();
                }
            });


//            listAdapter = new ReviewListAdapter(DetailedActivity.this, reviewGetDTOS);
//            binding.listReviewView.setAdapter(listAdapter);
//            binding.listReviewView.setClickable(false);
        }

        binding.btnOwnersReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, UserRatingsRequestsActivity.class);
                intent.putExtra("username",getIntent().getStringExtra("ownerId"));
                startActivity(intent);
            }
        });

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
        binding.addToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> addToFavouritesCall = userService.addToFavourites(loggedInUsername, accommodationId);
                addToFavouritesCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DetailedActivity.this, "Added to favourites!", Toast.LENGTH_SHORT).show();
                            binding.addToFavourites.setVisibility(View.INVISIBLE);
                            binding.removeFromFavourites.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(DetailedActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(DetailedActivity.this, "Error in back-end", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.removeFromFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> addToFavouritesCall = userService.removeFromFavourites(loggedInUsername, accommodationId);
                addToFavouritesCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DetailedActivity.this, "Removed from favourites!", Toast.LENGTH_SHORT).show();
                            binding.addToFavourites.setVisibility(View.VISIBLE);
                            binding.removeFromFavourites.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(DetailedActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(DetailedActivity.this, "Error in back-end", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setTextRateOwner(Long accommodationId) {
        Call<Accommodation> call1 = accommodationService.findById(accommodationId);

        call1.enqueue(new Callback<Accommodation>(){
           double  averageGradeOwner = 0.0;
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                Accommodation accommodation = response.body();
                String ownerId=accommodation.ownerId;
                Call<List<ReviewGetDTO>> call2=reviewService.findByOwnerId(ownerId);
                call2.enqueue(new Callback<List<ReviewGetDTO>>() {
                    @Override
                    public void onResponse(Call<List<ReviewGetDTO>> call, Response<List<ReviewGetDTO>> response) {
                        double averageGrade=0.0;
                        int numOfReviews=0;
                        int sum=0;
                        List<ReviewGetDTO> allReviews=response.body();
                        if(allReviews!=null){
                            for(ReviewGetDTO review:allReviews){
                                if(review.status.equals(ReviewStatusEnum.APPROVED)){
                                    sum+=review.grade;
                                    numOfReviews+=1;
                                }
                            }
                        }
                        if(numOfReviews==0){
                            averageGrade=0.0;
                        }else {
                            averageGrade=sum/numOfReviews;
                        }


                        averageGradeOwner=averageGrade;
                        binding.averageGradeOwner.setText("Owners average grade: " + String.valueOf(averageGradeOwner));

                    }

                    @Override
                    public void onFailure(Call<List<ReviewGetDTO>> call, Throwable t) {
                        averageGradeOwner=0.0;
                        binding.averageGradeOwner.setText("Owners average grade: " + String.valueOf(averageGradeOwner));
                    }
                });

            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {

            }
        });

    }

    private void setTextRateAccommodation(Long accommodationId) {



                Call<List<ReviewGetDTO>> call2=reviewService.findByAccommodationId(accommodationId);
                call2.enqueue(new Callback<List<ReviewGetDTO>>() {
                    @Override
                    public void onResponse(Call<List<ReviewGetDTO>> call, Response<List<ReviewGetDTO>> response) {
                        double averageGradeAccommodation=0.0;
                        int numOfReviews=0;
                        int sum=0;
                        List<ReviewGetDTO> allReviews=response.body();
                        if(allReviews!=null){
                            for(ReviewGetDTO review:allReviews){
                                if(review.status.equals(ReviewStatusEnum.APPROVED)){
                                    sum+=review.grade;
                                    numOfReviews+=1;
                                }
                            }
                        }
                        if(numOfReviews==0){
                            averageGradeAccommodation=0.0;
                        }else {
                            averageGradeAccommodation=sum/numOfReviews;

                        }

                        binding.averageGradeAccommodation.setText("Accommodation average grade: " + String.valueOf(averageGradeAccommodation));

                    }

                    @Override
                    public void onFailure(Call<List<ReviewGetDTO>> call, Throwable t) {
                        averageGradeOwner=0.0;
                        binding.averageGradeAccommodation.setText("Accommodation average grade: " + String.valueOf(binding.averageGradeAccommodation));
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
                    .append(p.type+"\n");
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

        Call<Accommodation> call1 = accommodationService.findById(accommodationId);

        call1.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call1, Response<Accommodation> response) {
                if (response.isSuccessful()) {
                    Accommodation accommodation = response.body();
                    for(PriceCard pc: accommodation.prices){
                        if(isWithinTimeSlot(arrival,checkout,pc.timeSlot)){
                            Log.e("JESTE U TIME SLOTU","JESTEEEEEE");
                            reservationPrice=pc.price;
                            reservationPriceType=pc.type;
                        }
                    }

                    Log.e("PRICEEEEEEEEE",String.valueOf(reservationPrice));//NE VALJA - 0.0
                    Log.e("PRICETYPEEEEEEE",String.valueOf(reservationPriceType));//NE VALJA - null
                    // Manually create a JSON object with the desired format
                    String requestBody = String.format(
                            "{\"accommodationId\": %d, \"userId\": \"%s\", \"timeSlot\": {\"id\": %d, \"startDate\": \"%s\", \"endDate\": \"%s\"}, \"numberOfGuests\": %d, \"price\":\"%s\", \"priceType\":\"%s\"}",
                            accommodationId, loggedInUsername, 999L, formattedArrivalDate, formattedCheckoutDate, guests, String.valueOf(reservationPrice), String.valueOf(reservationPriceType));
                    Log.e("RESERVATION POST", requestBody);

                    // Make the API call with the manually created JSON object
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);
                    Call<Reservation> createReservationCall = reservationService.createReservation(body);

                    createReservationCall.enqueue(new Callback<Reservation>() {
                        @Override
                        public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                            if (response.isSuccessful()) {
                                Log.e("USPESNO NAPRAVIO REZERVACIJU", "POGLEDAJ BAZU");
                                Toast.makeText(DetailedActivity.this, "Successfully created reservation!", Toast.LENGTH_SHORT).show();
                                if (reservationConfirmation.equals("AUTOMATIC")) {
                                    Long reservationId = response.body().getId();
                                    Log.e("RESERVATION ID", reservationId.toString());
                                    // Confirm the reservation
                                    confirmReservation(reservationId);
                                }
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
            public void onFailure(Call<Accommodation> call1, Throwable t) {
                Log.e("Failure", t.getMessage());
                t.printStackTrace();
            }
        });


    }

    @Override
    public void onFilterReservationButtonClicked(String accName, String arrivalDate, String checkoutDate, ReservationStatusEnum status) {

    }

    private void confirmReservation(Long reservationId) {
        Call<Void> confirmCall = reservationService.confirmReservation(reservationId);

        confirmCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailedActivity.this,"Reservation confirmed!",Toast.LENGTH_SHORT).show();
                    Log.e("Reservation Confirmed", "Reservation confirmed successfully");
                    // Additional handling if needed
                } else {
                    Log.e("Confirmation Error", "Error: " + response.message());
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
                    Toast.makeText(DetailedActivity.this, "Error confirming reservation: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("Confirmation Failure", "Error: " + t.getMessage());
            }
        });
    }
    boolean isWithinTimeSlot(Date arrival, Date checkout, TimeSlot timeSlot){
        Log.e("ARRIVALFFF",arrival.toString());
        Log.e("CHECKOUTFFF",checkout.toString());
        Log.e("TIMESLOTARRIVALFFF",timeSlot.getStartDate().toString());
        Log.e("TIMESLOTCHECKOUTGFFFFF",timeSlot.getEndDate().toString());
        return !arrival.before(timeSlot.getStartDate()) && !checkout.after(timeSlot.getEndDate());
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
    public void setImageFromPath(String imagePath){
        Picasso.get().load("http://"+ BuildConfig.IP_ADDR+":8084/files/download/"+imagePath).into(imageView);
    }

}
