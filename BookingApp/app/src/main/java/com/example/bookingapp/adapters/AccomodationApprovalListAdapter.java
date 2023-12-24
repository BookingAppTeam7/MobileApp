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
import com.example.bookingapp.model.AccommodationRequest;

import java.util.ArrayList;

public class AccomodationApprovalListAdapter extends ArrayAdapter<AccommodationRequest> {

    private ArrayList<AccommodationRequest> aRequests;

    public AccomodationApprovalListAdapter(Context context, ArrayList<AccommodationRequest> requests){
        super(context, R.layout.list_approval_accomodation, requests);
        aRequests = requests;
    }

    @Override
    public int getCount() {
        return aRequests.size();
    }

    @Nullable
    @Override
    public AccommodationRequest getItem(int position) {
        return aRequests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationRequest request = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_approval_accomodation,
                    parent, false);
        }

        ImageButton imageButtonApprove = convertView.findViewById(R.id.imageButtonApprove);
        ImageButton imageButtonReject = convertView.findViewById(R.id.imageButtonReject);

        TextView requestId = convertView.findViewById(R.id.listRequestId);
        TextView requestStatus = convertView.findViewById(R.id.listStatus);

        requestId.setText(String.valueOf(request.getId()));
        requestStatus.setText(String.valueOf(request.getRequestStatus()));
        imageButtonApprove.setImageResource(R.drawable.ic_check_blue);
        imageButtonReject.setImageResource(R.drawable.ic_reject_blue);

        return convertView;
    }
}
