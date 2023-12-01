package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.Review;

import java.util.ArrayList;

public class ReviewListAdapter extends ArrayAdapter<Review> {
    private ArrayList<Review> aReviews;

    public ReviewListAdapter(Context context, ArrayList<Review> reviews){
        super(context, R.layout.list_review,reviews);
        aReviews=reviews;
    }

    @Override
    public int getCount(){ return aReviews.size();}

    @Nullable
    @Override
    public Review getItem(int position) {
        return aReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Review review = getItem(position);
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
        //TextView accommodationDescription = convertView.findViewById(R.id.listAccommodationDesc);

        //imageView.setImageResource(accommodation.getImage());
        imageView.setImageResource(review.getImage());
        reviewUsername.setText(review.getUsername());
        reviewType.setText("Review type: " + String.valueOf(review.getReviewType()));
        reviewGrade.setText("Grade: " + String.valueOf(review.getGrade()));
        reviewComment.setText(review.getContent());

        return convertView;
    }
}
