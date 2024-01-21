package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
                    binding.listviewNotification.setClickable(true);

                    binding.listviewNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(!notifications.get(position).read){
                                Call<Void> call2= notificationService.readNotification(notifications.get(position).id);
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        notifications.get(position).read = true;
                                        listAdapter.notifyDataSetChanged();
                                        Toast.makeText(NotificationsActivity.this, "Notification read!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e("GREEEESKA",t.getMessage());
                                        t.printStackTrace();
                                    }
                                });
                            }else{
                                Toast.makeText(NotificationsActivity.this, "Notification is already read!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
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