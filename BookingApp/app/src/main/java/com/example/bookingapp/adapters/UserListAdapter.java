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
import com.example.bookingapp.activities.UserRatingsRequestsActivity;
import com.example.bookingapp.activities.UserReportsActivity;
import com.example.bookingapp.activities.UsersReviewActivity;
import com.example.bookingapp.model.DTOs.UserGetDTO;
import com.example.bookingapp.model.Reservation;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.ReservationStatusEnum;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.model.enums.StatusEnum;
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

public class UserListAdapter extends ArrayAdapter<UserGetDTO> {
    private ArrayList<UserGetDTO> aUsers;

    public UserListAdapter(Context context, ArrayList<UserGetDTO> users){
        super(context, R.layout.list_user, users);
        aUsers = users;
    }

    @Override
    public int getCount() {
        return aUsers.size();
    }

    @Nullable
    @Override
    public UserGetDTO getItem(int position) {
        return aUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserGetDTO user = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_user,
                    parent, false);
        }

        TextView username = convertView.findViewById(R.id.listUsername);
        TextView role = convertView.findViewById(R.id.listRole);
        TextView status=convertView.findViewById(R.id.listStatus);
        TextView name=convertView.findViewById(R.id.listFirstLastName);
        TextView address=convertView.findViewById(R.id.listAddress);
        TextView contact=convertView.findViewById(R.id.listContact);

        username.setText("Username : "+user.getUsername());
        role.setText("Role : "+user.getRole().toString());
        status.setText("Status : "+user.getStatus());
        name.setText("Name : "+user.getFirstName()+" "+user.getLastName());
        address.setText("Address : "+user.getAddress());
        contact.setText("Contact : "+user.getPhoneNumber());


        Button btnReviews = convertView.findViewById(R.id.btnReviews);
        if(user.getStatus()== StatusEnum.DEACTIVE || user.getRole()!=RoleEnum.OWNER){
            btnReviews.setVisibility(View.INVISIBLE);
        }

        Button btnReports=convertView.findViewById(R.id.btnReports);

        View finalConvertView = convertView;
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = finalConvertView.getContext();

                Intent intent = new Intent(context, UserRatingsRequestsActivity.class);
                intent.putExtra("username", user.getUsername()); // Assuming getUsername() is the correct method

                // Start the activity
                context.startActivity(intent);

            }

        });

        View finalConvertView1 = convertView;
        btnReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = finalConvertView1.getContext();

                Intent intent = new Intent(context, UserReportsActivity.class);
                intent.putExtra("username", user.getUsername());

                context.startActivity(intent);

            }

        });


        return convertView;
    }

}
