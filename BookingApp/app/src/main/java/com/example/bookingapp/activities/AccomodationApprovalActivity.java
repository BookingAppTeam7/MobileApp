package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.adapters.AccomodationApprovalListAdapter;
import com.example.bookingapp.databinding.ActivityAccomodationApprovalBinding;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.Review;
import com.example.bookingapp.model.ReviewType;
import com.example.bookingapp.model.TimeSlot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccomodationApprovalActivity extends AppCompatActivity {


    ActivityAccomodationApprovalBinding binding;
    AccomodationApprovalListAdapter listAdapter;
    ArrayList<Accommodation> accommodationArrayList = new ArrayList<Accommodation>();
    Accommodation accommodation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccomodationApprovalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int[] imageList = {R.drawable.accommodation1, R.drawable.accommodation2, R.drawable.accommodation1, R.drawable.accommodation2};
        Long[] idList = {1L, 2L, 3L, 4L};
        String[] nameList = {"Accommodation in Novi Sad", "Belgrade accommodation", "Prague accommodation", "Paris accommodation"};
        String[] descriptionList = {"Beautiful accommodation settled in Novi Sad", "Beautiful accommodation settled in Belgrade",
                "Beautiful accommodation settled in Prague", "Beautiful accommodation settled in Paris"};

        double [] locationXList={45.26799224033295,44.78318632559004,50.102423832053915,48.870974300967234};
        double [] locationYList={19.830824522193232, 20.49925984639849, 14.480125374774326, 2.478835370652442};
        String [] locationStrList={"Some location in Novi Sad","Some location in Belgrade","Some location in Prague",
                "Some location in Paris"};
        double [] priceList={150.0,215.0,100.0,199.99};
        List<Review> reviewsList = new ArrayList<>();


        reviewsList.add(new Review(R.drawable.ic_user,"User1", ReviewType.OWNER, "Great experience!", 5));
        reviewsList.add(new Review(R.drawable.ic_user,"User2", ReviewType.ACCOMMODATION, "Nice place to stay.", 4));
        reviewsList.add(new Review(R.drawable.ic_user,"User3", ReviewType.OWNER, "Very helpful owner.", 5));
        reviewsList.add(new Review(R.drawable.ic_user,"User4", ReviewType.ACCOMMODATION, "Clean and comfortable.", 4));

        ArrayList<String> assets=new ArrayList<>();
        assets.add("TV");
        assets.add("WI-FI");
        assets.add("Free parking");
        assets.add("Air conditioner");

        ArrayList<TimeSlot> availability=new ArrayList<>();
        availability.add(new TimeSlot(1L, LocalDate.of(2023,12,3),LocalDate.of(2023,12,6)));
        availability.add(new TimeSlot(1L, LocalDate.of(2023,12,9),LocalDate.of(2023,12,15)));

        for (int i = 0; i < imageList.length; i++) {
            accommodation = new Accommodation(idList[i], nameList[i], descriptionList[i], imageList[i],
                    locationStrList[i],locationXList[i],locationYList[i],priceList[i],reviewsList,assets,availability);
            accommodationArrayList.add(accommodation);
        }

        listAdapter = new AccomodationApprovalListAdapter(AccomodationApprovalActivity.this, accommodationArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AccomodationApprovalActivity.this, DetailedActivity.class);
                intent.putExtra("name", nameList[position]);
                intent.putExtra("description", descriptionList[position]);
                intent.putExtra("image", imageList[position]);

                startActivity(intent);
            }
        });

    }
}
