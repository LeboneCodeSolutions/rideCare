package com.example.ridecare.activities.service;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ridecare.R;
import com.example.ridecare.models.batteryReplacementRequest;
import com.example.ridecare.models.engineOverhaulRequest;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class engineOverhaulActivity  extends AppCompatActivity {
    // Static request metadata
    String serviceType = "Engine Overhaul";
    String mechanicID = null;
    String status = "Service Requested";
    // Runtime values
    String vehicleId;
    // Form inputs
    EditText etOdometerReading;
    AutoCompleteTextView ddRunner, ddCheckEngineLight, ddEngineWork,ddOverhaulPreference;
    ConstraintLayout clSubmitEngineOverhaulJob;

    // Firebase instances
    FirebaseFirestore db;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_overhaul);


        //Binding UI Elements

        clSubmitEngineOverhaulJob = findViewById(R.id.clSubmitEngineOverhaulJob);
        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //EditText
        etOdometerReading = findViewById(R.id.etOdometerReading);

        //Dropdowns

        ddRunner = findViewById(R.id.ddRunner);
        String[] isRunning = {
                "Yes",
                "No"
        };
        MyUtils.setDropdown(this,ddRunner,isRunning);


        ddCheckEngineLight = findViewById(R.id.ddCheckEngineLight);
        String[] isCheckEngineLight = {
                "Yes",
                "No"
        };
        MyUtils.setDropdown(this,ddCheckEngineLight,isCheckEngineLight);

        ddEngineWork = findViewById(R.id.ddEngineWork);
        String[] isEngineWorkDone= {
                "Yes",
                "No"
        };
        MyUtils.setDropdown(this,ddEngineWork, isEngineWorkDone);

        ddOverhaulPreference = findViewById(R.id.ddOverhaulPreference);
        String [] overhaulPref = {
                "Minor Rebuild",
                "Full Engine Overhaul",
                "Engine Replacement"

        };
        MyUtils.setDropdown(this,ddOverhaulPreference,overhaulPref);

        clSubmitEngineOverhaulJob.setOnClickListener(v -> submitRequest());
    }


    private void submitRequest(){
        // Extract and sanitize form input values using reusable helper

        String odometerReading = MyUtils.newStr(etOdometerReading);
        String isRunning = MyUtils.newStr(ddRunner);
        String isCheckEngineLightOn = MyUtils.newStr(ddCheckEngineLight);
        String isEngineOpen = MyUtils.newStr(ddEngineWork);
        String overhaulPref = MyUtils.newStr(ddOverhaulPreference);

        // Validate all required inputs before continuing
        if(!MyUtils.requireString(odometerReading,etOdometerReading,"Empty Field"))

        if(!MyUtils.requireString(isRunning,ddRunner, "Option Empty"))
            return;
        if (!MyUtils.requireString(isCheckEngineLightOn, ddCheckEngineLight, "Option Empty"))
            return;
        if(!MyUtils.requireString(isEngineOpen, ddEngineWork, "Option Empty"))
            return;
        if(!MyUtils.requireString(overhaulPref,ddOverhaulPreference,"Option Empty"))
            return;

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
                engineOverhaulRequest request = new engineOverhaulRequest();
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

                // Engine Overhaul Specific Fields
                request.setOdometerReading(odometerReading);
                request.setIsRunning(isRunning);
                request.setIsCheckEngineLightOn(isCheckEngineLightOn);
                request.setIsEngineOpen(isEngineOpen);
                request.setOverhaulPref(overhaulPref);

                // Save request to Firestore using reusable helper
                MyUtils.saveAndClose(this, docRef, request, this);
            });

        });


    }
}
