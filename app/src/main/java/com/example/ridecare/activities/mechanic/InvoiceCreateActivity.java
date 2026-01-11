package com.example.ridecare.activities.mechanic;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ridecare.R;
import com.example.ridecare.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class InvoiceCreateActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    EditText etInvoiceClientEmail, etVehicleReg, etVehicleVIN, etVehicleOdometer, etDescriptionJob,etPartsUsed,etCostEstimate ;
    Button submitInvoiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        etInvoiceClientEmail = findViewById(R.id.etInvoiceClientEmail);
        etVehicleReg= findViewById(R.id.etVehicleReg);
        etVehicleVIN = findViewById(R.id.etVehicleVIN);
        etVehicleOdometer = findViewById(R.id.etVehicleOdometer);
        etDescriptionJob = findViewById(R.id.etDescriptionJob);
        etPartsUsed = findViewById(R.id.etPartsUsed);
        etCostEstimate = findViewById(R.id.etCostEstimate);

        submitInvoiceBtn= findViewById(R.id.submitInvoiceBtn);

        submitInvoiceBtn.setOnClickListener(v -> {
            saveInvoice();
            startActivity(new Intent(this, MechanicDashboardActivity.class));
        });
    }

    private void saveInvoice() {
        String regExEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String regExNoSpecialCharacters = "^[A-Za-z0-9]+$";
        int vinNumberLength = 17;

        String clientEmail = etInvoiceClientEmail.getText().toString().trim();
        String registrationNumber = etVehicleReg.getText().toString().trim();
        String vinNumber = etVehicleVIN.getText().toString().trim();
        String OdometerReading = etVehicleOdometer.getText().toString().trim();
        String JobDescription = etDescriptionJob.getText().toString().trim();
        String partsUsed = etPartsUsed.getText().toString().trim();
        String costText = etCostEstimate.getText().toString().trim();

        // Basic field validation
        if (clientEmail.isEmpty() || registrationNumber.isEmpty() || vinNumber.isEmpty() ||
                partsUsed.isEmpty() || costText.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!clientEmail.matches(regExEmail)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate VIN
        if (!vinNumber.matches(regExNoSpecialCharacters) || vinNumber.length() != vinNumberLength) {
            Toast.makeText(this, "VIN must be 17 characters long with no special characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if client email exsist in the firestore for service request
        // Check if client exists in Firestore
        db.collection("users")
                .whereEqualTo("email", clientEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Client exists, proceed to create invoice
                        createInvoice(
                                clientEmail,
                                registrationNumber,
                                vinNumber,
                                OdometerReading,
                                JobDescription,
                                partsUsed,
                                costText
                                );
                    } else {
                        Toast.makeText(this, "Client email does not exist!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error checking client email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void createInvoice(String clientEmail,
                               String registrationNumber,
                               String vinNumber,
                               String OdometerReading,
                               String partsUsed,
                               String JobDescription,
                               String costText) {

        // Get mechanic's email from FirebaseAuth
        String mechanicEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Map<String, Object> invoice = new HashMap<>();
        invoice.put("clientEmailAddress", clientEmail);
        invoice.put("vehicleRegNumber", registrationNumber);
        invoice.put("vehicleVinNumber", vinNumber);
        invoice.put("mechanicId", FirebaseUtils.getUid());
        invoice.put("mechanicEmail", mechanicEmail); // <-- store mechanic email
        invoice.put("OdometerReading", OdometerReading);
        invoice.put("JobDescription", JobDescription);
        invoice.put("partsUsed", partsUsed);
        invoice.put("totalCost", costText);
        invoice.put("timestamp", System.currentTimeMillis());
        invoice.put("paid", false);

        db.collection("invoices")
                .add(invoice)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Invoice Successfully Created", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error creating invoice: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

}
