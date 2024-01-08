package com.example.bookingapp.adapters;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.model.AccommodationRequest;
import com.example.bookingapp.model.Reservation;

import java.util.ArrayList;

public class PendingReservationsListAdapter extends ArrayAdapter<Reservation> {

    private ArrayList<Reservation> aReservations;

    public PendingReservationsListAdapter(Context context, ArrayList<Reservation> reservations){
        super(context, R.layout.list_reservation_request, reservations);
        aReservations = reservations;
    }

    @Override
    public int getCount() {
        return aReservations.size();
    }

    @Nullable
    @Override
    public Reservation getItem(int position) {
        return aReservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation request = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_reservation_request,
                    parent, false);
        }


        TextView requestStatus = convertView.findViewById(R.id.listStatus);
        TextView guest = convertView.findViewById(R.id.listGuestUsername);
        TextView startDate=convertView.findViewById(R.id.listStartDate);
        TextView endDate=convertView.findViewById(R.id.listEndDate);
        TextView numOfGuests=convertView.findViewById(R.id.listNumberOfGuests);

        requestStatus.setText("Status of request : "+String.valueOf(request.getStatus()));
        guest.setText("Guest : "+String.valueOf(request.getUser().username));
        startDate.setText("Start Date : "+String.valueOf(request.getTimeSlot().getStartDate()));
        endDate.setText("End Date : "+String.valueOf(request.getTimeSlot().getEndDate()));
        numOfGuests.setText("Number of guests : "+String.valueOf(request.getNumberOfGuests()));

        return convertView;
    }
}
