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
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.CreateAccommodationActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class PriceCardFragment extends DialogFragment {

    private Calendar myCalendar = Calendar.getInstance();
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    public PriceCardFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_card, container, false);

        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        confirmButton.setOnClickListener(v -> {
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }
    private void showDatePickerDialog(EditText editText) {
        Context context = getContext();

        if (context != null) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String selectedDate = sdf.format(myCalendar.getTime());

                        editText.setText(selectedDate);
                        editText.clearFocus();
                        editText.invalidate();
                    },
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        }
    }
}
