package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityFavouriteAccommodationsBinding;
import com.example.bookingapp.databinding.ActivityReport1Binding;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.ReservationService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Report1Activity extends AppCompatActivity {
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public ReservationService reservationService=retrofit.create(ReservationService.class);
    ActivityReport1Binding binding;
    public String ownerUsername;
    private String day1;
    private String month1;
    private String year1;
    private String day2;
    private String month2;
    private String year2;
    public Map<String, Double> reportMap=new HashMap<>();
    public List<Long> idList=new ArrayList<>();
    public List<String> nameList=new ArrayList<>();
    public List<Double> profitList=new ArrayList<>();
    private TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityReport1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        ownerUsername=intent.getStringExtra("username");

        CalendarView calendar1 = binding.calendar1;
        CalendarView calendar2 = binding.calendar2;

        Button generateReport=binding.btnReport;

        tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        addCellToRow(headerRow, "ID");
        addCellToRow(headerRow, "Acc name");
        addCellToRow(headerRow, "Profit");

        tableLayout.addView(headerRow);

        LinearLayout linearLayout = findViewById(R.id.report1Linear);
        linearLayout.addView(tableLayout);

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

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportMap.clear();
                idList.clear();
                nameList.clear();
                profitList.clear();
                String arrivalDate = year1+"-"+month1+"-"+day1;
                String checkoutDate = year2+"-"+month2+"-"+day2;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date arrivalDateObj, checkoutDateObj;
                try {
                    arrivalDateObj = sdf.parse(arrivalDate);
                    checkoutDateObj = sdf.parse(checkoutDate);

                Call<List<Reservation>> call= reservationService.findAll();
                call.enqueue(new Callback<List<Reservation>>() {
                    @Override
                    public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                        if(response.isSuccessful()){
                            List<Reservation> reservations=response.body();
                            for(Reservation r:reservations){
                                if(r.status== ReservationStatusEnum.APPROVED && !r.timeSlot.getStartDate().before(arrivalDateObj)
                                && !r.timeSlot.getEndDate().after(checkoutDateObj) && r.accommodation.ownerId.equals(ownerUsername)){
                                    String key=r.accommodation.id.toString()+","+r.accommodation.name;
                                    if(reportMap.containsKey(key)){
                                        reportMap.put(key,reportMap.get(key) + reservationTotalPrice(r));
                                    }else{
                                        reportMap.put(key, reservationTotalPrice(r));
                                    }
                                }
                            }
                            for (Map.Entry<String, Double> entry : reportMap.entrySet()) {
                                String key = entry.getKey();
                                Double value = entry.getValue();
                                idList.add(Long.parseLong(key.split(",")[0]));
                                nameList.add(key.split(",")[1]);
                                profitList.add(value);
                                Log.e("Key: " + key , "Value: " + value);
                            }

                            showTable();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Reservation>> call, Throwable t) {

                    }
                });

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public double reservationTotalPrice(Reservation r){
        double totalPrice = 0.0;

        if (r != null && r.timeSlot != null) {
            long numberOfDays = calculateNumberOfDays(r.timeSlot.getStartDate(), r.timeSlot.getEndDate());

            totalPrice = numberOfDays * r.price;

            if (r.priceType == PriceTypeEnum.PERGUEST) {
                totalPrice *= r.numberOfGuests;
            }
        }

        return totalPrice;
    }
    private long calculateNumberOfDays(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diffInDays;
    }
    private void showTable() {
        int childCount = tableLayout.getChildCount();
        for (int i = 1; i < childCount; i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                tableLayout.removeViewAt(i);
                i--;
                childCount--;
            }
        }

        for (int i = 0; i < idList.size(); i++) {
            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            addCellToRow(dataRow, String.valueOf(idList.get(i)));
            addCellToRow(dataRow, nameList.get(i));
            addCellToRow(dataRow, String.valueOf(profitList.get(i))+"$");

            tableLayout.addView(dataRow);
        }
    }

    private void addCellToRow(TableRow row, String text) {
        TextView cell = new TextView(this);
        cell.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        cell.setText(text);
        cell.setPadding(16, 16, 16, 16);
        row.addView(cell);
    }
}
