package com.example.ridecare.activities.client;

import static android.icu.lang.UCharacter.toUpperCase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ridecare.R;
import com.example.ridecare.activities.service.*;
import com.example.ridecare.models.Vehicle;
import com.example.ridecare.utils.MyUtils;
import com.example.ridecare.viewmodels.VehicleDetailsViewModel;

public class ViewVehicleDetailsActivity extends AppCompatActivity {

    // UI Components
    ImageButton ibBack, btnMore, ibCopyVin;
    TextView tvMakeModel, tvSubtitle, tvMileage, tvRegistration, tvYear, tvBodyType, tvFuel, tvVin, tvViewAll;
    LinearLayout btnOilChange, btnTyreRepair, btnBrakes, btnBattery, btnOverhaul, btnTowing;
    FrameLayout flEditMileage;

    VehicleDetailsViewModel viewModel;

    String vehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        viewModel = new ViewModelProvider(this).get(VehicleDetailsViewModel.class);

        viewBinder();
        getVehicleId();
        setupListeners();
        observeViewModel();

        viewModel.loadVehicle(vehicleId); // ✅ only call
    }


    private void getVehicleId() {
        vehicleId = getIntent().getStringExtra("vehicleId");

        if (vehicleId == null || vehicleId.isEmpty()) {
            vehicleId = getIntent().getStringExtra("id");
        }

        if (vehicleId == null || vehicleId.isEmpty()) {
            Toast.makeText(this, "Vehicle not selected", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Bind Views
    private void viewBinder() {
        btnOilChange = findViewById(R.id.btnOilChange);
        btnTyreRepair = findViewById(R.id.btnTyreRepair);
        btnBrakes = findViewById(R.id.btnBrakes);
        btnBattery = findViewById(R.id.btnBattery);
        btnOverhaul = findViewById(R.id.btnOverhaul);
        btnTowing = findViewById(R.id.btnTowing);

        flEditMileage = findViewById(R.id.flEditMileage);

        tvMakeModel = findViewById(R.id.tvMakeModel);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvMileage = findViewById(R.id.tvMileage);
        tvRegistration = findViewById(R.id.tvRegistration);
        tvYear = findViewById(R.id.tvYear);
        tvBodyType = findViewById(R.id.tvBodyType);
        tvFuel = findViewById(R.id.tvFuel);
        tvVin  = findViewById(R.id.tvVin);
        tvViewAll = findViewById(R.id.tvViewAll);

        ibBack = findViewById(R.id.ibBack);
        btnMore = findViewById(R.id.btnMore);
        ibCopyVin = findViewById(R.id.ibCopyVin);
    }

    // 🔥 Click listeners → send events to ViewModel
    private void setupListeners() {

        ibBack.setOnClickListener(v -> finish());

        ibCopyVin.setOnClickListener(v ->
                Toast.makeText(this, "VIN Copied", Toast.LENGTH_SHORT).show()
        );

        flEditMileage.setOnClickListener(v ->
                Toast.makeText(this, "Edit Mileage Clicked", Toast.LENGTH_SHORT).show()
        );

        btnOilChange.setOnClickListener(v ->
                viewModel.onOilChangeClicked(vehicleId)
        );

        btnTyreRepair.setOnClickListener(v ->
                viewModel.onTyreRepairClicked(vehicleId)
        );

        btnBrakes.setOnClickListener(v ->
                viewModel.onBrakesClicked(vehicleId)
        );

        btnBattery.setOnClickListener(v ->
                viewModel.onBatteryClicked(vehicleId)
        );

        btnOverhaul.setOnClickListener(v ->
                viewModel.onOverhaulClicked(vehicleId)
        );

        btnTowing.setOnClickListener(v ->
                viewModel.onTowingClicked(vehicleId)
        );
    }


    private void observeViewModel() {

        // Vehicle data
        viewModel.getVehicle().observe(this, vehicle -> {
            if (vehicle != null) {
                tvMakeModel.setText(toUpperCase(vehicle.getMake() + " " + vehicle.getModel()));
                tvMileage.setText(String.valueOf(vehicle.getMileage()));
                tvRegistration.setText(vehicle.getRegistrationNumber());
                tvYear.setText(String.valueOf(vehicle.getYear()));
                tvFuel.setText(vehicle.getFuelType());
                tvVin.setText(vehicle.getVin());
            }
        });

        // Navigation
        viewModel.openOilChange().observe(this, open -> {
            if (Boolean.TRUE.equals(open)) {
                Intent intent = new Intent(this, oilChangeActivity.class);
                intent.putExtra("vehicleId", vehicleId);
                startActivity(intent);
            }
        });

        viewModel.openTyreRepair().observe(this, open -> {
            if (Boolean.TRUE.equals(open)) {
                Intent intent = new Intent(this, tyreRepairActivity.class);
                intent.putExtra("vehicleId", vehicleId);
                startActivity(intent);
            }
        });

        viewModel.openBrakes().observe(this, open -> {
            if (Boolean.TRUE.equals(open)) {
                Intent intent = new Intent(this, brakeSyetemRepairActivity.class);
                intent.putExtra("vehicleId", vehicleId);
                startActivity(intent);
            }
        });

        viewModel.openBattery().observe(this, open -> {
            if (Boolean.TRUE.equals(open)) {
                Intent intent = new Intent(this, batteryReplacementActivity.class);
                intent.putExtra("vehicleId", vehicleId);
                startActivity(intent);
            }
        });

        viewModel.openOverhaul().observe(this, open -> {
            if (Boolean.TRUE.equals(open)) {
                Intent intent = new Intent(this, engineOverhaulActivity.class);
                intent.putExtra("vehicleId", vehicleId);
                startActivity(intent);
            }
        });

        viewModel.openTowing().observe(this, open -> {
            if (Boolean.TRUE.equals(open)) {
                Intent intent = new Intent(this, towDispatchActivity.class);
                intent.putExtra("vehicleId", vehicleId);
                startActivity(intent);
            }
        });

        // Errors
        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg == null) return;

            if (msg.startsWith("LIMIT_REACHED_")) {
                int count = Integer.parseInt(msg.split("_")[2]);
                MyUtils.requestLimitReachMessage(count, this, ServiceListActivity.class);
            } else if (msg.equals("ERROR_LIMIT")) {
                MyUtils.toastMakeErrorRequestLimit(this);
            } else {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}