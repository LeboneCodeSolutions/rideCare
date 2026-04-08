package com.example.ridecare.activities.client;

import static com.example.ridecare.utils.MyUtils.checkVehicleRequestLimit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ridecare.R;
import com.example.ridecare.activities.service.batteryReplacementActivity;
import com.example.ridecare.activities.service.brakeSyetemRepairActivity;
import com.example.ridecare.activities.service.engineOverhaulActivity;
import com.example.ridecare.activities.service.oilChangeActivity;
import com.example.ridecare.activities.service.towDispatchActivity;
import com.example.ridecare.activities.service.tyreRepairActivity;
import com.example.ridecare.databinding.ActivityBookServiceV2Binding;
import com.example.ridecare.models.Vehicle;
import com.example.ridecare.models.ServiceRequest;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookServiceActivity extends AppCompatActivity {

    private ActivityBookServiceV2Binding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String vehicleId;
    String mechanicID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Replace setContentView with Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_service_v2);

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

        // Fetch vehicle from Firestore and bind to card
        loadVehicle(vehicleId);

        checkVehicleRequestLimit(vehicleId, new MyUtils.OnLimitCheckListener() {

            @Override
            public void onCanProceed(int currentCount) {
                binding.containerOilChange.setOnClickListener(v -> {
                    Intent intent = new Intent(BookServiceActivity.this, oilChangeActivity.class);
                    intent.putExtra("vehicleId", vehicleId);
                    startActivity(intent);
                });

                binding.containerTyreReplacement.setOnClickListener(v -> {
                    Intent intent = new Intent(BookServiceActivity.this, tyreRepairActivity.class);
                    intent.putExtra("vehicleId", vehicleId);
                    startActivity(intent);
                });

                binding.containerBrakingSystem.setOnClickListener(v -> {
                    Intent intent = new Intent(BookServiceActivity.this, brakeSyetemRepairActivity.class);
                    intent.putExtra("vehicleId", vehicleId);
                    startActivity(intent);
                });

                binding.containerBattteryReplacement.setOnClickListener(v -> {
                    Intent intent = new Intent(BookServiceActivity.this, batteryReplacementActivity.class);
                    intent.putExtra("vehicleId", vehicleId);
                    startActivity(intent);
                });

                binding.containerOverhaul.setOnClickListener(v -> {
                    Intent intent = new Intent(BookServiceActivity.this, engineOverhaulActivity.class);
                    intent.putExtra("vehicleId", vehicleId);
                    startActivity(intent);
                });

                binding.containerTowing.setOnClickListener(v -> {
                    Intent intent = new Intent(BookServiceActivity.this, towDispatchActivity.class);
                    intent.putExtra("vehicleId", vehicleId);
                    startActivity(intent);
                });
            }

            @Override
            public void onLimitReached(int currentCount) {
                MyUtils.requestLimitReachMessage(currentCount, BookServiceActivity.this, ServiceListActivity.class);
            }

            @Override
            public void onError(Exception e) {
                MyUtils.toastMakeErrorRequestLimit(BookServiceActivity.this);
            }
        });
    }

    private void loadVehicle(String vehicleId) {
        db.collection("vehicles")
                .document(vehicleId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Vehicle vehicle = documentSnapshot.toObject(Vehicle.class);
                        binding.setVehicle(vehicle);
                    } else {
                        Toast.makeText(this, "Vehicle not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load vehicle", Toast.LENGTH_SHORT).show()
                );
    }
}