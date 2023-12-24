package com.example.bookingapp.activities;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityRegisterScreenBinding;
import com.example.bookingapp.fragments.accommodations.NotificationOwnerDialogFragment;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.DTOs.PriceCardPostDTO;
import com.example.bookingapp.model.DTOs.UserPostDTO;
import com.example.bookingapp.model.User;
import com.example.bookingapp.model.enums.RoleEnum;
import com.example.bookingapp.model.enums.StatusEnum;
import com.example.bookingapp.network.RetrofitClientInstance;
import com.example.bookingapp.services.AccommodationService;
import com.example.bookingapp.services.UserService;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterScreenActivity extends AppCompatActivity {

    public Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
    public UserService userService = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityRegisterScreenBinding binding=ActivityRegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create Account Screen");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dodajte nazad dugme
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Postavljanje akcije za nazad dugme

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = binding.editTextFirstName.getText().toString();
                String lastName = binding.editTextLastName.getText().toString();
                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                String passwordConfirmation = binding.editTextPasswordConfirmation.getText().toString();
                String phoneNumber = binding.editTextPhoneNumber.getText().toString();
                String address = binding.editTextAddress.getText().toString();

                RadioButton selectedRadioButton = findViewById(binding.radioGroupRole.getCheckedRadioButtonId());
                String role = selectedRadioButton.getText().toString();

                //notifications

                boolean reservationRequestNotification=false;
                boolean reservationCancellationNotification=false;
                boolean ownerRatingNotification=false;
                boolean accommodationRatingNotification=false;
                boolean ownerRepliedToRequestNotification=false;


                RoleEnum roleEnum=RoleEnum.GUEST;

                if(role.equals("Guest")){roleEnum=RoleEnum.GUEST;
                    NotificationOwnerDialogFragment dialogFragment = new NotificationOwnerDialogFragment();
                    dialogFragment.show(getSupportFragmentManager(), "notification_dialog");
                    ownerRepliedToRequestNotification=true;
                    reservationRequestNotification=true;
                    reservationCancellationNotification=true;
                    ownerRatingNotification=true;
                    accommodationRatingNotification=true;
                    // ownerRepliedToRequestNotification=showGuestNotificationDialog();

                }
                if(role.equals("Owner")){
                    roleEnum=RoleEnum.OWNER;
                    reservationRequestNotification=true;
                    reservationCancellationNotification=true;
                    ownerRatingNotification=true;
                    accommodationRatingNotification=true;
                    ownerRepliedToRequestNotification=false;
                }
                if(role.equals("Admin")){
                    roleEnum=RoleEnum.ADMIN;
                    reservationRequestNotification=false;
                    reservationCancellationNotification=false;
                    ownerRatingNotification=false;
                    accommodationRatingNotification=false;
                    ownerRepliedToRequestNotification=false;
                }

                UserPostDTO newUser=new UserPostDTO(firstName,lastName,username,password,passwordConfirmation,roleEnum,address,
                phoneNumber,reservationRequestNotification,reservationCancellationNotification,ownerRatingNotification,accommodationRatingNotification,ownerRepliedToRequestNotification,false);

                Call<User> call = userService.create(newUser);
                call.enqueue(new Callback<User>() {
                                 @Override
                                 public void onResponse(Call<User> call, Response<User> response) {
                                     if (response.isSuccessful()) {
                                         User createdUser = response.body();
                                         Toast.makeText(RegisterScreenActivity.this, "Successfully registered!Mail is sent...", Toast.LENGTH_SHORT).show();
                                     } else {
                                         Toast.makeText(RegisterScreenActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                     }
                                 }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

                Intent intent=new Intent(RegisterScreenActivity.this, AccountScreenActivity.class);
                startActivity(intent);
            }
        });

    }

//    private boolean[] showOwnerNotificationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_notifications_owner, null);
//
//        builder.setView(dialogView);
//
//        // Retrieve views from the layout
//        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
//        ListView listView = dialogView.findViewById(R.id.listView);
//        Button btnApply = dialogView.findViewById(R.id.btnApply);
//        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
//
//        String[] options = {
//                "Reservation Request Notification",
//                "Reservation Cancellation Notification",
//                "Owner Rating Notification",
//                "Accommodation Rating Notification"
//        };
//        boolean[] selectedOptions = {false, false, false, false};  // Initially, nothing is selected
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_multiple_choice, options);
//
//        listView.setAdapter(adapter);
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            selectedOptions[position] = !selectedOptions[position];
//        });
//
//        btnApply.setOnClickListener(v -> {
//            // Do something with the selected options, if needed
//            // For now, just close the dialog
//            builder.create().dismiss();
//        });
//
//        btnCancel.setOnClickListener(v -> builder.create().dismiss());
//
//        builder.create().show();
//
//        // Return the array of choices after the user confirms their selections
//        return selectedOptions;
//    }


