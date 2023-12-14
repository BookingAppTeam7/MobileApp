package com.example.bookingapp.fragments.accommodations;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.CreateAccommodationActivity;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PriceCardFragment extends DialogFragment {

    private Calendar myCalendar = Calendar.getInstance();
    private PriceCardListener priceCardListener;

    private long selectedStartDateMillis;
    private long selectedEndDateMillis;
    Date startDate;
    Date endDate;

    public PriceCardFragment() {

    }

    public interface PriceCardListener {
        void onPriceCardSaved(Date startDate,Date endDate, PriceTypeEnum priceType, double price);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_card, container, false);

        CalendarView calendarViewStart = view.findViewById(R.id.calendarViewStart);
        CalendarView calendarViewEnd = view.findViewById(R.id.calendarViewEnd);

        // Set up OnDateChangeListener for the start date CalendarView
        calendarViewStart.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedStartDateMillis = myCalendar.getTimeInMillis();

            startDate=myCalendar.getTime();

            
        });

        // Set up OnDateChangeListener for the end date CalendarView
        calendarViewEnd.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedEndDateMillis = myCalendar.getTimeInMillis();

            endDate=myCalendar.getTime();
        });


        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        confirmButton.setOnClickListener(v -> {
            savePriceCard(view);
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            priceCardListener = (PriceCardListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PriceCardListener");
        }
    }


    public void savePriceCard(View view){

        PriceTypeEnum priceType=PriceTypeEnum.PERUNIT ;
        RadioGroup radioGroupPriceType = view.findViewById(R.id.radioGroupPriceType);
        int selectedRadioButtonId = radioGroupPriceType.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = view.findViewById(selectedRadioButtonId);
            String selectedPriceType = selectedRadioButton.getText().toString();
            if(selectedPriceType.equals("PERGUEST")){
                priceType= PriceTypeEnum.PERGUEST;
            }
        } else {
            System.out.println("Nijedan tip cene nije odabran");
        }

        TextInputEditText priceInput = view.findViewById(R.id.priceInput);
        String priceStr = priceInput.getText().toString();

        if (!priceStr.isEmpty()) {
            try {
                int price = Integer.parseInt(priceStr);

                // Invoke the listener with the saved data
                if (priceCardListener != null) {
                    priceCardListener.onPriceCardSaved(startDate, endDate, priceType, price);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error parsing price: " + e.getMessage());
                // Handle the case where the input cannot be parsed to an integer
            }
        } else {
            System.out.println("Price input is empty");
        }

        // Invoke the listener with the saved data
//        if (priceCardListener != null) {
//            priceCardListener.onPriceCardSaved(startDate, endDate, priceType, price);
//        }

    }
}
