/**
 * MyUtils.java
 *
 * Central utility class for reusable helper methods used across the RideCare app.
 *
 * Responsibilities:
 * - Form input handling (EditText → String conversion)
 * - Field validation
 * - DatePicker helpers
 * - Firebase-related checks
 * - Dropdown setup
 * - Common UI actions (Toast, navigation, etc.)
 *
 * Purpose:
 * Reduce duplicated code across Activities and keep business logic clean.
 *
 * Author: Lebone Ngobese
 * Project: RideCare
 */

package com.example.ridecare.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;

public class MyUtils {

    // =============================
    // DATE HELPERS
    // =============================

    /**
     * Opens a DatePicker dialog and inserts the selected date into the provided EditText.
     */
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

    // =============================
    // TEXT INPUT HELPERS
    // =============================

    /**
     * Safely extracts trimmed text from an EditText.
     * Prevents null pointer crashes.
     */
    public static String newStr(EditText text) {
        if (text == null || text.getText() == null) return "";
        return text.getText().toString().trim();
    }

    // =============================
    // VALIDATION HELPERS
    // =============================

    /**
     * Validates that a string is not empty and displays an error on the field if it is.
     */
    public static boolean requireString(String value, EditText editText, String errorMessage) {
        if (value == null || value.isEmpty()) {
            editText.setError(errorMessage);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Ensures a Firestore document exists before proceeding.
     */
    public static boolean requireDocument(DocumentSnapshot doc, Context context, String message) {
        if (doc == null || !doc.exists()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // =============================
    // FIREBASE HELPERS
    // =============================

    /**
     * Checks if a user is logged in and returns the UID.
     */
    public static String UidCheck(Context context, FirebaseAuth auth) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(context, "Not logged in", Toast.LENGTH_SHORT).show();
            return null;
        }
        return auth.getCurrentUser().getUid();
    }

    /**
     * Retrieves vehicleId from Intent safely.
     */
    public static String vehicleIdCheck(Activity activity) {
        Intent intent = activity.getIntent();
        String vehicleId = intent.getStringExtra("vehicleId");

        if (vehicleId == null) {
            vehicleId = intent.getStringExtra("id");   // FIXED: was missing assignment
        }

        if (vehicleId == null || vehicleId.isEmpty()) {
            Toast.makeText(activity, "Vehicle ID missing. Please reopen vehicle.", Toast.LENGTH_LONG).show();
            activity.finish();
            return null;
        }
        return vehicleId;
    }

    /**
     * Saves data to Firestore and closes the activity on success.
     */
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

    // =============================
    // DROPDOWN HELPERS
    // =============================

    /**
     * Attaches an ArrayAdapter to an AutoCompleteTextView dropdown.
     */
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



    public static String batterySpecs(String vehicleSize, String startStop) {
        if ("Small".equals(vehicleSize)) {
            if ("Yes".equals(startStop)) {
                return "H5 | 60–70Ah | AGM";
            }
            return "H4/H5 | 40–60Ah | Lead-acid";
        }
        if ("Medium".equals(vehicleSize)) {
            if ("Yes".equals(startStop)) {
                return "H6 | 70–80Ah | AGM";
            }
            return "H5/H6 | 60–75Ah | Lead-acid";
        }
        if("Large/Diesel".equals(vehicleSize)){
            if ("Yes".equals(startStop)){
                return "H7/H8 | 80–95Ah | AGM";
            }
            return "H6/H7 | 70–90Ah | Lead-acid";
        }

        if("LargeSUV".equals(vehicleSize)){
            if ("Yes".equals(startStop)){
                return " H8/H9 | 95–120Ah | AGM";
            }
            return "N70/H8 | 90–120Ah | Lead-acid";
        }

        return "Battery size unknown";
    }

}
