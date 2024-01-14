package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.NotificationListAdapter;
import com.example.bookingapp.databinding.ActivityNotificationsBinding;
import com.example.bookingapp.model.Notification;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.NotificationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationsActivity extends AppCompatActivity {
    public Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
    public NotificationService notificationService=retrofit.create(NotificationService.class);
    ActivityNotificationsBinding binding;
    NotificationListAdapter listAdapter;
    private ListView listView;
    public String username="";
    public List<Notification> notifications=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        username=intent.getStringExtra("username");

        listView=binding.listviewNotification;

        Call<List<Notification>> call = notificationService.getNotificationsByUserId(username);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    notifications = response.body();
                    Log.e("body",response.body().toString());
                    for(Notification n:notifications){
                        Log.e("NOTIFICATION",n.toString());
                    }
                    listAdapter=new NotificationListAdapter(NotificationsActivity.this,notifications);
                    binding.listviewNotification.setAdapter(listAdapter);
                    binding.listviewNotification.setClickable(false);
                } else {
                    Log.e("Response not successful","FAIL RESPONSE");
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e("GREEEESKA",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}