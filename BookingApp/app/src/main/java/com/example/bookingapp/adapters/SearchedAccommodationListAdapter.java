package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationDetails;

import java.util.List;

public class SearchedAccommodationListAdapter extends ArrayAdapter<AccommodationDetails> {
    private List<AccommodationDetails> aAccommodations;
    public SearchedAccommodationListAdapter(Context context, List<AccommodationDetails> accommodations){
        super(context, R.layout.list_detail_accommodation,accommodations);
        aAccommodations=accommodations;
    }

    @Override
    public int getCount(){return aAccommodations.size();}

    @Nullable
    @Override
    public AccommodationDetails getItem(int position) {
        return aAccommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        AccommodationDetails accommodationDetails=getItem(position);
        Accommodation accommodation=getItem(position).getAccommodation();
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_detail_accommodation,
                    parent,false);
        }
        ImageView imageView=convertView.findViewById(R.id.listImage);
        TextView accommodationName=convertView.findViewById(R.id.listAccommodationName);
        accommodationName.setText(accommodation.getName());
        TextView totalPrice=convertView.findViewById(R.id.totalPrice);
        totalPrice.setText("Total price:"+String.valueOf(accommodationDetails.getTotalPrice())+"$");
        TextView unitPrice=convertView.findViewById(R.id.unitPrice);
        unitPrice.setText("Unit price:"+String.valueOf(accommodationDetails.getUnitPrice())+"$");
        TextView averageRating=convertView.findViewById(R.id.averageRating);
        averageRating.setText("Average rating:"+String.valueOf(accommodationDetails.getAverageRating()));

        return convertView;
    }
}
