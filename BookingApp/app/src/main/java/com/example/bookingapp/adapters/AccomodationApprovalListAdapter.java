package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.model.Accommodation;

import java.util.ArrayList;

public class AccomodationApprovalListAdapter extends ArrayAdapter<Accommodation> {

    private ArrayList<Accommodation> aAccommodations;

    public AccomodationApprovalListAdapter(Context context, ArrayList<Accommodation> accommodations){
        super(context, R.layout.list_approval_accomodation, accommodations);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_approval_accomodation,
                    parent, false);
        }
        //LinearLayout accommodationCard = convertView.findViewById(R.id.accommodation_card_item);
        ImageView imageView = convertView.findViewById(R.id.listImage);
        ImageButton imageButtonApprove = convertView.findViewById(R.id.imageButtonApprove);
        ImageButton imageButtonReject = convertView.findViewById(R.id.imageButtonReject);

        TextView accommodationName = convertView.findViewById(R.id.listAccommodationName);
        //TextView accommodationDescription = convertView.findViewById(R.id.listAccommodationDesc);

        imageView.setImageResource(accommodation.getImage());
        accommodationName.setText(accommodation.getName());
        imageButtonApprove.setImageResource(R.drawable.ic_check_blue);
        imageButtonReject.setImageResource(R.drawable.ic_reject_blue);
        //accommodationDescription.setText(accommodation.getDescription());

        return convertView;
    }
}
