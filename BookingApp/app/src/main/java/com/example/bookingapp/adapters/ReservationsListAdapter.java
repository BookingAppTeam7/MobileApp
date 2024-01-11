package com.example.bookingapp.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.AccountScreenActivity;
import com.example.bookingapp.activities.RateAccommodationActivity;
import com.example.bookingapp.activities.RateOwnerActivity;
import com.example.bookingapp.activities.RegisterScreenActivity;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.ReservationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservationsListAdapter extends ArrayAdapter<Reservation> {

    private ArrayList<Reservation> aReservations;

    public ReservationsListAdapter(Context context, ArrayList<Reservation> reservations){
        super(context, R.layout.list_reservation_request, reservations);
        aReservations = reservations;
    }

    @Override
    public int getCount() {
        return aReservations.size();
    }

    @Nullable
    @Override
    public Reservation getItem(int position) {
        return aReservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation request = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_reservation_request,
                    parent, false);
        }




        TextView requestStatus = convertView.findViewById(R.id.listStatus);
        TextView guest = convertView.findViewById(R.id.listGuestUsername);
        TextView startDate=convertView.findViewById(R.id.listStartDate);
        TextView endDate=convertView.findViewById(R.id.listEndDate);
        TextView numOfGuests=convertView.findViewById(R.id.listNumberOfGuests);



        Date startDateValue = request.getTimeSlot().getStartDate();
        Date endDateValue = request.getTimeSlot().getEndDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formattedStartDate = dateFormat.format(startDateValue);
        String formattedEndDate = dateFormat.format(endDateValue);

        startDate.setText("Start Date: " + formattedStartDate);
        endDate.setText("End Date: " + formattedEndDate);


        requestStatus.setText("Status of request : "+String.valueOf(request.getStatus()));
        guest.setText("Guest : "+String.valueOf(request.getUser().username));
        numOfGuests.setText("Number of guests : "+String.valueOf(request.getNumberOfGuests()));

        Button btnApprove = convertView.findViewById(R.id.btnApprove);
        Button btnReject = convertView.findViewById(R.id.btnReject);

        Button btnCancel=convertView.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.INVISIBLE);

        Button btnDelete=convertView.findViewById(R.id.btnDeleteReservation);
        btnDelete.setVisibility(View.INVISIBLE);


        Button btnRateAccommodation=convertView.findViewById(R.id.btnRateAccommodation);
        btnRateAccommodation.setVisibility(View.INVISIBLE);
        Button btnRateOwner=convertView.findViewById(R.id.btnRateOwner);
        //btnRateAccommodation.setVisibility(View.INVISIBLE);
        btnRateOwner.setVisibility(View.INVISIBLE);


        RoleEnum role= TokenManager.getLoggedInUser().role;
        if(!role.equals(RoleEnum.OWNER)){
            btnApprove.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnRateAccommodation.setVisibility(View.VISIBLE);
            btnRateOwner.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(!requestStatus.getText().equals("PENDING")){
//                    Toast.makeText(getContext(), "Reservation request is closed!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }

               Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                ReservationService reservationService = retrofit.create(ReservationService.class);

                Call<Void> call = reservationService.confirmReservation(request.id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            requestStatus.setText("Status of request : APPROVED");
                            Toast.makeText(getContext(), "Reservation wit id : "+request.id+"  APPROVED!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Greška u komunikaciji sa serverom", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
        View finalConvertView3 = convertView;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                if(request.status!=ReservationStatusEnum.PENDING){
                    Toast.makeText(getContext(), "Can't delete if status isn't PENDING!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ReservationService reservationService = retrofit.create(ReservationService.class);

                Call<Void> call = reservationService.deleteReservation(request.id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Reservation DELETED!", Toast.LENGTH_SHORT).show();
                            finalConvertView3.setVisibility(View.INVISIBLE);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Greška u komunikaciji sa serverom", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(!requestStatus.getText().equals("PENDING")){
//                    Toast.makeText(getContext(), "Reservation request is closed!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                ReservationService reservationService = retrofit.create(ReservationService.class);

                Call<Void> call = reservationService.rejectReservation(request.id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            requestStatus.setText("Status of request : REJECTED");
                            Toast.makeText(getContext(), "Reservation wit id : "+request.id+"  REJECTED!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Greška u komunikaciji sa serverom", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(request.status!= ReservationStatusEnum.APPROVED){
                    Toast.makeText(getContext(), "Reservation is not approved yet!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check cancellationDeadline

                Date startDate = request.getTimeSlot().getStartDate();

                if (startDate != null) {
                    Date currentDate = new Date();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    calendar.add(Calendar.DAY_OF_YEAR, -request.getAccommodation().getCancellationDeadline());
                    Date cancellationAllowedDate = calendar.getTime();

                    if (currentDate.after(cancellationAllowedDate)) {
                        Toast.makeText(getContext(), "Error! Cancellation deadline is passed!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                ReservationService reservationService = retrofit.create(ReservationService.class);

                Call<Void> call = reservationService.cancelReservation(request.id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            requestStatus.setText("Status of request : CANCELLED");
                            Toast.makeText(getContext(), "Reservation wit id : "+request.id+"  CANCELLED!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Greška u komunikaciji sa serverom", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
        View finalConvertView = convertView;
        View finalConvertView1 = finalConvertView;
        btnRateAccommodation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = finalConvertView1.getContext();

                Intent intent = new Intent(context, RateAccommodationActivity.class);
                intent.putExtra("accommodationId",request.accommodation.id); // Assuming getUsername() is the correct method
                intent.putExtra("ownerIdA",request.accommodation.ownerId);
                intent.putExtra("reservationId",request.id);
                // Start the activity
                context.startActivity(intent);

            }

        });
      finalConvertView = convertView;
        View finalConvertView2 = finalConvertView;
        btnRateOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = finalConvertView2.getContext();

                Intent intent = new Intent(context, RateOwnerActivity.class);
                intent.putExtra("ownerId",request.accommodation.ownerId); // Assuming getUsername() is the correct method

                // Start the activity
                context.startActivity(intent);

            }

        });

        return convertView;
    }
}
