package com.example.bookingapp.fragments.accommodations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FilterReservationBottomSheetFragment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private String day1;
    private String month1;
    private String year1;
    private String day2;
    private String month2;
    private String year2;
    private RadioButton radioButtonPending, radioButtonCancelled, radioButtonApproved;
    public static FilterReservationBottomSheetFragment newInstance(){
        FilterReservationBottomSheetFragment fragment=new FilterReservationBottomSheetFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottom_sheet_filter_reservation, container, false);
        Button btnReservation = view.findViewById(R.id.filterReservations);
        CalendarView calendar1 = view.findViewById(R.id.calendar1);
        CalendarView calendar2 = view.findViewById(R.id.calendar2);
        radioButtonApproved=view.findViewById(R.id.radioButtonApproved);
        radioButtonCancelled=view.findViewById(R.id.radioButtonCancelled);
        radioButtonPending=view.findViewById(R.id.radioButtonPending);

        calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                if(dayOfMonth<10){
                    day1="0"+String.valueOf(dayOfMonth);
                }else{
                    day1 = String.valueOf(dayOfMonth);
                }
                if(month+1<10){
                    month1="0"+String.valueOf(month+1);
                }else{
                    month1 = String.valueOf(month+1);
                }
                year1 = String.valueOf(year);

            }
        });

        calendar2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                if(dayOfMonth<10){
                    day2="0"+String.valueOf(dayOfMonth);
                }else{
                    day2 = String.valueOf(dayOfMonth);
                }
                if(month+1<10){
                    month2="0"+String.valueOf(month+1);
                }else{
                    month2 = String.valueOf(month+1);
                }
                year2 = String.valueOf(year);

            }
        });
        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accName="";
                // Get the values from your views (placeInput, editTextGuests, etc.)
                ReservationStatusEnum selectedType=getSelectedType();
                TextInputLayout guestsInputLayout = view.findViewById(R.id.accNameInput);
                TextInputEditText editTextGuests = guestsInputLayout.findViewById(R.id.editTextAccName);
                accName = editTextGuests.getText().toString();

                String arrivalDate = year1+"-"+month1+"-"+day1;
                String checkoutDate = year2+"-"+month2+"-"+day2;
                // Pass the data back to the activity
                if (mListener != null) {
                    mListener.onFilterReservationButtonClicked(accName, arrivalDate, checkoutDate, selectedType);
                }

                // Dismiss the bottom sheet
                dismiss();
            }
        });
        return view;
    }
    private ReservationStatusEnum getSelectedType() {
        if (radioButtonPending.isChecked()) {
            return ReservationStatusEnum.PENDING;
        } else if (radioButtonCancelled.isChecked()) {
            return ReservationStatusEnum.CANCELLED;
        } else if (radioButtonApproved.isChecked()) {
            return ReservationStatusEnum.APPROVED;
        } else {
            return null;
        }
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