////
//private boolean[] showOwnerNotificationPopup(View anchorView) {
//    View popupView = getLayoutInflater().inflate(R.layout.pop_up_notifications_owner, null);
//    PopupWindow popupWindow = new PopupWindow(popupView,
//            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//
//    // Retrieve views from the layout
//    TextView popupTitle = popupView.findViewById(R.id.popup_title);
//    ListView popupListView = popupView.findViewById(R.id.popupListView);
//    Button btnApply = popupView.findViewById(R.id.popup_btnApply);
//    Button btnCancel = popupView.findViewById(R.id.popup_btnCancel);
//
//    String[] options = {
//            "Reservation Request Notification",
//            "Reservation Cancellation Notification",
//            "Owner Rating Notification",
//            "Accommodation Rating Notification"
//    };
//    boolean[] selectedOptions = {false, false, false, false};  // Initially, nothing is selected
//
//    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//            android.R.layout.simple_list_item_multiple_choice, options);
//
//    popupListView.setAdapter(adapter);
//    popupListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//    popupListView.setOnItemClickListener((parent, view, position, id) -> {
//        selectedOptions[position] = !selectedOptions[position];
//    });
//
//    btnApply.setOnClickListener(v -> {
//        // Do something with the selected options, if needed
//
//        // Dismiss the popup
//        popupWindow.dismiss();
//    });
//
//    btnCancel.setOnClickListener(v -> popupWindow.dismiss());
//
//    // Set the elevation for the popup (if needed)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        popupWindow.setElevation(20);
//    }
//
//    // Show the popup at the specified location
//    popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
//
//    // Return the array of choices after the user confirms their selections
//    return selectedOptions;
//}
//
//    private boolean[] showOwnerNotificationDialog() {
//            // Create a Dialog instead of AlertDialog
//            Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.dialog_notifications_owner); // Set your custom layout here
//
//            // Retrieve views from the layout
//            TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
//            ListView listView = dialog.findViewById(R.id.listView);
//            Button btnApply = dialog.findViewById(R.id.btnApply);
//            Button btnCancel = dialog.findViewById(R.id.btnCancel);
//
//        String[] options = {
//                    "Reservation Request Notification",
//                    "Reservation Cancellation Notification",
//                    "Owner Rating Notification",
//                    "Accommodation Rating Notification"
//            };
//            boolean[] selectedOptions = {false, false, false, false};  // Initially, nothing is selected
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                    android.R.layout.simple_list_item_multiple_choice, options);
//
//            listView.setAdapter(adapter);
//            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//            listView.setOnItemClickListener((parent, view, position, id) -> {
//                selectedOptions[position] = !selectedOptions[position];
//            });
//
//
//        btnApply.setOnClickListener(v -> {
//            // Do something with the selected options, if needed
//
//            // Dismiss the dialog
//            dialog.dismiss();
//        });
//
//        btnCancel.setOnClickListener(v -> {
//            // Reset the selected options to their initial state
//            Arrays.fill(selectedOptions, false);
//            dialog.dismiss();
//        });
//            dialog.show();
//
//            // Return the array of choices after the user confirms their selections
//            return selectedOptions;
//        }
//
//private boolean showGuestNotificationDialog() {
//    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreenActivity.this);
//    builder.setTitle("Izaberite obaveštenje");
//
//    String[] options = {"Owner Replied To Reservation Request Notification"};
//    boolean[] selectedOptions = {false};  // Inicijalno ništa nije označeno
//
//    builder.setMultiChoiceItems(options, selectedOptions, (dialog, which, isChecked) -> {
//        selectedOptions[which] = isChecked;
//    });

//
//// Add the buttons.
//    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//        public void onClick(DialogInterface dialog, int id) {
//            // User taps OK button.
//        }
//    });
//    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//        public void onClick(DialogInterface dialog, int id) {
//            // User cancels the dialog.
//        }
//    });
//
//
//// Create the AlertDialog.
//    AlertDialog dialog = builder.create();
//
//
//    dialog.show();
//
//    return selectedOptions[0];
//}

//    private boolean showGuestNotificationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Izaberite obaveštenje");
//
//        String[] options = {"Owner Replied To Reservation Request Notification"};
//        boolean[] selectedOptions = {false};  // Inicijalno ništa nije označeno
//
//        builder.setMultiChoiceItems(options, selectedOptions, (dialog, which, isChecked) -> {
//            selectedOptions[which] = isChecked;
//        });
//
//
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//
//
//        });
//        builder.setNeutralButton("Apply", (dialog, which) -> {
//
//            // Opciono: Zatvori dijalog nakon primene izbora
//            dialog.dismiss();
//        });
//
//        final AlertDialog dialog = builder.create();
//        // Kreiraj dugme "Apply" unutar dijaloga
//        Button applyButton = new Button(this);
//        applyButton.setText("Apply");
//        applyButton.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        applyButton.setOnClickListener(v -> {
//            // Ako želite nešto raditi kad korisnik pritisne "Apply", možete dodati logiku ovde
//            dialog.dismiss();  // Zatvori dijalog nakon primene izbora
//        });
//
//        // Dodaj dugme "Apply" u dijalog
//        builder.setView(applyButton);
//
//
//
//        dialog.show();
//
//        return selectedOptions[0];
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}