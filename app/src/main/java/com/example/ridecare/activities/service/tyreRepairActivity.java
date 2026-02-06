package com.example.ridecare.activities.service;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ridecare.R;
import com.example.ridecare.models.TyreRepairRequest;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * TyreRepairActivity
 *
 * Handles tyre repair service request submission.
 * Collects user input, validates form fields, retrieves user/vehicle data,
 * and stores the request in Firestore using the TyreRepairRequest model.
 */
public class tyreRepairActivity extends AppCompatActivity {

    // Static request metadata
    String serviceType = "Tyre Repair";
    String status = "Service Requested";
    String mechanicID = null;

    // Runtime values
    String vehicleId, tyreSize;

    // Form inputs
    EditText tyreWidth, tyreHeight, tyreDiametre, etTyreQty, etPrevPurchDate;
    AutoCompleteTextView ddTyreQuality, ddTyreBrand;

    // UI container used as submit button
    ConstraintLayout clSubmitTyreRepair;

    // Firebase instances
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_service_tyre_repair);

        // Bind tyre size input fields
        tyreWidth = findViewById(R.id.tyreWidth);
        tyreHeight = findViewById(R.id.tyreHeight);
        tyreDiametre = findViewById(R.id.tyreDiametre);
        etTyreQty = findViewById(R.id.etTyreQty);

        // Previous purchase date field with reusable date picker helper
        etPrevPurchDate = findViewById(R.id.etPrevPurchDate);
        etPrevPurchDate.setOnClickListener(v ->
                MyUtils.openDatePicker(this, etPrevPurchDate)
        );

        // Tyre quality dropdown setup
        ddTyreQuality = findViewById(R.id.ddTyreQuality);
        String[] tyreQuality = {
                "Brand New",
                "Second Hand"
        };
        MyUtils.setDropdown(this, ddTyreQuality, tyreQuality);

        // Tyre brand dropdown setup
        ddTyreBrand = findViewById(R.id.ddTyreBrand);
        String[] tyreBrands = {
                "Michelin",
                "Bridgestone",
                "Goodyear",
                "Pirelli",
                "Continental",
                "Dunlop",
                "Yokohama",
                "Hankook",
                "BFGoodrich",
                "Firestone"
        };
        MyUtils.setDropdown(this, ddTyreBrand, tyreBrands);

        // Submit container acts as button
        clSubmitTyreRepair = findViewById(R.id.clSubmitTyreRepair);
        clSubmitTyreRepair.setOnClickListener(v -> submitRequest());
    }

    /**
     * Handles form submission:
     * - Extracts and validates all user inputs
     * - Builds tyre size string
     * - Retrieves authenticated user + vehicle data
     * - Creates and saves TyreRepairRequest to Firestore
     */
    private void submitRequest() {

        // Extract and sanitize form input values using reusable helper
        String tyreWidthString = MyUtils.newStr(tyreWidth);
        String tyreHeightString = MyUtils.newStr(tyreHeight);
        String tyreDiametreString = MyUtils.newStr(tyreDiametre);
        String TyreQtyString = MyUtils.newStr(etTyreQty);
        String prevPurchaseDateString = MyUtils.newStr(etPrevPurchDate);
        String tyreBrand = MyUtils.newStr(ddTyreBrand);
        String tyreQuality = MyUtils.newStr(ddTyreQuality);

        // Validate all required inputs before continuing
        if (!MyUtils.requireString(tyreWidthString, tyreWidth, "Tyre width required")) return;
        if (!MyUtils.requireString(tyreHeightString, tyreHeight, "Tyre height required")) return;
        if (!MyUtils.requireString(tyreDiametreString, tyreDiametre, "Tyre diameter required")) return;
        if (!MyUtils.requireString(TyreQtyString, etTyreQty, "Tyre Quantity required")) return;
        if (!MyUtils.requireString(prevPurchaseDateString, etPrevPurchDate, "Date Required")) return;
        if (!MyUtils.requireString(tyreBrand, ddTyreBrand, "Please make selection")) return;
        if (!MyUtils.requireString(tyreQuality, ddTyreQuality, "Please make selection")) return;

        // Build standard tyre size format: e.g. 205 55 R16
        tyreSize = tyreWidthString + " " + tyreHeightString + " " + "R" + tyreDiametreString;

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

                // Create new tyre repair request model
                TyreRepairRequest request = new TyreRepairRequest();

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

                // Tyre-specific fields
                request.setTyreSize(tyreSize);
                request.setTyreQty(TyreQtyString);
                request.setPrevPurchaseDate(prevPurchaseDateString);
                request.setTyreBrand(tyreBrand);
                request.setTyreQuality(tyreQuality);

                // Save request to Firestore using reusable helper
                MyUtils.saveAndClose(this, docRef, request, this);
            });
        });
    }
}
