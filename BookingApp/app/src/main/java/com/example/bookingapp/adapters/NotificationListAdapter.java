package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.model.Notification;

import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<Notification> {
    private List<Notification> aNotifications;
    public NotificationListAdapter(Context context, List<Notification> notifications){
        super(context, R.layout.list_notification,notifications);
        aNotifications=notifications;
    }
    @Override
    public int getCount(){
        return aNotifications.size();
    }
    @Nullable
    @Override
    public Notification getItem(int position){
        return aNotifications.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Notification notification=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_notification,
                    parent,false);
        }
        TextView notificationUser=convertView.findViewById(R.id.listNotificationUser);
        TextView notificationContent=convertView.findViewById(R.id.listNotificationContent);
        TextView notificationDate=convertView.findViewById(R.id.listNotificationDate);

        notificationUser.setText("User: "+notification.userId);
        notificationContent.setText("Content: "+notification.content);
        notificationDate.setText("Date: "+notification.dateTime);


        return convertView;
    }

    public void updateDate(List<Notification> newData){
        aNotifications.clear();
        aNotifications.addAll(newData);
        notifyDataSetChanged();
    }
}
