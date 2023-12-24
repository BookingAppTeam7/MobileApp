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
import com.example.bookingapp.activities.EditPriceCardsActivity;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.PriceCardStringDTO;
import com.example.bookingapp.model.DTOs.TimeSlotStringDTO;
import com.example.bookingapp.model.PriceCard;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.TypeEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.PriceCardService;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PriceCardListAdapter extends ArrayAdapter<PriceCard> {
    private List<PriceCard> aPrices;
    private static Long accommodationId;

    public PriceCardListAdapter(Context context, List<PriceCard> prices) {
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

    public void setAccommodationId(Long id) {
        this.accommodationId = id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PriceCard priceCard = getItem(position);
        if (convertView == null) {
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


        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prikupite podatke iz forme
                double price = Double.parseDouble(editTextPrice.getText().toString());
                String startDate = editTextStartDate.getText().toString();
                String endDate = editTextEndDate.getText().toString();
                PriceTypeEnum type = radioButtonUnit.isChecked() ? PriceTypeEnum.PERUNIT : PriceTypeEnum.PERGUEST;

                TimeSlotStringDTO timeSlotStringDTO = new TimeSlotStringDTO(startDate, endDate);
                PriceCardStringDTO priceCardStringDTO = new PriceCardStringDTO(timeSlotStringDTO, price, type);
                // accommodationId nam je nebitan u ovom slučaju
                priceCardStringDTO.setAccommodationId(accommodationId);

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
                PriceCardService priceCardService = retrofit.create(PriceCardService.class);

                Call<PriceCard> call = priceCardService.modify(priceCardStringDTO, priceCard.getId());
                call.enqueue(new Callback<PriceCard>() {
                    @Override
                    public void onResponse(Call<PriceCard> call, Response<PriceCard> response) {
                        if (response.isSuccessful()) {
                            PriceCard modifiedPriceCard = response.body();
                            // Update the data in the adapter
                            updatePriceCard(position, modifiedPriceCard);
                        }
                    }

                    @Override
                    public void onFailure(Call<PriceCard> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
        return convertView;
    }

    public void updatePriceCard(int position, PriceCard updatedPriceCard) {
        aPrices.set(position, updatedPriceCard);
        notifyDataSetChanged();
    }

}