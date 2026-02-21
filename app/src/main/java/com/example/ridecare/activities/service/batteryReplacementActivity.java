package com.example.ridecare.activities.service;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ridecare.R;
import com.example.ridecare.models.batteryReplacementRequest;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class batteryReplacementActivity extends AppCompatActivity {


    // Static request metadata
    String serviceType = "Battery Replacement";
    String batterySpec;
    String mechanicID = null;
    String status = "Service Requested";
    // Runtime values
    String vehicleId;

    // Form inputs
    AutoCompleteTextView ddVehicleTrim,ddStartStop;
    ConstraintLayout clSubmitBatteryReplaceJob;

    // Firebase instances
    FirebaseFirestore db;
    FirebaseAuth auth;


 @Override
    protected void onCreate(Bundle savedInstanceState){
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_service_battery_replacment);


     //Bind UI Elements
     clSubmitBatteryReplaceJob = findViewById(R.id.clSubmitBatteryReplaceJob);
     // Initialize Firebase
     db = FirebaseFirestore.getInstance();
     auth = FirebaseAuth.getInstance();

     //dropdown setup

     ddStartStop = findViewById(R.id.ddStartStop);
     String[] startStop = {
             "Yes",
             "No"
     };

     MyUtils.setDropdown(this,ddStartStop,startStop);

     ddVehicleTrim = findViewById(R.id.ddVehicleTrim);
     String[] vehicleTrim = {
             "Small",
             "Medium",
             "Large/Diesel",
             "LargeSUV"
     };
     MyUtils.setDropdown(this,ddVehicleTrim, vehicleTrim);
     clSubmitBatteryReplaceJob.setOnClickListener(v -> submitRequest());

 }

    private void submitRequest() {
        // Extract and sanitize form input values using reusable helper
        String vehicleTrim = MyUtils.newStr(ddVehicleTrim);
        String startStop = MyUtils.newStr(ddStartStop);

        // Validate all required inputs before continuing

        if(!MyUtils.requireString(startStop,ddStartStop,"Option Empty"))
            return;
        if (!MyUtils.requireString(vehicleTrim,ddVehicleTrim,"Option Empty"))
            return;


        batterySpec = MyUtils.batterySpecs(vehicleTrim,startStop);


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
                batteryReplacementRequest request = new batteryReplacementRequest();
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

                // Battery Replacement Specific Fields
                request.setVehicleTrim(vehicleTrim);
                request.setHasStartStop(startStop);
                request.setRecommendedBattery(batterySpec);

                // Save request to Firestore using reusable helper
                MyUtils.saveAndClose(this, docRef, request, this);
            });

        });

    }
}