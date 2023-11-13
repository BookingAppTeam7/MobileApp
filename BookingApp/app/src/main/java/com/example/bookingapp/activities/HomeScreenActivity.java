package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.databinding.ActivityHomeScreenBinding;
import com.example.bookingapp.fragments.accommodations.FilterBottomSheetDialogFragment;
import com.example.bookingapp.model.Accommodation;

import java.util.ArrayList;


public class HomeScreenActivity extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    AccommodationListAdapter listAdapter;
    ArrayList<Accommodation> accommodationArrayList = new ArrayList<Accommodation>();
    Accommodation accommodation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageList={R.drawable.accommodation1,R.drawable.accommodation2,R.drawable.accommodation1,R.drawable.accommodation2};
        Long[] idList={1L,2L,3L,4L};
        String[] nameList={"Accommodation 1","Accommodation 2", "Accommodation 3", "Accommodation 4"};
        String[] descriptionList={"Description 1","Description 2","Description 3","Description 4"};

        for(int i=0;i<imageList.length;i++){
            accommodation=new Accommodation(idList[i],nameList[i],descriptionList[i],imageList[i]);
            accommodationArrayList.add(accommodation);
        }

        listAdapter=new AccommodationListAdapter(HomeScreenActivity.this,accommodationArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeScreenActivity.this, DetailedActivity.class);
                intent.putExtra("name",nameList[position]);
                intent.putExtra("description",descriptionList[position]);
                intent.putExtra("image",imageList[position]);

                startActivity(intent);
            }
        });

        binding.btnFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterBottomSheetDialogFragment bottomSheetFragment = new FilterBottomSheetDialogFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }
}