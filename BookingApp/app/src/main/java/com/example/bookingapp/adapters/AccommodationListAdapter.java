package com.example.bookingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.model.Accommodation;

import java.util.ArrayList;
import java.util.List;

public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private List<Accommodation> aAccommodations;

    public AccommodationListAdapter(Context context, List<Accommodation> accommodations){
        super(context, R.layout.list_accommodation, accommodations);
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
        //LinearLayout accommodationCard = convertView.findViewById(R.id.accommodation_card_item);
        ImageView imageView = convertView.findViewById(R.id.listImage);
        TextView accommodationName = convertView.findViewById(R.id.listAccommodationName);
        //TextView accommodationDescription = convertView.findViewById(R.id.listAccommodationDesc);

        //imageView.setImageResource(accommodation.getImage());
        accommodationName.setText(accommodation.getName());
        //accommodationDescription.setText(accommodation.getDescription());

        return convertView;
    }

    public void updateData(List<Accommodation> newData) {
        aAccommodations.clear();
        aAccommodations.addAll(newData);
        notifyDataSetChanged();
    }
}
