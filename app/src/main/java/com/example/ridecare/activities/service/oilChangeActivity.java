package com.example.ridecare.activities.service;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ridecare.utils.MyUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.ridecare.utils.MyUtils;
import com.example.ridecare.R;
import com.example.ridecare.models.ServiceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class oilChangeActivity extends AppCompatActivity {

    String serviceType = "Oil Change";
    String status = "Service Requested";
    String mechanicID = null;
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
        etServiceDate.setOnClickListener(v ->  MyUtils.openDatePicker(this, etServiceDate));

        // Submit
        clSubmitOilChange.setOnClickListener(v -> submitRequest());
    }



    private void submitRequest() {

        String odometerReading = MyUtils.newStr(etOdometerReading);
        String serviceDate = MyUtils.newStr(etServiceDate);

        if (!MyUtils.requireString(odometerReading, etOdometerReading,"Odometer Reading Required")) return;

        if (!MyUtils.requireString(serviceDate, etServiceDate, "Service date required")) return;


        String uid = MyUtils.UidCheck(this, auth);
        if (uid == null) return;

        vehicleId = MyUtils.vehicleIdCheck(this);
        if (vehicleId == null) return;

        DocumentReference docRef = db.collection("request_service_vehicle").document();

        db.collection("users").document(uid).get().addOnSuccessListener(userDoc -> {
            if (!MyUtils.requireDocument(userDoc, this, "User not found")) return;

            db.collection("vehicles").document(vehicleId).get().addOnSuccessListener(vehicleDoc -> {
                if (!MyUtils.requireDocument(vehicleDoc, this, "Vehicle")) return;


                ServiceRequest request = new ServiceRequest();
                request.setServiceRequestId(docRef.getId());
                request.setUserId(uid);
                request.setVehicleID(vehicleId);
                request.setVehicleReg(vehicleDoc.getString("registrationNumber"));
                request.setVinNumber(vehicleDoc.getString("vin"));
                request.setVehicleMake(vehicleDoc.getString("make"));
                request.setVehicleModel(vehicleDoc.getString("model"));
                request.setServiceType(serviceType);

                request.setMechanicId(mechanicID);
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
