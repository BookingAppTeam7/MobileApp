package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PriceCardListAdapter extends ArrayAdapter<PriceCard> {
    private List<PriceCard> aPrices;

    public PriceCardListAdapter(Context context, List<PriceCard> prices){
        super(context, R.layout.item_edit_price_card, prices);
        aPrices = prices;
    }

    @Override
    public int getCount() {
        return aPrices.size();
    }

    @Nullable
    @Override
    public PriceCard getItem(int position) {
        return aPrices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PriceCard priceCard = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_edit_price_card,
                    parent, false);
        }

        TextInputEditText editTextPrice = convertView.findViewById(R.id.editTextPrice);
        TextInputEditText editTextStartDate = convertView.findViewById(R.id.editTextStartDate);
        TextInputEditText editTextEndDate = convertView.findViewById(R.id.editTextEndDate);
        Button buttonSaveChanges = convertView.findViewById(R.id.buttonCreate);

        RadioButton radioButtonUnit = convertView.findViewById(R.id.radioButtonUnit);
        RadioButton radioButtonGuest = convertView.findViewById(R.id.radioButtonGuest);


        // Postavljanje podataka
        if (priceCard != null) {
            editTextPrice.setText(String.valueOf(priceCard.price));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            String formattedStartDate = sdf.format(priceCard.timeSlot.getStartDate());
            String formattedEndDate = sdf.format(priceCard.timeSlot.getEndDate());

            editTextStartDate.setText(formattedStartDate);
            editTextEndDate.setText(formattedEndDate);

            if (priceCard.getType() == PriceTypeEnum.PERGUEST) {
                radioButtonGuest.setChecked(true);
            } else {
                radioButtonUnit.setChecked(true);
            }

        }

        return convertView;
    }

    public void updateData(List<PriceCard> newData) {
        aPrices.clear();
        aPrices.addAll(newData);
        notifyDataSetChanged();
    }
}