package com.example.ridecare.activities.service;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ridecare.R;
import com.example.ridecare.models.engineOverhaulRequest;
import com.example.ridecare.models.towingRequest;
import com.example.ridecare.utils.MyUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class towDispatchActivity extends AppCompatActivity {
    String serviceType = "Tow Request";
    String mechanicID = null;

    String status = "Service Requested";
    // ── Progress segments ──────────────────────────────────────────
    private View seg1, seg2, seg3, seg4;

    // ── Section 1 – Location ──────────────────────────────────────
    private TextInputEditText currentLocation;
    private ChipGroup cgRoadType;
    private MaterialButton btnSafeYes, btnSafeNo;
    private String safeSelection = "";
    private String isSteeringLocked = "";
    private String canRoll = "";
    private String isVehicleDamaged ;
    private String areWheelsIntact;



    // ── Section 2 – Vehicle Info ──────────────────────────────────

    private String driveTrain, vehicleCondition;
    private ChipGroup cgDriveTrain, cgVehicleCondition;

    private MaterialButton btnSteeringLocked, btnSteeringFree;


    private MaterialButton btnRollsYes, btnRollsNo;

    // ── Section 3 – Condition ─────────────────────────────────────
    private ChipGroup cgIncident, cgIsStuck;
    private MaterialButton btnDamagedYes, btnDamagedNo;
    private MaterialButton btnWheelsYes, btnWheelsNo;
    private TextInputEditText etConditionNotes;

    // ── Section 4 – Payment ───────────────────────────────────────
    private ChipGroup cgPaymentType, cgReceipt;
    private MaterialButton btnInsuranceYes, btnInsuranceNo;
    private String hasInsurance;
    private TextInputLayout layoutInsuranceProvider;
    private TextInputEditText etInsuranceProvider;

    // Database
    FirebaseFirestore db;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tow_dispatch);


        bindViews();
        setupTogglePairs();
        setupProgressTracking();
        submitRequest();
    }

    // ─────────────────────────────────────────────────────────────
    //  Bind views
    // ─────────────────────────────────────────────────────────────
    private void bindViews() {
        seg1 = findViewById(R.id.progressSeg1);
        seg2 = findViewById(R.id.progressSeg2);
        seg3 = findViewById(R.id.progressSeg3);
        seg4 = findViewById(R.id.progressSeg4);

        currentLocation = findViewById(R.id.etLocation);
        cgRoadType      = findViewById(R.id.cgPosition);
        btnSafeYes      = findViewById(R.id.btnSafeYes);
        btnSafeNo       = findViewById(R.id.btnSafeNo);

        cgDriveTrain       = findViewById(R.id.cgDriveType);
        cgVehicleCondition = findViewById(R.id.cgMods);
        btnSteeringLocked  = findViewById(R.id.btnSteeringLocked);
        btnSteeringFree    = findViewById(R.id.btnSteeringFree);
        btnRollsYes        = findViewById(R.id.btnRollsYes);
        btnRollsNo         = findViewById(R.id.btnRollsNo);

        cgIncident       = findViewById(R.id.cgIncident);
        btnDamagedYes    = findViewById(R.id.btnDamagedYes);
        btnDamagedNo     = findViewById(R.id.btnDamagedNo);
        btnWheelsYes     = findViewById(R.id.btnWheelsYes);
        btnWheelsNo      = findViewById(R.id.btnWheelsNo);
        cgIsStuck        = findViewById(R.id.cgStuck);
        etConditionNotes = findViewById(R.id.etConditionNotes);

        cgPaymentType           = findViewById(R.id.cgPayment);
        btnInsuranceYes         = findViewById(R.id.btnInsuranceYes);
        btnInsuranceNo          = findViewById(R.id.btnInsuranceNo);
        layoutInsuranceProvider = findViewById(R.id.layoutInsuranceProvider);
        etInsuranceProvider     = findViewById(R.id.etInsuranceProvider);
        cgReceipt               = findViewById(R.id.cgReceipt);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    // ─────────────────────────────────────────────────────────────
    //  Yes / No toggle pairs
    // ─────────────────────────────────────────────────────────────
    private void setupTogglePairs() {

        //Yes or No toggle pairs

       //Is the area safe
        MyUtils.setupYesNo(this, btnSafeYes,       btnSafeNo,       R.color.accent_green, R.color.accent_red, this::updateProgress);
        btnSafeYes.setOnClickListener(v -> safeSelection = "Yes");
        btnSafeNo.setOnClickListener(v -> safeSelection = "No");

        //Steering Locked
        MyUtils.setupYesNo(this, btnSteeringLocked, btnSteeringFree, R.color.accent_green, R.color.accent_red, this::updateProgress);
        btnSteeringLocked.setOnClickListener(v -> isSteeringLocked = "Yes");
        btnSteeringFree.setOnClickListener(v -> isSteeringLocked = "No");

       //Does the vehicle roll
        MyUtils.setupYesNo(this, btnRollsYes,       btnRollsNo,      R.color.accent_green, R.color.accent_red, this::updateProgress);
        btnRollsYes.setOnClickListener(v -> canRoll = "Yes");
        btnRollsNo.setOnClickListener(v -> canRoll = "No");

        // Is the vehicle damaged
        MyUtils.setupYesNo(this, btnDamagedYes,     btnDamagedNo,    R.color.accent_green, R.color.accent_red, this::updateProgress);
        btnDamagedYes.setOnClickListener(v -> isVehicleDamaged = "Yes");
        btnDamagedNo.setOnClickListener(v -> isVehicleDamaged = "No");
       //Wheels Intact
        MyUtils.setupYesNo(this, btnWheelsYes,      btnWheelsNo,     R.color.accent_green, R.color.accent_red, this::updateProgress);
        btnWheelsYes.setOnClickListener(v -> areWheelsIntact = "Yes");
        btnWheelsNo.setOnClickListener(v -> areWheelsIntact =  "No");

        // Insurance
        MyUtils.setupYesNo(this, btnInsuranceYes,     btnInsuranceNo,     R.color.accent_green, R.color.accent_red, this::updateProgress);
        btnInsuranceYes.setOnClickListener(v -> hasInsurance = "Yes");
        btnInsuranceNo.setOnClickListener(v -> hasInsurance =  "No");
    }
    // ─────────────────────────────────────────────────────────────
    //  Insurance reveal — colour toggle + show/hide field
    // ─────────────────────────────────────────────────────────────

    // ─────────────────────────────────────────────────────────────
    //  Progress tracking
    // ─────────────────────────────────────────────────────────────
    private void setupProgressTracking() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { updateProgress(); }
        };

        currentLocation.addTextChangedListener(watcher);


        ChipGroup.OnCheckedStateChangeListener chipListener = (group, checkedIds) -> updateProgress();
        List<ChipGroup> chipGroups = Arrays.asList(
                cgRoadType, cgDriveTrain, cgVehicleCondition,
                cgIncident, cgIsStuck, cgPaymentType, cgReceipt
        );
        for (ChipGroup cg : chipGroups) {
            cg.setOnCheckedStateChangeListener(chipListener);
        }
    }
    private void updateProgress() {
        int amber = ContextCompat.getColor(this, R.color.accent_amber);
        int green = ContextCompat.getColor(this, R.color.accent_green);
        int idle  = ContextCompat.getColor(this, R.color.bg_border);

        boolean s1 = (currentLocation.getText() != null && currentLocation.getText().toString().trim().length() > 0)
                || cgRoadType.getCheckedChipId() != View.NO_ID;
        boolean s2 = cgDriveTrain.getCheckedChipId() != View.NO_ID;
        boolean s3 = cgIncident.getCheckedChipId() != View.NO_ID;

        seg1.setBackgroundColor(s1 ? green : amber);
        seg2.setBackgroundColor(s2 ? green : (s1 ? amber : idle));
        seg3.setBackgroundColor(s3 ? green : (s2 ? amber : idle));
    }

    // ─────────────────────────────────────────────────────────────
    //  Submit & Validation
    // ─────────────────────────────────────────────────────────────
    private void submitRequest() {
        MaterialButton btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {

            // ─────────────────────────────────────────────────────────────
            // Validate first before doing anything
            // ─────────────────────────────────────────────────────────────
            if (!validate()) return;

            // ─────────────────────────────────────────────────────────────
            //  Tow Conditions / Vehicle Location
            // ─────────────────────────────────────────────────────────────
            String locationText = currentLocation.getText() != null
                    ? currentLocation.getText().toString() : "";

            String selectedRoadType = MyUtils.getSelectedChipText(this, R.id.cgPosition);
            if (selectedRoadType == null) return;

            String dayPeriod;
            Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

            if (hourOfDay >= 6 && hourOfDay < 12) {
                dayPeriod = "Morning";
            } else if (hourOfDay >= 12 && hourOfDay < 17) {
                dayPeriod = "Afternoon";
            } else if (hourOfDay >= 17 && hourOfDay < 21) {
                dayPeriod = "Evening";
            } else {
                dayPeriod = "Night";
            }

            // ✅ Use safeSelection directly — no empty string middleman
            String isVehicleAccessibleOutput = MyUtils.mapYesNoSelection("", safeSelection);
            String conditionTow = MyUtils.towCondition(selectedRoadType, isVehicleAccessibleOutput, dayPeriod);
            Log.d("Output Test", "Towing: " + conditionTow);

            // ─────────────────────────────────────────────────────────────
            //  Tow Truck Type / Vehicle Information
            // ─────────────────────────────────────────────────────────────
            String driveTrain = MyUtils.getSelectedChipText(this, R.id.cgDriveType);
            if (driveTrain == null) return;

            String modifications = MyUtils.getSelectedChipText(this, R.id.cgMods);
            if (modifications == null) return;

            // ✅ Pass the mapped output variables, not the empty strings
            String steeringLockedOutput  = MyUtils.mapYesNoSelection("", isSteeringLocked);
            String vehicleRollingOutput  = MyUtils.mapYesNoSelection("", canRoll);

            String recommTowTruckType = MyUtils.towTruckType(driveTrain, modifications, steeringLockedOutput, vehicleRollingOutput);
            Log.d("Output Test", "Tow Truck: " + recommTowTruckType);

            // ─────────────────────────────────────────────────────────────
            // Tow Vehicle Condition
            // ─────────────────────────────────────────────────────────────
            String incident = MyUtils.getSelectedChipText(this, R.id.cgIncident);
            if (incident == null) return;

            // ✅ Pass mapped outputs
            String isDamagedOutput    = MyUtils.mapYesNoSelection("", isVehicleDamaged  != null ? isVehicleDamaged  : "");
            String wheelsIntactOutput = MyUtils.mapYesNoSelection("", areWheelsIntact   != null ? areWheelsIntact   : "");

            String recommendedVehicleCondition = MyUtils.vehicleCondition(incident, isDamagedOutput, wheelsIntactOutput);

            // ─────────────────────────────────────────────────────────────
            // Payment Method
            // ─────────────────────────────────────────────────────────────
            String paymentMethod = MyUtils.getSelectedChipText(this, R.id.cgPayment);
            if (paymentMethod == null) return;

            String receiptMethod = MyUtils.getSelectedChipText(this, R.id.cgReceipt);
            if (receiptMethod == null) return;

            // ✅ Pass mapped output
            String insuranceOutput = MyUtils.mapYesNoSelection("", hasInsurance != null ? hasInsurance : "");
            String payment = MyUtils.paymentMethod(paymentMethod, insuranceOutput, receiptMethod);
            Log.d("Testing Output", payment);

            // ─────────────────────────────────────────────────────────────
            // Database Entry
            // ─────────────────────────────────────────────────────────────
            String uid = MyUtils.UidCheck(this, auth);
            if (uid == null) return;

            String vehicleId = MyUtils.vehicleIdCheck(this);
            if (vehicleId == null) return;

            DocumentReference docRef = db.collection("request_service_vehicle").document();

            db.collection("users").document(uid).get().addOnSuccessListener(userDoc -> {

                if (!MyUtils.requireDocument(userDoc, this, "User not found")) return;

                db.collection("vehicles").document(vehicleId).get().addOnSuccessListener(vehicleDoc -> {

                    if (!MyUtils.requireDocument(vehicleDoc, this, "Vehicle")) return;

                    towingRequest request = new towingRequest();

                    // Base fields
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

                    // Location
                    request.setRoadType(selectedRoadType);
                    request.setIsVehicleAccessible(isVehicleAccessibleOutput);
                    request.setLocation(locationText);

                    // Vehicle Condition
                    request.setIncident(incident);
                    request.setIsDamaged(isDamagedOutput);
                    request.setWheelsIntact(wheelsIntactOutput);

                    // Vehicle Info
                    request.setDriveTrain(driveTrain);
                    request.setCondition(modifications);
                    request.setIsSteeringLocked(steeringLockedOutput);
                    request.setCanRoll(vehicleRollingOutput);

                    // Payment
                    request.setPaymentType(paymentMethod);
                    request.setHasAssistance(insuranceOutput);
                    request.setNeedsInvoice(receiptMethod);

                    // Recommendations
                    request.setTowTruck(recommTowTruckType);
                    request.setVehicleCondition(recommendedVehicleCondition);
                    request.setPaymentMethod(payment);
                    request.setTowCondition(conditionTow);

                    // ✅ Save first, then show success dialog inside the callback
                    MyUtils.saveAndClose(this, docRef, request, this);
                    showSuccessDialog();
                });
            });
        });
    }

    private boolean validate() {
        boolean[] ok = {true};

        validateField(currentLocation, findViewById(R.id.layoutLocation), ok);

        if (!ok[0]) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please fill in the required fields",
                    Snackbar.LENGTH_SHORT
            ).show();
        }
        return ok[0];
    }
    private void validateField(TextInputEditText et, TextInputLayout layout, boolean[] ok) {
        if (et.getText() == null || et.getText().toString().trim().isEmpty()) {
            layout.setError("Required");
            ok[0] = false;
        } else {
            layout.setError(null);
        }
    }
    // ─────────────────────────────────────────────────────────────
    //  Success dialog
    // ─────────────────────────────────────────────────────────────
    private void showSuccessDialog() {
        int jobNum = 1000 + new Random().nextInt(9000);
        String jobRef = "TOW-" + jobNum;

        Dialog dialog = new Dialog(this, R.style.Theme_RideCare);
        dialog.setContentView(R.layout.dialog_success);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        }
        dialog.setCancelable(false);

        TextView tvJobId = dialog.findViewById(R.id.tvJobId);
        tvJobId.setText("JOB #" + jobRef);

        MaterialButton btnNewRequest = dialog.findViewById(R.id.btnNewRequest);
        btnNewRequest.setOnClickListener(v -> {
            dialog.dismiss();
            resetForm();
        });

        dialog.show();
    }
    // ─────────────────────────────────────────────────────────────
    //  Reset form
    // ─────────────────────────────────────────────────────────────
    private void resetForm() {
        List<TextInputEditText> textFields = Arrays.asList(
                currentLocation, etConditionNotes, etInsuranceProvider
        );
        for (TextInputEditText field : textFields) {
            if (field.getText() != null) field.getText().clear();
        }

        List<ChipGroup> chipGroups = Arrays.asList(
                cgRoadType, cgDriveTrain, cgVehicleCondition,
                cgIncident, cgIsStuck, cgPaymentType, cgReceipt
        );
        for (ChipGroup cg : chipGroups) {
            cg.clearCheck();
        }

        List<MaterialButton> toggleButtons = Arrays.asList(
                btnSafeYes, btnSafeNo, btnSteeringLocked, btnSteeringFree,
                btnRollsYes, btnRollsNo, btnDamagedYes, btnDamagedNo,
                btnWheelsYes, btnWheelsNo, btnInsuranceYes, btnInsuranceNo
        );
        for (MaterialButton btn : toggleButtons) {
            btn.setTextColor(ContextCompat.getColor(this, R.color.text_muted));
            btn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.bg_border));
            btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.bg_steel));
        }

        layoutInsuranceProvider.setVisibility(View.GONE);
        updateProgress();
    }
}
