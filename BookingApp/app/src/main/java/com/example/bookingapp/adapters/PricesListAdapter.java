package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.model.DTOs.PriceCardStringDTO;
import com.example.bookingapp.model.DTOs.TimeSlotStringDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.PriceCardService;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PricesListAdapter extends ArrayAdapter<PriceCardStringDTO> {
    private List<PriceCardStringDTO> aPrices;
    private static Long accommodationId;

    public PricesListAdapter(Context context, List<PriceCardStringDTO> prices) {
        super(context, R.layout.list_price_card, prices);
        aPrices = prices;
    }

    @Override
    public int getCount() {
        return aPrices.size();
    }

    @Nullable
    @Override
    public PriceCardStringDTO getItem(int position) {
        return aPrices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setAccommodationId(Long id) {
        this.accommodationId = id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PriceCardStringDTO priceCard = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_edit_price_card,
                    parent, false);
        }


        TextView startDate=convertView.findViewById(R.id.listStartDate);
        TextView endDate=convertView.findViewById(R.id.listEndDate);
        TextView price=convertView.findViewById(R.id.listPrice);
        TextView type=convertView.findViewById(R.id.listType);

        if (priceCard != null && priceCard.getTimeSlot() != null) {

            startDate.setText("Start date : " + priceCard.getTimeSlot().getStartDate());
            endDate.setText("End Date : " + priceCard.getTimeSlot().getEndDate());
        }
        price.setText("Price : "+(int) priceCard.getPrice());
        type.setText("Type of price : "+priceCard.getType().toString());

        return convertView;
    }

}
