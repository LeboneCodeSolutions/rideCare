package com.example.ridecare.activities.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.gridlayout.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ridecare.R;
import com.example.ridecare.utils.MyUtils;
import com.example.ridecare.viewmodels.AddVehicleViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class AddVehicleActivity extends AppCompatActivity {

    // Vehicle Details
    TextInputEditText tfMake, tfYear, tfModel, tfVin, tfMileage, tfRegistration;

    // Specifications
    //Drive Type
    LinearLayout driveTypeGroup, btnManual, btnAutomatic;


    // Fuel Type
    LinearLayout fuelTypeGroup, btnPetrol, btnDiesel, btnElectric;

    // Body Type
    GridLayout bodyTypeGrid;
    LinearLayout btnSedan, btnSuv, btnHatchback, btnCoupe, btnBakkie, btnVan;

    // Service History
    LinearLayout serviceHistoryGroup, btnServiceNone, btnServicePartial, btnServiceFull;
    SwitchMaterial switchServiceBook;

    // Save Button
    MaterialButton btnSaveVehicle;

  // View Model Import
    AddVehicleViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        viewBinder(); // -> bind the views
        setupViewModel(); // -> send the values collected from the user to the processer == viewmodel
        setupListeners();
    }

    private final Map<String, EditText> fieldMap = new HashMap<>();
    private void viewBinder() {
        tfMake = findViewById(R.id.tfMake);
        tfYear = findViewById(R.id.tfYear);
        tfModel = findViewById(R.id.tfModel);
        tfVin = findViewById(R.id.tfVin);
        tfMileage = findViewById(R.id.tfMileage);
        tfRegistration = findViewById(R.id.tfRegistration);

        // Specifications
        //Drive Type
        driveTypeGroup = findViewById(R.id.driveTypeGroup);
        btnManual = findViewById(R.id.btnManual);
        btnAutomatic = findViewById(R.id.btnAutomatic);

        // Fuel Type
        fuelTypeGroup = findViewById(R.id.fuelTypeGroup);
        btnPetrol = findViewById(R.id.btnPetrol);
        btnDiesel = findViewById(R.id.btnDiesel);
        btnElectric = findViewById(R.id.btnElectric);

        // Body Type
        bodyTypeGrid = findViewById(R.id.bodyTypeGrid);
        btnSedan = findViewById(R.id.btnSedan);
        btnSuv = findViewById(R.id.btnSuv);
        btnHatchback = findViewById(R.id.btnHatchback);
        btnCoupe = findViewById(R.id.btnCoupe);
        btnBakkie = findViewById(R.id.btnBakkie);
        btnVan = findViewById(R.id.btnVan);

        // Service History
        serviceHistoryGroup = findViewById(R.id.serviceHistoryGroup);
        btnServiceNone = findViewById(R.id.btnServiceNone);
        btnServicePartial = findViewById(R.id.btnServicePartial);
        btnServiceFull = findViewById(R.id.btnServiceFull);


        switchServiceBook = findViewById(R.id.switchServiceBook);

        // Save Button
        btnSaveVehicle = findViewById(R.id.btnSaveVehicle);

        // Add New Values into map if i create a new field
        fieldMap.put("make",  tfMake);
        fieldMap.put("model", tfModel);
        fieldMap.put("mileage", tfMileage);
        fieldMap.put("year",  tfYear);
        fieldMap.put("vin",   tfVin);
        fieldMap.put("reg",   tfRegistration);

    }
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AddVehicleViewModel.class);

        viewModel.getSaveStatus().observe(this, status -> {
            if (status.equals("success")) {
                Toast.makeText(this, "Vehicle saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else if (status.startsWith("error:")) {
                handleError(status.replace("error:", ""));
            }
        });

// chnage to my utilss
        viewModel.getServiceHistoryType().observe(this, type ->{
            btnServiceNone.setBackgroundResource(R.drawable.bg_option_unselected);
            btnServicePartial.setBackgroundResource(R.drawable.bg_option_unselected);
            btnServiceFull.setBackgroundResource(R.drawable.bg_option_unselected);

            switch (type){
                case "None": btnServiceNone.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Partial": btnServicePartial.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Full": btnServiceFull.setBackgroundResource(R.drawable.bg_option_selected); break;
            }
        });
        viewModel.getBodyType().observe(this, type -> {
            // Reset all first
            btnSedan.setBackgroundResource(R.drawable.bg_option_unselected);
            btnSuv.setBackgroundResource(R.drawable.bg_option_unselected);
            btnHatchback.setBackgroundResource(R.drawable.bg_option_unselected);
            btnCoupe.setBackgroundResource(R.drawable.bg_option_unselected);
            btnBakkie.setBackgroundResource(R.drawable.bg_option_unselected);
            btnVan.setBackgroundResource(R.drawable.bg_option_unselected);

            // Highlight selected
            switch (type) {
                case "Sedan":    btnSedan.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Suv":      btnSuv.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Hatchback":btnHatchback.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Coupe":    btnCoupe.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Bakkie":   btnBakkie.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Van":      btnVan.setBackgroundResource(R.drawable.bg_option_selected); break;
            }
        });
        viewModel.getTransmissionType().observe(this,type->{
            btnManual.setBackgroundResource(R.drawable.bg_option_unselected);
            btnAutomatic.setBackgroundResource(R.drawable.bg_option_unselected);
            switch (type) {
                case "Manual":    btnManual.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Automatic":      btnAutomatic.setBackgroundResource(R.drawable.bg_option_selected); break;

            }
        });
        viewModel.getFuelType().observe(this, type ->{
            btnPetrol.setBackgroundResource(R.drawable.bg_option_unselected);
            btnDiesel.setBackgroundResource(R.drawable.bg_option_unselected);
            btnElectric.setBackgroundResource(R.drawable.bg_option_unselected);
            switch (type) {
                case "Petrol":    btnPetrol.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Diesel":  btnDiesel.setBackgroundResource(R.drawable.bg_option_selected); break;
                case "Electric":  btnElectric.setBackgroundResource(R.drawable.bg_option_selected); break;


            }
        });
        viewModel.getIsServiceBookPresent().observe(this, type ->{
            switchServiceBook.setChecked(type);
        });
    }
    private void setupListeners() {
        // Drive Type
        btnManual.setOnClickListener(v -> viewModel.setTransmissionType("Manual"));
        btnAutomatic.setOnClickListener(v -> viewModel.setTransmissionType("Automatic"));

        // Fuel Type
        btnPetrol.setOnClickListener(v -> viewModel.setFuelType("Petrol"));
        btnDiesel.setOnClickListener(v -> viewModel.setFuelType("Diesel"));
        btnElectric.setOnClickListener(v -> viewModel.setFuelType("Electric"));

        // Body Type
        btnSedan.setOnClickListener(v -> viewModel.setBodyType("Sedan"));
        btnSuv.setOnClickListener(v -> viewModel.setBodyType("Suv"));
        btnHatchback.setOnClickListener(v -> viewModel.setBodyType("Hatchback"));
        btnCoupe.setOnClickListener(v -> viewModel.setBodyType("Coupe"));
        btnBakkie.setOnClickListener(v -> viewModel.setBodyType("Bakkie"));
        btnVan.setOnClickListener(v -> viewModel.setBodyType("Van"));

        // Service History
        btnServiceNone.setOnClickListener(v -> viewModel.setServiceHistoryType("None"));
        btnServicePartial.setOnClickListener(v -> viewModel.setServiceHistoryType("Partial"));
        btnServiceFull.setOnClickListener(v -> viewModel.setServiceHistoryType("Full"));

        // Service Book Switch
        switchServiceBook.setOnCheckedChangeListener((buttonView, isChecked) ->
           viewModel.setIsServiceBookPresent(isChecked));

        btnSaveVehicle.setOnClickListener(v -> {
            // Activity just collects and passes - no logic
            String make    = MyUtils.newStr(tfMake);
            String model   = MyUtils.newStr(tfModel);
            String yearStr = MyUtils.newStr(tfYear);
            String vin     = MyUtils.newStr(tfVin);
            String reg     = MyUtils.newStr(tfRegistration);
            int year       = MyUtils.intParser(yearStr, tfYear);

            String transmissionType = viewModel.getTransmissionType().getValue();
            String fuelType = viewModel.getFuelType().getValue();
            String bodyType = viewModel.getBodyType().getValue();
            String serviceHistory = viewModel.getServiceHistoryType().getValue();
            Boolean isServiceBookPresent = viewModel.getIsServiceBookPresent().getValue();

            viewModel.saveVehicle(make, model, year, vin, reg, transmissionType, fuelType, bodyType, serviceHistory, isServiceBookPresent);
        });
    }


    private void handleError(String field) {
        EditText formInputs = fieldMap.get(field);
        if (formInputs != null) {
            MyUtils.requireString(formInputs.getText().toString(), formInputs, "Required");
        } else {
            // Changed to myUtil Function Refer to MyUtils
            MyUtils.eDisplay(this, field);
        }
    }
}