package com.example.bookingapp.interfaces;

import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.model.enums.TypeEnum;

public interface BottomSheetListener {
    void onSearchButtonClicked(String place, int guests, String arrivalDate, String checkoutDate);
    void onFilterButtonClicked(TypeEnum selectedType,String joined,String minTotalPrice,String maxTotalPrice);
    void onReservationButtonClicked(int guests, String arrivalDate,String checkoutDate);
    void onFilterReservationButtonClicked(String accName, String arrivalDate, String checkoutDate, ReservationStatusEnum status);
}
