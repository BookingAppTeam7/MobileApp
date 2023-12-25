package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccomodationApprovalListAdapter;
import com.example.bookingapp.databinding.ActivityAccomodationApprovalBinding;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationRequest;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.ReviewEnum;
import com.example.bookingapp.model.TimeSlot;
import com.example.bookingapp.model.enums.AccommodationRequestStatus;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationRequestService;
import com.example.bookingapp.services.AccommodationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccomodationApprovalActivity extends AppCompatActivity {

    ActivityAccomodationApprovalBinding binding;
    AccomodationApprovalListAdapter listAdapter;
    public ArrayList<AccommodationRequest> requests = new ArrayList<AccommodationRequest>();
    //Accommodation accommodation;

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationRequestService requestsService = retrofit.create(AccommodationRequestService.class);

    public AccommodationService accommodationService = retrofit.create(AccommodationService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccomodationApprovalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //binding.listview.setClickable(true);

        Call<List<AccommodationRequest>> call1 = requestsService.findByStatus(AccommodationRequestStatus.PENDING_CREATED,AccommodationRequestStatus.PENDING_EDITED);

        call1.enqueue(new Callback<List<AccommodationRequest>>() {
            @Override
            public void onResponse(Call<List<AccommodationRequest>> call1, Response<List<AccommodationRequest>> response1) {
                if (response1.isSuccessful()) {
                    List<AccommodationRequest> accommodationRequests = response1.body();
                    AccomodationApprovalActivity.this.requests= (ArrayList<AccommodationRequest>) accommodationRequests;
                    AccomodationApprovalActivity.this.listAdapter = new AccomodationApprovalListAdapter(AccomodationApprovalActivity.this, requests);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);
                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            AccommodationRequest request=requests.get(position);
                            Long accommodationId=Long.valueOf(request.unapprovedAccommodationId);
                            Toast.makeText(AccomodationApprovalActivity.this,"USAO",Toast.LENGTH_LONG).show();

                            Call<Accommodation> call2 = accommodationService.findById(accommodationId);

                            call2.enqueue(new Callback<Accommodation>() {
                                @Override
                                public void onResponse(Call<Accommodation> call2, Response<Accommodation> response2) {
                                    if (response2.isSuccessful()) {
                                        Accommodation accommodation = response2.body();
                                        Intent intent = new Intent(AccomodationApprovalActivity.this, DetailedActivity.class);
                                        intent.putExtra("name",accommodation.getName());
                                        intent.putExtra("description", accommodation.getDescription());
                                        intent.putExtra("image", "putanja");
                                        intent.putExtra("location",accommodation.getLocation().address+", "+accommodation.getLocation().city);
                                        intent.putExtra("locationX",accommodation.getLocation().x);
                                        intent.putExtra("locationY",accommodation.getLocation().y);
                                        intent.putExtra("reviewsList",new ArrayList<>(accommodation.getReviews()));
                                        intent.putExtra("assets",new ArrayList<>(accommodation.getAssets()));
                                        intent.putExtra("priceList",new ArrayList<>(accommodation.getPrices()));
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Accommodation> call2, Throwable t) {
                                    // Obrada greške prilikom poziva
                                    t.printStackTrace();
                                }
                            });

                        }
                    });



                } else {
                    //Log.e("AccomodationApprovalActivity", "Greška: " + response.code());
                    System.out.println("Greška: " + response1.code());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationRequest>> call1, Throwable t) {
                // Obrada greške prilikom poziva
                Log.e("AccomodationApprovalActivity", "Greška: ");
                t.printStackTrace();
            }
        });

       // binding.listview.setClickable(true);



//        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                AccommodationRequest request=requests.get(position);
//                Long accommodationId=Long.valueOf(request.unapprovedAccommodationId);
//
//                Call<Accommodation> call = accommodationService.findById(accommodationId);
//
//                call.enqueue(new Callback<Accommodation>() {
//                    @Override
//                    public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
//                        if (response.isSuccessful()) {
//                            Accommodation accommodation = response.body();
//                            Intent intent = new Intent(AccomodationApprovalActivity.this, DetailedActivity.class);
//                            intent.putExtra("name",accommodation.getName());
//                            intent.putExtra("description", accommodation.getDescription());
//                            intent.putExtra("image", accommodation.getImages().get(0));
//                            intent.putExtra("location",accommodation.getLocation().address+", "+accommodation.getLocation().city);
//                            intent.putExtra("locationX",accommodation.getLocation().x);
//                            intent.putExtra("locationY",accommodation.getLocation().y);
//                            intent.putExtra("reviewsList",new ArrayList<>(accommodation.getReviews()));
//                            intent.putExtra("assets",new ArrayList<>(accommodation.getAssets()));
//                            intent.putExtra("priceList",new ArrayList<>(accommodation.getPrices()));
//                            startActivity(intent);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Accommodation> call, Throwable t) {
//                        // Obrada greške prilikom poziva
//                        t.printStackTrace();
//                    }
//                });
//
//            }
//        });

    }
}
