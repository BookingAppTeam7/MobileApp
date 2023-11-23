package com.example.bookingapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.bookingapp.databinding.ActivityEditAccommodationsDataBinding;

import java.util.Calendar;

public class EditAccommodationsDataActivity extends AppCompatActivity {

    private EditText editTextStartDate;
    private EditText editTextEndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEditAccommodationsDataBinding binding = ActivityEditAccommodationsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        editTextStartDate=binding.editTextStartDate;
        editTextEndDate=binding.editTextEndDate;

        editTextStartDate.setFocusable(false);
        editTextStartDate.setClickable(true);

        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    editTextStartDate.setText(selectedDate);
                },
                year, month, day);

        // Ograničavanje da se odabere samo datum iz budućnosti
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();

    }

}