package com.example.ridecare.activities.service;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ridecare.R;
import com.example.ridecare.models.BrakeSystemRequest;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class brakeSyetemRepairActivity extends AppCompatActivity {


    // Static request metadata
    String serviceType = "Brake Job";
    String status = "Service Requested";
    String mechanicID = null;

    // Runtime values
    String vehicleId;

    // Form inputs
    EditText etOdometerReading, etServiceDate;
    AutoCompleteTextView ddBrakeSystemQ1, ddBrakeSystemQ2, ddBrakeSystemQ3;
    ConstraintLayout clSubmitBrakeJob;

    // Firebase instances
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_brake_system_repair);
        // Bind UI elements
        etOdometerReading = findViewById(R.id.etOdometerReading);
        etServiceDate = findViewById(R.id.etServiceDate);
        clSubmitBrakeJob = findViewById(R.id.clSubmitBrakeJob);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Attach reusable DatePicker helper to service date field
        etServiceDate.setOnClickListener(v ->
                MyUtils.openDatePicker(this, etServiceDate)
        );

        //dropdown setup
        ddBrakeSystemQ1 = findViewById(R.id.ddBrakeSystemQ1);
        String[] Question1 = {
                "Yes",
                "No"
        };
        MyUtils.setDropdown(this, ddBrakeSystemQ1, Question1);

        ddBrakeSystemQ2 = findViewById(R.id.ddBrakeSystemQ2);
        String[] Question2 = {
                "1",
                "2",
                "3",
                "4",
                "5"
        };
        MyUtils.setDropdown(this, ddBrakeSystemQ2, Question2);

        ddBrakeSystemQ3 = findViewById(R.id.ddBrakeSystemQ3);
        String[] Question3 = {
                "Front",
                "Rear",
                "Both"
        };
        MyUtils.setDropdown(this, ddBrakeSystemQ3, Question3);

        clSubmitBrakeJob = findViewById(R.id.clSubmitBrakeJob);
        clSubmitBrakeJob.setOnClickListener(v -> submitRequest());
    }

    /**
     * Handles form submission:
     * - Extracts and validates all user inputs
     * - Builds tyre size string
     * - Retrieves authenticated user + vehicle data
     * - Creates and saves Request to Firestore
     */


    private void submitRequest() {
        // Extract and sanitize form input values using reusable helper
        String odomedtreReadString = MyUtils.newStr(etOdometerReading);
        String serviceDateString = MyUtils.newStr(etServiceDate);
        String brakeQ1String = MyUtils.newStr(ddBrakeSystemQ1);
        String brakeQ2String = MyUtils.newStr(ddBrakeSystemQ2);
        String brakeQ3String = MyUtils.newStr(ddBrakeSystemQ3);

        // Validate all required inputs before continuing

        if (!MyUtils.requireString(odomedtreReadString, etOdometerReading, "Please fill in  odometer reading!"))
            return;
        if (!MyUtils.requireString(serviceDateString, etServiceDate, "Service Date Required"))
            return;
        if (!MyUtils.requireString(brakeQ1String, ddBrakeSystemQ1, "Question 1 Empty")) return;
        if (!MyUtils.requireString(brakeQ2String, ddBrakeSystemQ2, "Question 2 Empty")) return;
        if (!MyUtils.requireString(brakeQ3String, ddBrakeSystemQ3, "Question 3 Empty")) return;

        // Ensure user is authenticated before proceeding
        String uid = MyUtils.UidCheck(this, auth);
        if (uid == null) return;

        // Retrieve vehicle ID from Intent safely
        vehicleId = MyUtils.vehicleIdCheck(this);
        if (vehicleId == null) return;

        // Create new Firestore document reference
        DocumentReference docRef = db.collection("request_service_vehicle").document();
        // Fetch user document for validation
        db.collection("users").document(uid).get().addOnSuccessListener(userDoc -> {

            if (!MyUtils.requireDocument(userDoc, this, "User not found")) return;

            // Fetch vehicle document for vehicle details
            db.collection("vehicles").document(vehicleId).get().addOnSuccessListener(vehicleDoc -> {

                if (!MyUtils.requireDocument(vehicleDoc, this, "Vehicle")) return;
                // Create new  repair request model
                BrakeSystemRequest request = new BrakeSystemRequest();

                // Base service request fields (inherited structure)
                request.setServiceRequestId(docRef.getId());
                request.setUserId(uid);
                request.setVehicleID(vehicleId);
                request.setMechanicId(mechanicID);
                request.setStatus(status);
                request.setServiceType(serviceType);

                // Vehicle metadata
                request.setVehicleReg(vehicleDoc.getString("registrationNumber"));
                request.setVinNumber(vehicleDoc.getString("vin"));
                request.setVehicleMake(vehicleDoc.getString("make"));
                request.setVehicleModel(vehicleDoc.getString("model"));

                //Brake System Specific Fields
                request.setBrakeNoise(brakeQ1String);
                request.setOdometerReading(odomedtreReadString);
                request.setBrakeNoiseRating(brakeQ2String);
                request.setPrevBrakeJob(serviceDateString);
                request.setBrakeJobSide(brakeQ3String);


                // Save request to Firestore using reusable helper
                MyUtils.saveAndClose(this, docRef, request, this);

            });

        });

    }
}