package com.example.ridecare.activities.service;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ridecare.R;
import com.example.ridecare.models.OilChangeRequest;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * oilChangeActivity
 *
 * Handles oil change service request submission.
 * Collects user input, validates fields, retrieves user & vehicle data,
 * and saves an OilChangeRequest to Firestore.
 */
public class oilChangeActivity extends AppCompatActivity {

    // Static request metadata
    String serviceType = "Oil Change";
    String status = "Service Requested";
    String mechanicID = null;

    // Runtime values
    String vehicleId;

    // Form inputs
    EditText etOdometerReading, etServiceDate;

    // Submit container acting as a button
    ConstraintLayout clSubmitOilChange;

    // Firebase instances
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_oil_change);

        // Bind UI elements
        etOdometerReading = findViewById(R.id.etOdometerReading);
        etServiceDate = findViewById(R.id.etServiceDate);
        clSubmitOilChange = findViewById(R.id.clSubmitOilChange);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Attach reusable DatePicker helper to service date field
        etServiceDate.setOnClickListener(v ->
                MyUtils.openDatePicker(this, etServiceDate)
        );

        // Handle submit action
        clSubmitOilChange.setOnClickListener(v -> submitRequest());
    }

    /**
     * Handles oil change request submission:
     * - Extracts and validates form inputs
     * - Ensures user is authenticated
     * - Retrieves vehicle details
     * - Builds OilChangeRequest model
     * - Saves request to Firestore
     */
    private void submitRequest() {

        // Extract sanitized input values using reusable helper
        String odometerReading = MyUtils.newStr(etOdometerReading);
        String serviceDate = MyUtils.newStr(etServiceDate);

        // Validate required inputs before proceeding
        if (!MyUtils.requireString(odometerReading, etOdometerReading, "Odometer Reading Required")) return;
        if (!MyUtils.requireString(serviceDate, etServiceDate, "Service date required")) return;

        // Ensure user is logged in and retrieve UID
        String uid = MyUtils.UidCheck(this, auth);
        if (uid == null) return;

        // Retrieve vehicle ID safely from Intent
        vehicleId = MyUtils.vehicleIdCheck(this);
        if (vehicleId == null) return;

        // Create new Firestore document reference
        DocumentReference docRef = db.collection("request_service_vehicle").document();

        // Verify user exists before continuing
        db.collection("users").document(uid).get().addOnSuccessListener(userDoc -> {
            if (!MyUtils.requireDocument(userDoc, this, "User not found")) return;

            // Fetch vehicle details for request metadata
            db.collection("vehicles").document(vehicleId).get().addOnSuccessListener(vehicleDoc -> {
                if (!MyUtils.requireDocument(vehicleDoc, this, "Vehicle")) return;

                // Create oil change request model
                OilChangeRequest request = new OilChangeRequest();

                // Base service request fields
                request.setServiceRequestId(docRef.getId());
                request.setUserId(uid);
                request.setVehicleID(vehicleId);
                request.setServiceType(serviceType);
                request.setStatus(status);
                request.setMechanicId(mechanicID);

                // Vehicle metadata
                request.setVehicleReg(vehicleDoc.getString("registrationNumber"));
                request.setVinNumber(vehicleDoc.getString("vin"));
                request.setVehicleMake(vehicleDoc.getString("make"));
                request.setVehicleModel(vehicleDoc.getString("model"));

                // Oil-specific fields
                request.setOdometerReading(odometerReading);
                request.setPrevServiceDate(serviceDate);

                // Save request to Firestore using reusable helper
                MyUtils.saveAndClose(this, docRef, request, this);
            });
        });
    }
}
