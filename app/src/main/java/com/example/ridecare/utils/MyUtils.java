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

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ridecare.R;
import com.example.ridecare.activities.client.ServiceListActivity;
import com.example.ridecare.activities.client.VehicleListActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
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


    public static int intParser(String strValue, EditText etValue) {
        // Function - Converts string value into a integer
        try {
            return Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            etValue.setError("Invalid Integer");
            return 0 ;
        }
    }

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


// test function toastMake error used in add vehicle acticity


    public static void eDisplay(Context context, String field){
       String errText = "Error: ";
        Toast.makeText(context, errText + field, Toast.LENGTH_SHORT).show();
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
            vehicleId = intent.getStringExtra("id");
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


public static boolean errEmptyVal(MutableLiveData<String> saveStatus, String value){

    if(value == null || value.isEmpty()){
        saveStatus.setValue("Error" + value);
        return false;
    }
    return true;
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

    // =============================
    //Tow Truck
    // =============================
    public static String towTruckType(String drivetrain, String modification, String steeringLocked, String canRoll) {

        // Oversized — always needs a low-bed loader
        if ("Oversized".equals(modification)) {
            return "Low-Bed Loader Truck Required";
        }

        // AWD / Not Sure — flatbed required to protect drivetrain
        if ("AWD".equals(drivetrain) || "NotSure".equals(drivetrain)) {
            return "Flatbed Truck Required";
        }

        // Wheels can't roll — flatbed only
        if ("No".equals(canRoll)) {
            return "Flatbed Truck Required";
        }

        // Lowered or Modified — boom truck needed for access
        if ("Lowered".equals(modification) || "Modified".equals(modification)) {
            return "Boom Truck Required";
        }

        // RWD + steering locked + can roll — dolly tow
        if ("RWD".equals(drivetrain) && "Yes".equals(steeringLocked) && "Yes".equals(canRoll)) {
            return "Dolly Truck Required";
        }

        // FWD — standard wheel lift (lift front wheels)
        if ("FWD".equals(drivetrain) && "No".equals(steeringLocked) && "Yes".equals(canRoll)) {
            return "Wheel Lift Truck Required";
        }

        // RWD — standard wheel lift (lift rear wheels)
        if ("RWD".equals(drivetrain) && "No".equals(steeringLocked) && "Yes".equals(canRoll)) {
            return "Wheel Lift Truck Required";
        }

        // Fallback — flatbed is always the safest default
        return "Flatbed Truck Required";
    }

    public static String vehicleCondition(String incident, String isDamaged, String wheelsIntact) {

        // Accident with damage — flatbed recommended, vehicle may not be safe to roll
        if ("Accident".equals(incident) && "Yes".equals(isDamaged)) {
            return "Accident - Damaged - Flatbed Recommended";
        }

        // Accident but no visible damage — still inspect before towing
        if ("Accident".equals(incident) && "No".equals(isDamaged)) {
            return "Accident - No Known Damage - Inspect Before Tow";
        }

        // Wheels not intact — cannot roll, flatbed only
        if ("No".equals(wheelsIntact)) {
            return "Wheel Damage - Flatbed Only";
        }

        // Flat tire — can be changed on site or flatbed if no spare
        if ("Flat Tyre".equals(incident)) {
            return "Flat Tyre - Roadside Change or Flatbed";
        }

        // Dead battery — jump start attempt first, tow if unsuccessful
        if ("Dead Battery".equals(incident)) {
            return "Dead Battery - Jump Start First";
        }

        // Standard breakdown, wheels intact, no damage, not stuck
        if ("Breakdown".equals(incident) && "No".equals(isDamaged) && "Yes".equals(wheelsIntact)) {
            return "Standard Breakdown - Ready to Tow";
        }

        // Breakdown with damage
        if ("Breakdown".equals(incident) && "Yes".equals(isDamaged)) {
            return "Breakdown - Damaged - Flatbed Recommended";
        }

        return "Condition Unknown - Assess On Site";
    }

    public static String paymentMethod(String paymentType, String hasAssistance, String needsInvoice) {

        // Roadside assistance or insurance covering the tow
        if ("Yes".equals(hasAssistance)) {
            if ("Yes".equals(needsInvoice)) {
                return "Covered - Send Invoice to Insurer";
            }
            return "Covered - Confirm with Assistance Provider";
        }

        // Cash payment
        if ("Cash".equals(paymentType)) {
            if ("Yes".equals(needsInvoice)) {
                return "Cash - Receipt Required";
            }
            return "Cash - No Receipt Needed";
        }

        // Card payment
        if ("Card".equals(paymentType)) {
            if ("Yes".equals(needsInvoice)) {
                return "Card - Invoice Required";
            }
            return "Card - No Invoice Needed";
        }

        // EFT payment
        if ("EFT".equals(paymentType)) {
            if ("Yes".equals(needsInvoice)) {
                return "EFT - Invoice Required";
            }
            return "EFT - Confirm Bank Details On Site";
        }

        return "Payment Method Unknown - Confirm On Arrival";
    }

    public static String towCondition(String roadType, String isVehicleAccessible, String dayPeriod ){

        if("No".equals(isVehicleAccessible)){
            return "High Risk";
        }
        if("Parking Lot / Private Property".equals(roadType) && "Night-Time".equals(dayPeriod)){
            return "High Risk";
        }

        if("Off-Road / Gravel / Farm".equals(roadType) && "Night-Time".equals(dayPeriod)){
            return "High Risk";
        }

        return "Low Risk";
    }




// Test functions

    /* public static String regNumberValidation(String regNum){
        1. regex checker if that input has char A - Z and 0 - 9
            2. max char 6 with province code [gp/mp/l/zn]
            3. if identified as custom plate max 7 chars excl prov code
            4. Western Cape has a different format the province code is first followeed by digts [123-679]
            5. Custom plates in western cape always use WC in the end

    };*/
    public static String mapYesNoSelection(String currentValue, String userSelection) {
        if (userSelection.equals("Yes")) {
            currentValue = "Yes";
        } else if (userSelection.equals("No")) {
            currentValue = "No";
        } else {
            currentValue = "not selected";
        }
        return currentValue;
    }

    public static String getSelectedChipText(AppCompatActivity activity, int chipGroupId) {
        ChipGroup chipGroup = activity.findViewById(chipGroupId);
        int selectedChipId = chipGroup.getCheckedChipId();

        if (selectedChipId != View.NO_ID) {
            Chip selectedChip = activity.findViewById(selectedChipId);
            return selectedChip.getText().toString();
        }
        return null;
    }
    public static void setupYesNo(Context context,
                                  MaterialButton yes,
                                  MaterialButton no,
                                  int yesColor,
                                  int noColor,
                                  Runnable onChanged) {

        View.OnClickListener resetAndApply = v -> {
            // Reset both buttons
            for (MaterialButton btn : new MaterialButton[]{yes, no}) {
                btn.setTextColor(ContextCompat.getColor(context, R.color.text_muted));
                btn.setStrokeColor(ContextCompat.getColorStateList(context, R.color.bg_border));
                btn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.bg_steel));
            }
            // Apply active colour to the tapped button
            MaterialButton tapped = (MaterialButton) v;
            int activeColor = (tapped == yes) ? yesColor : noColor;
            tapped.setTextColor(ContextCompat.getColor(context, activeColor));
            tapped.setStrokeColor(ContextCompat.getColorStateList(context, activeColor));

            if (onChanged != null) onChanged.run();
        };

        yes.setOnClickListener(resetAndApply);
        no.setOnClickListener(resetAndApply);
    }

    public static void checkVehicleRequestLimit(String vehicleId, OnLimitCheckListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // 👈 get instance directly

        db.collection("request_service_vehicle")
                .whereEqualTo("vehicleID", vehicleId)
                .whereIn("status", Arrays.asList("pending", "active", "in_progress", "Service Requested"))
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int activeCount = querySnapshot.size();
                    if (activeCount >= 3) {
                        listener.onLimitReached(activeCount);
                    } else {
                        listener.onCanProceed(activeCount);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ServiceRequest", "Failed to check request limit", e);
                    listener.onError(e);
                });
    }
    public interface OnLimitCheckListener {
        void onCanProceed(int currentCount);
        void onLimitReached(int currentCount);
        void onError(Exception e);
    }




    public static void requestLimitReachMessage(int currentCount, Context context, Class<?> targetClass){
        new AlertDialog.Builder(context)
                .setTitle("Request Limit Reached")
                .setMessage("This vehicle already has " + currentCount + " active service requests.\n\n" +
                        "Please cancel an existing request or wait for a job to be completed before booking again.")
                .setPositiveButton("View My Requests", (dialog, which) -> {
                    context.startActivity(new Intent(context, targetClass));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    public static void toastMakeErrorRequestLimit (Context context){
        Toast.makeText(context,
                "Could not verify request limit. Please try again.",
                Toast.LENGTH_SHORT).show();
    }



    // Year limit

    public static void yearLimit(MutableLiveData<String> saveStatus, int year){
        if (year == 0) {
            saveStatus.setValue("error: Year Entry 0");
            return;
        } else if (year < 1970 ) {saveStatus.setValue("error: Year Limit");return;}
    }
}
