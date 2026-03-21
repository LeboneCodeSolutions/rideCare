package com.example.ridecare.activities.client;

import static com.example.ridecare.utils.MyUtils.checkVehicleRequestLimit;

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
import com.example.ridecare.activities.service.towDispatchActivity;
import com.example.ridecare.activities.service.tyreRepairActivity;
import com.example.ridecare.models.BrakeSystemRequest;
import com.example.ridecare.models.ServiceRequest;
import com.example.ridecare.utils.MyUtils;
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
        checkVehicleRequestLimit(vehicleId, new MyUtils.OnLimitCheckListener(){

            @Override
            public void onCanProceed(int currentCount) {
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
                    Intent intent = new Intent(BookServiceActivity.this, towDispatchActivity.class);
                    intent.putExtra("vehicleId", vehicleId);
                    startActivity(intent);
                });
            }

            @Override
            public void onLimitReached(int currentCount) {
                MyUtils.requestLimitReachMessage(currentCount,BookServiceActivity.this, ServiceListActivity.class);
            }

            @Override
            public void onError(Exception e) {
                MyUtils.toastMakeErrorRequestLimit(BookServiceActivity.this);
            }
        });

    }

}
