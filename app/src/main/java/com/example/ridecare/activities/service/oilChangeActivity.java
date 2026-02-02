package com.example.ridecare.activities.service;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ridecare.R;
import com.example.ridecare.models.ServiceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class oilChangeActivity extends AppCompatActivity {

    String serviceType = "Oil Change";
    String status = "Service Requested";
    String vehicleId;

    EditText etOdometerReading, etServiceDate;
    ConstraintLayout clSubmitOilChange;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_oil_change);

        etOdometerReading = findViewById(R.id.etOdometerReading);
        etServiceDate = findViewById(R.id.etServiceDate);
        clSubmitOilChange = findViewById(R.id.clSubmitOilChange);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Date picker
        etServiceDate.setOnClickListener(v -> openDatePicker());

        // Submit
        clSubmitOilChange.setOnClickListener(v -> submitRequest());
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
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

    private void submitRequest() {

        String odometerReading = etOdometerReading.getText().toString().trim();
        String serviceDate = etServiceDate.getText().toString().trim();

        if (odometerReading.isEmpty()) {
            etOdometerReading.setError("Odometer reading required");
            return;
        }

        if (serviceDate.isEmpty()) {
            etServiceDate.setError("Service date required");
            return;
        }

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        // Get vehicle ID
        Intent intent = getIntent();
        vehicleId = intent.getStringExtra("vehicleId");
        if (vehicleId == null) {
            vehicleId = intent.getStringExtra("id");
        }

        DocumentReference docRef = db.collection("request_service_vehicle").document();

        db.collection("users").document(uid).get().addOnSuccessListener(userDoc -> {

            if (!userDoc.exists()) {
                Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
                return;
            }

            db.collection("vehicles").document(vehicleId).get().addOnSuccessListener(vehicleDoc -> {

                if (!vehicleDoc.exists()) {
                    Toast.makeText(this, "Vehicle not found", Toast.LENGTH_LONG).show();
                    return;
                }

                ServiceRequest request = new ServiceRequest();
                request.setServiceRequestId(docRef.getId());
                request.setUserId(uid);
                request.setVehicleID(vehicleId);
                request.setVehicleReg(vehicleDoc.getString("registrationNumber"));
                request.setVinNumber(vehicleDoc.getString("vin"));
                request.setVehicleMake(vehicleDoc.getString("make"));
                request.setVehicleModel(vehicleDoc.getString("model"));
                request.setServiceType(serviceType);
                request.setOdometerReading(odometerReading);
                request.setServicePrevDate(serviceDate); // ✅ FIXED
                request.setStatus(status);

                docRef.set(request)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Service booked successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());

            });

        });
    }
}
