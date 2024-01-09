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
import com.example.bookingapp.model.Review;

import java.util.List;

public class ReviewListAdapter extends ArrayAdapter<Review> {
    private List<Review> aReviews;

    public ReviewListAdapter(Context context, List<Review> reviews){
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
        //TextView accommodationDescription = convertView.findViewById(R.id.listAccommodationDesc);

        //imageView.setImageResource(accommodation.getImage());
        //imageView.setImageResource(review.getImage());
        reviewUsername.setText("Username: "+review.getUserId());
        reviewType.setText("Type: " + review.getType().toString());
        reviewGrade.setText("Grade: " + String.valueOf(review.getGrade()));
        reviewComment.setText(review.getComment());

        return convertView;
    }
}
