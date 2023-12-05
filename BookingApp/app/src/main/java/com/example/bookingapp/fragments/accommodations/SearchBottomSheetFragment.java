package com.example.bookingapp.fragments.accommodations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SearchBottomSheetFragment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottom_sheet_search, container, false);
        Button btnSearch = view.findViewById(R.id.btnSearch1);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place="";
                int guests=0;
                // Get the values from your views (placeInput, editTextGuests, etc.)
                TextInputLayout placeInputLayout = view.findViewById(R.id.placeInput);
                TextInputEditText editTextPlace = placeInputLayout.findViewById(R.id.editTextPlace);
                place = editTextPlace.getText().toString();

                TextInputLayout guestsInputLayout = view.findViewById(R.id.guestsInput);
                TextInputEditText editTextGuests = guestsInputLayout.findViewById(R.id.editTextGuests);
                guests = Integer.parseInt(editTextGuests.getText().toString());



                String arrivalDate = "";
                String checkoutDate = "";
                CalendarView calendar1 = view.findViewById(R.id.calendar1);
                CalendarView calendar2 = view.findViewById(R.id.calendar2);
                long selectedDateMillis1 = calendar1.getDate();
                long selectedDateMillis2 = calendar2.getDate();

                // Convert millis to formatted date strings using a SimpleDateFormat
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                arrivalDate = sdf.format(new Date(selectedDateMillis1));
                checkoutDate = sdf.format(new Date(selectedDateMillis2));

                // Pass the data back to the activity
                if (mListener != null) {
                    mListener.onSearchButtonClicked(place, guests, arrivalDate, checkoutDate);
                }

                // Dismiss the bottom sheet
                dismiss();
            }
        });
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

}
