package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityReport1Binding;
import com.example.bookingapp.databinding.ActivityReport2Binding;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.enums.PriceTypeEnum;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.ReservationService;

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

public class Report2Activity extends AppCompatActivity {
    ActivityReport2Binding binding;
    public String ownerUsername;
    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public AccommodationService accommodationService=retrofit.create(AccommodationService.class);
    public ReservationService reservationService=retrofit.create(ReservationService.class);
    public Map<String, Double> reportMap=new HashMap<>();
    public List<Integer> monthList=new ArrayList<>();
    public List<Integer> numResList=new ArrayList<>();
    public List<Double> profitList=new ArrayList<>();
    private TableLayout tableLayout;
    public List<Accommodation> ownerAccommodations=new ArrayList<>();
    public List<String> months=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        months.clear();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        super.onCreate(savedInstanceState);
        binding= ActivityReport2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        ownerUsername=intent.getStringExtra("username");

        tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        addCellToRow(headerRow, "Month");
        addCellToRow(headerRow, "Num of reservations");
        addCellToRow(headerRow, "Profit");

        tableLayout.addView(headerRow);

        LinearLayout linearLayout = findViewById(R.id.linearLayout2);
        linearLayout.addView(tableLayout);

        Call call1=accommodationService.findByOwnerId(ownerUsername);
        call1.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call1, Response<List<Accommodation>> response) {
                if(response.isSuccessful()){
                    ownerAccommodations=response.body();
                    for(Accommodation a:ownerAccommodations)
                        Log.e("ACCOMMODATION1111",a.toString());
                    binding.btnReport2.setEnabled(true);
                }else{
                    Log.e("ERROR",String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("GREEEESKA",t.getMessage());
                t.printStackTrace();
            }
        });
        binding.btnReport2.setEnabled(false);
        binding.btnReport2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profitList.clear();
                monthList.clear();
                numResList.clear();
                for(int i=0;i<12;i++){
                    profitList.add(0.0);
                    monthList.add(i);
                    numResList.add(0);
                }
                if(binding.editTextAccId.getText().toString().isEmpty() || binding.editTextYear.getText().toString().isEmpty()){
                    Toast.makeText(Report2Activity.this,"Fill all data!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Long accommodationId=Long.parseLong(binding.editTextAccId.getText().toString());
                Integer year=Integer.parseInt(binding.editTextYear.getText().toString());
                if(!isInAccommodations(accommodationId,ownerAccommodations)){
                    Toast.makeText(Report2Activity.this,"Accommodation id incorrect!",Toast.LENGTH_SHORT).show();
                    return;
                }if(year<1000){
                    Toast.makeText(Report2Activity.this,"Year input is incorrect!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Call call2=reservationService.findByAccommodationId(accommodationId);
                call2.enqueue(new Callback<List<Reservation>>() {
                    @Override
                    public void onResponse(Call<List<Reservation>> call2, Response<List<Reservation>> response) {
                        if(response.isSuccessful()){
                            List<Reservation> allRes=response.body();
                            for(Reservation r:allRes){
                                if(r.status== ReservationStatusEnum.APPROVED && (r.timeSlot.getStartDate().getYear()+1900)==year && r.accommodation.id.equals(accommodationId)){
                                    Log.e("REZ",r.id.toString());
                                    Log.e("Mesec",String.valueOf(r.timeSlot.getStartDate().getMonth()));
                                    //dodaj ukupnu cenu rez u listu i u drugu +1 na month mesto
                                    int month=r.timeSlot.getStartDate().getMonth();
                                    profitList.set(month,profitList.get(month)+reservationTotalPrice(r));
                                    numResList.set(month,numResList.get(month)+1);
                                }
                            }
                            showTable();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Reservation>> call2, Throwable t) {
                        Log.e("Error : ",t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        });
    }
    private boolean isInAccommodations(Long id, List<Accommodation> accommodations){
        for(Accommodation a:accommodations){
            if(a.id.equals(id)){
                return true;
            }
        }
        return false;
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
    private void addCellToRow(TableRow row, String text) {
        TextView cell = new TextView(this);
        cell.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        cell.setText(text);
        cell.setPadding(16, 16, 16, 16);
        row.addView(cell);
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

        for (int i = 0; i < 12; i++) {
            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            addCellToRow(dataRow, months.get(i));
            addCellToRow(dataRow, numResList.get(i).toString());
            addCellToRow(dataRow, profitList.get(i).toString()+"$");

            tableLayout.addView(dataRow);
        }
    }
}