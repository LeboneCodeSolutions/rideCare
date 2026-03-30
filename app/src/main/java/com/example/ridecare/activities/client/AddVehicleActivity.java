package com.example.ridecare.activities.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ridecare.R;
import com.example.ridecare.utils.MyUtils;
import com.example.ridecare.viewmodels.AddVehicleViewModel;

import java.util.HashMap;
import java.util.Map;

public class AddVehicleActivity extends AppCompatActivity {

    // only UI variables here
    EditText etMake, etModel, etYear, etVIN, etReg;
    Button btnSave;
    AddVehicleViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        bindViews(); // -> bind the views
        setupViewModel(); // -> send the values collected from the user to the processer == viewmodel
        setupListeners();
    }

    private final Map<String, EditText> fieldMap = new HashMap<>();
    private void bindViews() {
        etMake  = findViewById(R.id.etMake);
        etModel = findViewById(R.id.etModel);
        etYear  = findViewById(R.id.etYear);
        etVIN   = findViewById(R.id.etVIN);
        etReg   = findViewById(R.id.etReg);
        btnSave = findViewById(R.id.btnSaveVehicle);

        // Add New Values into map if i create a new field
        fieldMap.put("make",  etMake);
        fieldMap.put("model", etModel);
        fieldMap.put("year",  etYear);
        fieldMap.put("vin",   etVIN);
        fieldMap.put("reg",   etReg);
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
    }
    private void setupListeners() {
        btnSave.setOnClickListener(v -> {
            // Activity just collects and passes - no logic
            String make    = MyUtils.newStr(etMake);
            String model   = MyUtils.newStr(etModel);
            String yearStr = MyUtils.newStr(etYear);
            String vin     = MyUtils.newStr(etVIN);
            String reg     = MyUtils.newStr(etReg);
            int year       = MyUtils.intParser(yearStr, etYear);

            viewModel.saveVehicle(make, model, year, vin, reg);
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