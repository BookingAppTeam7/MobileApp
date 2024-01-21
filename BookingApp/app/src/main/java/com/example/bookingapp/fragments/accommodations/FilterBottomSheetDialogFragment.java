package com.example.bookingapp.fragments.accommodations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.interfaces.BottomSheetListener;
import com.example.bookingapp.model.enums.TypeEnum;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class FilterBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private RadioButton radioButtonRoom, radioButtonApartment, radioButtonVipRoom;
    private CheckBox checkBoxWifi, checkBoxFreeParking, checkBoxAirConditioner, checkBoxKitchen;
    private TextInputEditText editTextMinPrice, editTextMaxPrice;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottom_sheet_filter, container, false);
        Button btnFilter=view.findViewById(R.id.btnFilter);
        radioButtonRoom = view.findViewById(R.id.radioButtonRoom);
        radioButtonApartment = view.findViewById(R.id.radioButtonApartment);
        radioButtonVipRoom = view.findViewById(R.id.radioButtonVipRoom);

        checkBoxWifi = view.findViewById(R.id.checkBoxWiFi);
        checkBoxFreeParking = view.findViewById(R.id.checkBoxFreeParking);
        checkBoxAirConditioner = view.findViewById(R.id.checkBoxAirConditioning);
        checkBoxKitchen = view.findViewById(R.id.checkBoxKitchen);

        editTextMinPrice = view.findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = view.findViewById(R.id.editTextMaxPrice);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeEnum selectedType = getSelectedType();
                List<String> selectedAssets = getSelectedAssets();
                String joined=null;
                if(!selectedAssets.isEmpty()){
                    joined=String.join(",",selectedAssets);
                }

                String minTotalPrice = editTextMinPrice.getText().toString().trim();
                String maxTotalPrice = editTextMaxPrice.getText().toString().trim();
                if(minTotalPrice.isEmpty())
                    minTotalPrice="-1";
                if(maxTotalPrice.isEmpty())
                    maxTotalPrice="-1";
                mListener.onFilterButtonClicked(selectedType, joined, minTotalPrice, maxTotalPrice);
                dismiss();
            }
        });
        return view;
    }
    private TypeEnum getSelectedType() {
        if (radioButtonRoom.isChecked()) {
            return TypeEnum.ROOM;
        } else if (radioButtonApartment.isChecked()) {
            return TypeEnum.APARTMENT;
        } else if (radioButtonVipRoom.isChecked()) {
            return TypeEnum.VIP_ROOM;
        } else {
            return null;
        }
    }
    private List<String> getSelectedAssets() {
        List<String> selectedAssets = new ArrayList<>();
        if (checkBoxWifi.isChecked()) {
            selectedAssets.add("Free Wi-Fi");
        }
        if (checkBoxFreeParking.isChecked()) {
            selectedAssets.add("Free Parking");
        }
        if (checkBoxAirConditioner.isChecked()) {
            selectedAssets.add("Air Conditioner");
        }
        if (checkBoxKitchen.isChecked()) {
            selectedAssets.add("Kitchen");
        }
        return selectedAssets;
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
