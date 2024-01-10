package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.model.DTOs.ReviewGetDTO;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.UserReport;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.UserReportService;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ReportListAdapter  extends ArrayAdapter<UserReport> {
    private List<UserReport> aReports;

    public ReportListAdapter(Context context, List<UserReport> reports){
        super(context, R.layout.list_user_report,reports);
        aReports=reports;
    }

    @Override
    public int getCount(){ return aReports.size();}

    @Nullable
    @Override
    public UserReport getItem(int position) {
        return aReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserReport report = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_user_report,
                    parent, false);
        }

       TextView username = convertView.findViewById(R.id.listUsernameReportedUser);
        TextView usernameThatReports = convertView.findViewById(R.id.listUserThatReports);
        TextView reason=convertView.findViewById(R.id.listReason);

        username.setText("Reported user : "+report.getUserThatIsReported());
        usernameThatReports.setText("Reported by : "+report.getUserThatReports());
        reason.setText("Reason of report : "+report.getReason());

        Button btnIgnore=convertView.findViewById(R.id.btnIgnore);
        Button btnDeactivate=convertView.findViewById(R.id.btnDeactivate);

        btnDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                UserReportService reportService = retrofit.create(UserReportService.class);

                Call<UserReport> call = reportService.report(report.id);
                call.enqueue(new Callback<UserReport>() {
                    @Override
                    public void onResponse(Call<UserReport> call, Response<UserReport> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "User with id : "+report.getUserThatIsReported()+" DEACTIVATED! Report is closed!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserReport> call, Throwable t) {
                        Toast.makeText(getContext(), "Greška u komunikaciji sa serverom", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });




        btnIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                UserReportService reportService = retrofit.create(UserReportService.class);

                Call<UserReport> call = reportService.ignoreReport(report.id);
                call.enqueue(new Callback<UserReport>() {
                    @Override
                    public void onResponse(Call<UserReport> call, Response<UserReport> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Report is closed!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserReport> call, Throwable t) {
                        Toast.makeText(getContext(), "Greška u komunikaciji sa serverom", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });


        return convertView;
    }
}
