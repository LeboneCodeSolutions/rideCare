package com.example.ridecare.activities.client;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ridecare.R;
import com.example.ridecare.models.Vehicle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddVehicleActivity extends AppCompatActivity {

    EditText etMake, etModel, etYear, etVIN, etReg;
    Button btnSave;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        etMake = findViewById(R.id.etMake);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etVIN = findViewById(R.id.etVIN);
        etReg = findViewById(R.id.etReg);
        btnSave = findViewById(R.id.btnSaveVehicle);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnSave.setOnClickListener(v -> saveVehicle());
    }

    private void saveVehicle() {

        // Check if the user is logged in or not
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = auth.getCurrentUser().getUid();

// Filled from the form
        String make = etMake.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String vin = etVIN.getText().toString().trim().toUpperCase();
        String reg = etReg.getText().toString().trim();

        if (TextUtils.isEmpty(make)) { etMake.setError("Required"); return; }
        if (TextUtils.isEmpty(model)) { etModel.setError("Required"); return; }
        if (TextUtils.isEmpty(yearStr)) { etYear.setError("Required"); return; }
        if (TextUtils.isEmpty(vin)) { etVIN.setError("Required"); return; }
        if (TextUtils.isEmpty(reg)) { etReg.setError("Required"); return; }

        int year;
        try { year = Integer.parseInt(yearStr); }
        catch (NumberFormatException e) { etYear.setError("Invalid year"); return; }

        String uEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;
        if (uEmail == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }


        Vehicle vehicle = new Vehicle(userId,  make, model, year, vin, reg);

        DocumentReference docRef = db.collection("vehicles").document();
        // setId is linked to the vehicleId
        vehicle.setVehicleId(docRef.getId());

        docRef.set(vehicle)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Vehicle Added", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
