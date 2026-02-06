package com.example.ridecare.utils;

import static android.content.Intent.getIntent;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Calendar;

public class MyUtils {
    public static void openDatePicker(Context context, EditText etServiceDate) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etServiceDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show();
    }
    public static String newStr(EditText text){
        return text.getText().toString().trim();
    }

    public static boolean requireString(String value, EditText editText, String errorMessage) {
        if (value.isEmpty()) {
            editText.setError(errorMessage);
            editText.requestFocus();
            return false;
        }
        return true;
    }


    public static String UidCheck(Context context, FirebaseAuth auth) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(context, "Not logged in", Toast.LENGTH_SHORT).show();
            return null;
        }
        return auth.getCurrentUser().getUid();
    }

    public static String vehicleIdCheck(Activity activity){
        Intent intent = activity.getIntent();
        String vehicleId = intent.getStringExtra("vehicleId");

        if (vehicleId == null) {
            intent.getStringExtra("id");
        }

        if (vehicleId == null || vehicleId.isEmpty()) {
            Toast.makeText(activity, "Vehicle ID missing. Please reopen vehicle.", Toast.LENGTH_LONG).show();
            activity.finish(); // closes the broken screen
            return null;
        }
        return vehicleId;
    }


    public static boolean requireDocument(DocumentSnapshot doc, Context context, String message) {
        if (doc == null || !doc.exists()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static void setDropdown(Context context,
                                   AutoCompleteTextView dropdown,
                                   String[] items) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_dropdown_item_1line,
                items
        );

        dropdown.setAdapter(adapter);
    }

    public static void saveAndClose(Context context, DocumentReference docRef, Object data, Activity activity) {
        docRef.set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show();
                    activity.finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }


}

