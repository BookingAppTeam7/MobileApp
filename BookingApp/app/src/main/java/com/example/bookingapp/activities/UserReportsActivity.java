package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;


import com.example.bookingapp.R;
import com.example.bookingapp.adapters.ReportListAdapter;
import com.example.bookingapp.adapters.UserListAdapter;
import com.example.bookingapp.databinding.ActivityUserReportsBinding;
import com.example.bookingapp.databinding.ActivityUsersReviewBinding;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.UserReport;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.UserReportService;
import com.example.bookingapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserReportsActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

    public UserReportService reportService = retrofit.create(UserReportService.class);

    public ReportListAdapter listAdapter;
    public ArrayList<UserReport> reportsToShow= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserReportsBinding binding = ActivityUserReportsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme


        Call call = reportService.findByUser(getIntent().getStringExtra("username"));
        call.enqueue(new Callback<List<UserReport>>() {
            @Override
            public void onResponse(Call<List<UserReport>> call, Response<List<UserReport>> response) {
                if (response.isSuccessful()) {

                    for(UserReport u:response.body()){
                        reportsToShow.add(u);
                    }

                    listAdapter = new ReportListAdapter(UserReportsActivity.this, reportsToShow);
                    binding.listview.setAdapter(listAdapter);
                    binding.listview.setClickable(true);

                    binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            UserReport selectedReport = (UserReport) parent.getItemAtPosition(position);

                        }
                    });
                } else {
                    // Handle error
                    Log.e("GRESKA",String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<UserReport>> call, Throwable t) {
                Log.e("Error : ",t.getMessage());
                t.printStackTrace();
            }
        });

    }
}