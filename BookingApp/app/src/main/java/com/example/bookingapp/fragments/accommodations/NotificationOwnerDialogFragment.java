package com.example.bookingapp.fragments.accommodations;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class NotificationOwnerDialogFragment extends DialogFragment {

    private boolean selectedOption;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Izaberite obaveštenje");

        String[] options = {"Owner Replied To Reservation Request Notification"};
        boolean[] selectedOptions = {false};  // Inicijalno ništa nije označeno

        builder.setMultiChoiceItems(options, selectedOptions, (dialog, which, isChecked) -> {
            selectedOptions[which] = isChecked;
        });
        builder.setPositiveButton("Apply", null); // null parametar da biste podesili dugme kasnije
        builder.setNegativeButton("Cancel", null); // null parametar da biste podesili dugme kasnije

        AlertDialog alertDialog = builder.create();

        // Postavljanje klika na dugme "Apply"
        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                // Opciono: Implementirajte logiku koja se izvršava pritiskom na dugme "Apply"
                selectedOption = selectedOptions[0];
                dismiss();  // Zatvori dijalog nakon primene izbora
            });
        });

        // Postavljanje klika na dugme "Cancel"
        alertDialog.setOnShowListener(dialog -> {
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> {
                // Opciono: Implementirajte logiku koja se izvršava pritiskom na dugme "Cancel"
                selectedOption = false;
                dismiss();  // Zatvori dijalog nakon pritiska na dugme "Cancel"
            });
        });


//        builder.setPositiveButton("Apply", (dialog, which) -> {
//            // Opciono: Implementirajte logiku koja se izvršava pritiskom na dugme "Apply"
//            selectedOption = selectedOptions[0];
//            dismiss();  // Zatvori dijalog nakon primene izbora
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            // Opciono: Implementirajte logiku koja se izvršava pritiskom na dugme "Cancel"
//            selectedOption = false;
//            dismiss();  // Zatvori dijalog nakon pritiska na dugme "Cancel"
//        });

        return builder.create();
    }

    public boolean getSelectedOption() {
        return selectedOption;
    }
}
