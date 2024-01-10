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
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.TokenManager;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.ReservationService;
import com.example.bookingapp.services.ReviewService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewListAdapter extends ArrayAdapter<ReviewGetDTO> {
    private List<ReviewGetDTO> aReviews;

    public ReviewListAdapter(Context context, List<ReviewGetDTO> reviews){
        super(context, R.layout.list_review,reviews);
        aReviews=reviews;
    }

    @Override
    public int getCount(){ return aReviews.size();}

    @Nullable
    @Override
    public ReviewGetDTO getItem(int position) {
        return aReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReviewGetDTO review = getItem(position);
        System.out.println(review.getType());
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_review,
                    parent, false);
        }
        //LinearLayout accommodationCard = convertView.findViewById(R.id.accommodation_card_item);
        ImageView imageView = convertView.findViewById(R.id.listImage);
        TextView reviewUsername = convertView.findViewById(R.id.listUsername);
        TextView reviewType = convertView.findViewById(R.id.listReviewType);
        TextView reviewGrade = convertView.findViewById(R.id.listGrade);
        TextView reviewComment = convertView.findViewById(R.id.listComment);
        TextView reported=convertView.findViewById(R.id.listReported);
        TextView time=convertView.findViewById(R.id.listTime);
        TextView status=convertView.findViewById(R.id.listStatus);
        //TextView accommodationDescription = convertView.findViewById(R.id.listAccommodationDesc);

        //imageView.setImageResource(accommodation.getImage());
        //imageView.setImageResource(review.getImage());
        reviewUsername.setText("Username: "+review.getUserId());
        reviewType.setText("Type : " + review.getType().toString());
        reviewGrade.setText("Grade : " + String.valueOf(review.getGrade()));
        reviewComment.setText("Comment : "+review.getComment());
        time.setText("Time : "+review.getDateTime());


        if(review.getReported()){
            reported.setText("REPORTED!");
        }
        else{
            reported.setText("");
        }

        Button buttonApprove=convertView.findViewById(R.id.btnApprove);
        Button rejectButton=convertView.findViewById(R.id.btnReject);
        buttonApprove.setVisibility(View.INVISIBLE);
        rejectButton.setVisibility(View.INVISIBLE);
        Button deleteButton=convertView.findViewById(R.id.btnDelete);
        deleteButton.setVisibility(View.INVISIBLE);


        if(TokenManager.getLoggedInUser().getRole()== RoleEnum.ADMIN){
            buttonApprove.setVisibility(View.VISIBLE);
            status.setText("Status : "+review.getStatus());
            if(review.getReported()) {
                rejectButton.setVisibility(View.VISIBLE);
            }
        }
        else{
            status.setText(" ");
        }
        if(TokenManager.getLoggedInUser().username.equals(review.getUserId())){
            deleteButton.setVisibility(View.VISIBLE);
        }

        buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                ReviewService reviewService = retrofit.create(ReviewService.class);

                Call<Void> call = reviewService.approve(review.id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            status.setText("Status of request : APPROVED");
                            Toast.makeText(getContext(), "Reservation wit id : "+review.id+"  APPROVED!", Toast.LENGTH_SHORT).show();
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


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                ReviewService reviewService = retrofit.create(ReviewService.class);

                Call<Void> call = reviewService.delete(review.id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "DELETED!", Toast.LENGTH_SHORT).show();
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


        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();

                ReviewService reviewService = retrofit.create(ReviewService.class);

                Call<Void> call = reviewService.reject(review.id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            status.setText("Status of request : REJECTED");
                            Toast.makeText(getContext(), "Reservation wit id : "+review.id+"  REJECTED!", Toast.LENGTH_SHORT).show();
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

        return convertView;
    }
}
