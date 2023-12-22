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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.CreateAccommodationActivity;
import com.example.bookingapp.model.DTOs.PriceCardPostDTO;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PriceCardFragment extends Fragment {

    private List<PriceCardPostDTO> prices;

    public PriceCardFragment(List<PriceCardPostDTO> prices) {
        this.prices = prices;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_card, container, false);

        TableLayout tableLayout = view.findViewById(R.id.tableLayout);

//        ImageButton closeButton=view.findViewById(R.id.closeButton);

        for (PriceCardPostDTO priceCard : prices) {
            TableRow row = new TableRow(requireContext());

            TextView startDateTextView = new TextView(requireContext());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String startDateText = sdf.format(priceCard.getTimeSlot().getStartDate());
            startDateTextView.setText(startDateText);
            row.addView(startDateTextView);

            TextView endDateTextView = new TextView(requireContext());
            String endDateText = sdf.format(priceCard.getTimeSlot().getEndDate());
            endDateTextView.setText(endDateText);
            row.addView(endDateTextView);

            TextView priceTextView = new TextView(requireContext());
            priceTextView.setText(String.valueOf(priceCard.getPrice()));
            row.addView(priceTextView);

            TextView typeTextView = new TextView(requireContext());
            typeTextView.setText(priceCard.getType().toString());
            row.addView(typeTextView);

            // Prvo uklonite row iz njegovog postojećeg roditelja, ako ga ima
            if (row.getParent() != null) {
                ((ViewGroup) row.getParent()).removeView(row);
            }

            // Dodajte row u tableLayout
            tableLayout.addView(row);
        }

//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Dohvati menadžera fragmenata
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//
//                // Proveri da li postoji fragment na steku
//                Fragment fragment = fragmentManager.findFragmentByTag("PriceCardFragment");
//
//                // Ako fragment postoji, izvrši transakciju za njegovo uklanjanje
//                if (fragment != null) {
//                    fragmentManager.beginTransaction().remove(fragment).commit();
//                }
//            }
//        });

        return view;
    }
}
