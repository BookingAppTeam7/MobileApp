package com.example.bookingapp.fragments.accommodations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AvailabilityBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_AVAILABLE_DATES = "availableDates";

    public static AvailabilityBottomSheetDialogFragment newInstance(String availableDates) {
        AvailabilityBottomSheetDialogFragment fragment = new AvailabilityBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_AVAILABLE_DATES, availableDates);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.bottom_sheet_availability,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView datesTextView = view.findViewById(R.id.dates);

        if (getArguments() != null) {
            String availableDates = getArguments().getString(ARG_AVAILABLE_DATES);
            datesTextView.setText("Available dates:\n"+availableDates);
        }
    }
}
