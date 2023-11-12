package com.example.bookingapp.fragments.accommodations;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookingapp.R;
import com.example.bookingapp.fragments.FragmentTransition;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.databinding.FragmentAccommodationsPageBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class AccommodationsPageFragment extends Fragment {
    public static ArrayList<Accommodation> accommodations = new ArrayList<Accommodation>();
    private AccommodationsPageViewModel accommodationsViewModel;
    private FragmentAccommodationsPageBinding binding;

    public static AccommodationsPageFragment newInstance() {
        return new AccommodationsPageFragment();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accommodationsViewModel = new ViewModelProvider(this).get(AccommodationsPageViewModel.class);

        binding = FragmentAccommodationsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prepareAccommodationList(accommodations);

        SearchView searchView = binding.searchText;
        accommodationsViewModel.getText().observe(getViewLifecycleOwner(), searchView::setQueryHint);

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            Log.i("BookingApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });


        FragmentTransition.to(AccommodationsListFragment.newInstance(accommodations), getActivity(), false, R.id.scroll_products_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareAccommodationList(ArrayList<Accommodation> accommodations){
        accommodations.add(new Accommodation(1L, "Accommodation 1", "Description 1", R.drawable.accommodation1));
        accommodations.add(new Accommodation(2L, "Accommodation 2", "Description 2", R.drawable.accommodation2));
        accommodations.add(new Accommodation(3L, "Accommodation 3", "Description 3", R.drawable.accommodation1));
        accommodations.add(new Accommodation(4L, "Accommodation 4", "Description 4", R.drawable.accommodation2));
    }
}
