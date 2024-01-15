package com.example.bookingapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.HomeScreenActivity;
import com.example.bookingapp.model.Accommodation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private List<Accommodation> aAccommodations;
    private Context context;
    public AccommodationListAdapter(Context context, List<Accommodation> accommodations){
        super(context, R.layout.list_accommodation, accommodations);
        this.context=context;
        aAccommodations = accommodations;
    }

    @Override
    public int getCount() {
        return aAccommodations.size();
    }

    @Nullable
    @Override
    public Accommodation getItem(int position) {
        return aAccommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Accommodation accommodation = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_accommodation,
                    parent, false);
        }
        if(accommodation.images.size()>0){
            String imagesrc=accommodation.images.get(0).split("/")[5];
            ImageView imageView = convertView.findViewById(R.id.listImage);
            handleImage(imagesrc,imageView);

        }else{//default slika
            ImageView imageView = convertView.findViewById(R.id.listImage);
            imageView.setImageResource(R.drawable.accommodation1);
        }


        TextView accommodationName = convertView.findViewById(R.id.listAccommodationName);
        //TextView accommodationDescription = convertView.findViewById(R.id.listAccommodationDesc);

        //imageView.setImageResource(accommodation.getImage());
        accommodationName.setText("Name :" +accommodation.getName());
        //accommodationDescription.setText(accommodation.getDescription());

        TextView accommodationLocation = convertView.findViewById(R.id.listAccommodationLocation);
        if(accommodation.location!=null) {
            accommodationLocation.setText("Location : " + accommodation.location.address + ", " + accommodation.location.city);
        }
        TextView accommodationAssets=convertView.findViewById(R.id.listAccommodationAssets);

        String assetsString="";
        for(String s:accommodation.assets){
            assetsString+=" "+s;
        }

        accommodationAssets.setText("Assets : "+assetsString);

        return convertView;
    }

    public void updateData(List<Accommodation> newData) {
        aAccommodations.clear();
        aAccommodations.addAll(newData);
        notifyDataSetChanged();
    }
    public void handleImage(String imagesrc, ImageView imageView){
        if(imagesrc.equals("accommodation1.jpg")){
            imageView.setImageResource(R.drawable.accommodation1);
        }else if(imagesrc.equals("accommodation2.jpg")){
            imageView.setImageResource(R.drawable.accommodation2);
        }else if(imagesrc.equals("accommodation3.jpg")){
            imageView.setImageResource(R.drawable.accommodation3);
        }else if(imagesrc.equals("accommodation4.jpg")){
            imageView.setImageResource(R.drawable.accommodation4);
        }else if(imagesrc.equals("accommodation5.jpg")){
            imageView.setImageResource(R.drawable.accommodation5);
        }else if(imagesrc.equals("accommodation6.jpg")){
            imageView.setImageResource(R.drawable.accommodation6);
        }else if(imagesrc.equals("accommodation8.jpg")){
            imageView.setImageResource(R.drawable.accommodation8);
        }else if(imagesrc.equals("accommodation9.jpg")){
            imageView.setImageResource(R.drawable.accommodation9);
        }else if(imagesrc.equals("accommodation11.jpg")){
            imageView.setImageResource(R.drawable.accommodation11);
        }else if(imagesrc.equals("accommodation13.jpg")){
            imageView.setImageResource(R.drawable.accommodation13);
        }else if(imagesrc.equals("accommodation14.jpg")){
            imageView.setImageResource(R.drawable.accommodation14);
        }
    }
}
