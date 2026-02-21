package com.example.ridecare.activities.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.example.ridecare.R;
import com.example.ridecare.activities.service.batteryReplacementActivity;
import com.example.ridecare.activities.service.brakeSyetemRepairActivity;
import com.example.ridecare.activities.service.engineOverhaulActivity;
import com.example.ridecare.activities.service.oilChangeActivity;
import com.example.ridecare.activities.service.tyreRepairActivity;
import com.example.ridecare.models.BrakeSystemRequest;
import com.example.ridecare.models.ServiceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookServiceActivity extends AppCompatActivity {
    EditText etDescription;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ConstraintLayout containerOilChange, containerTyreReplacement, containerBrakingSystem, containerBattteryReplacement, containerOverhaul, containerTowing;


    TextView tvOilChange;
    String  selectedService;
    String vehicleId;
    // Mechanic assigned later when job is accepted
    String mechanicID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service_v2);


        //Service Types
        containerOilChange = findViewById(R.id.containerOilChange);
        containerTyreReplacement = findViewById(R.id.containerTyreReplacement);
        containerBrakingSystem = findViewById(R.id.containerBrakingSystem);
        containerBattteryReplacement = findViewById(R.id.containerBattteryReplacement);
        containerOverhaul = findViewById(R.id.containerOverhaul);
        containerTowing = findViewById(R.id.containerTowing);

        tvOilChange = findViewById(R.id.tvOilChange);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        vehicleId = getIntent().getStringExtra("vehicleId");

        if (vehicleId == null || vehicleId.isEmpty()) {
            vehicleId = getIntent().getStringExtra("id");
        }

        if (vehicleId == null || vehicleId.isEmpty()) {
            Toast.makeText(this, "Vehicle not selected", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Developer Note: Create a cancel service button


        // Developer Note: Set functionality on the containers,
        //Preset the container values - staus = done
        // user can't select more than 1 container at a time
        // and once user confirms the oder
        containerOilChange.setOnClickListener(v -> {
            Intent intent = new Intent(BookServiceActivity.this, oilChangeActivity.class);
            intent.putExtra("vehicleId", vehicleId);
            startActivity(intent);
        });

        containerTyreReplacement.setOnClickListener(v -> {
            Intent intent = new Intent(BookServiceActivity.this, tyreRepairActivity.class);
            intent.putExtra("vehicleId", vehicleId);
            startActivity(intent);
        });

        containerBrakingSystem.setOnClickListener(v -> {
            Intent intent = new Intent(BookServiceActivity.this, brakeSyetemRepairActivity.class);
            intent.putExtra("vehicleId", vehicleId);
            startActivity(intent);
        });

        containerBattteryReplacement.setOnClickListener(v -> {
            Intent intent = new Intent(BookServiceActivity.this, batteryReplacementActivity.class);
            intent.putExtra("vehicleId", vehicleId);
            startActivity(intent);
        });

        containerOverhaul.setOnClickListener(v -> {
            Intent intent = new Intent(BookServiceActivity.this, engineOverhaulActivity.class);
            intent.putExtra("vehicleId", vehicleId);
            startActivity(intent);
        });

        containerTowing.setOnClickListener(v -> {
            selectedService = "24/7 Towing Request";
        });
    }

}
  /*  private void submitRequest() {


        if (selectedService == null) {
            Toast.makeText(this, "Please Select service type", Toast.LENGTH_SHORT).show();
            return;
        }
        String desc = etDescription.getText().toString().trim();

        if (desc.isEmpty()) {
            etDescription.setError("Description required");
            return;
        }
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = auth.getCurrentUser().getUid();
        String clientEmail = auth.getCurrentUser().getEmail();

        // ✅ GET VEHICLE ID FIRST - BEFORE USING IT!
        Intent intent = getIntent();
        vehicleId = intent.getStringExtra("vehicleId");
        if (vehicleId == null) {
            vehicleId = intent.getStringExtra("id");
        }

        if (vehicleId == null || vehicleId.isEmpty()) {
            Toast.makeText(this, "Vehicle ID not provided", Toast.LENGTH_LONG).show();
            return;
        }

        // 🔹 Create Firestore document reference FIRST
        DocumentReference docRef = db.collection("request_service_vehicle").document();

        // 🔹 Fetch user profile
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(userDoc -> {

                    if (!userDoc.exists()) {
                        Toast.makeText(this, "User profile not found", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // 🔹 Fetch vehicle (NOW vehicleId has a value!)
                    db.collection("vehicles")
                            .document(vehicleId)
                            .get()
                            .addOnSuccessListener(vehicleDoc -> {

                                if (!vehicleDoc.exists()) {
                                    Toast.makeText(this, "Vehicle not found", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // ✅ REMOVED - Already got vehicleId above
                                // Don't try to get it again here!

                                String vehicleReg = vehicleDoc.getString("registrationNumber");
                                String vinNumber = vehicleDoc.getString("vin");
                                String vehicleMake = vehicleDoc.getString("make");
                                String vehicleModel  = vehicleDoc.getString("model");

                                // 🔹 Build ServiceRequest
                                ServiceRequest request = new ServiceRequest();
                                request.setVehicleID(vehicleId);
                                request.setUserId(uid);
                                request.setVehicleReg(vehicleReg);
                                request.setVinNumber(vinNumber);
                                request.setVehicleMake(vehicleMake);
                                request.setVehicleModel(vehicleModel);
                                request.setServiceType(serviceType);
                                request.setStatus(status);
                                request.setClientDescription(desc);
                                request.setServiceRequestId(docRef.getId());

                                // 🔹 WRITE TO FIRESTORE
                                docRef.set(request)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(
                                                    this,
                                                    "Service request submitted!",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(
                                                        this,
                                                        "Write failed: " + e.getMessage(),
                                                        Toast.LENGTH_LONG
                                                ).show()
                                        );

                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(
                                            this,
                                            "Vehicle fetch failed: " + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "User fetch failed: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
    }
}
*/