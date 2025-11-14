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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InvoiceCreateActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    EditText etClientEmailAddress, etWorkDone1,etWorkDone2, etWorkDone3, etClientVehicleRegistrationNumber, etClientVehicleVinNumber, etCost, etPartsUsed;
    Button btnSubmitInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_create);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        etClientEmailAddress = findViewById(R.id.etClientEmailAddress);
        etClientVehicleRegistrationNumber = findViewById(R.id.etClientVehicleRegistrationNumber);
        etClientVehicleVinNumber = findViewById(R.id.etClientVehicleVinNumber);

        etWorkDone1 = findViewById(R.id.etWorkDone1);
        etWorkDone2 = findViewById(R.id.etWorkDone2);
        etWorkDone3 = findViewById(R.id.etWorkDone3);


        etPartsUsed = findViewById(R.id.etPartsUsed);
        etCost = findViewById(R.id.etCost);

        btnSubmitInvoice= findViewById(R.id.btnSubmitInvoice);

        btnSubmitInvoice.setOnClickListener(v -> saveInvoice());
    }

    private void saveInvoice() {
        String regExEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String regExNoSpecialCharacters = "^[A-Za-z0-9]+$";
        int vinNumberLength = 17;

        String clientEmail = etClientEmailAddress.getText().toString().trim();
        String registrationNumber = etClientVehicleRegistrationNumber.getText().toString().trim();
        String vinNumber = etClientVehicleVinNumber.getText().toString().trim();
        String workDone1 = etWorkDone1.getText().toString().trim();
        String workDone2 = etWorkDone2.getText().toString().trim();
        String workDone3 = etWorkDone3.getText().toString().trim();
        String partsUsed = etPartsUsed.getText().toString().trim();
        String costText = etCost.getText().toString().trim();

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

        // Check if client exists in Firestore
        db.collection("users")
                .whereEqualTo("email", clientEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Client exists, proceed to create invoice
                        createInvoice(clientEmail, registrationNumber, vinNumber,
                                workDone1, workDone2, workDone3, partsUsed, costText);
                    } else {
                        Toast.makeText(this, "Client email does not exist!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error checking client email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void createInvoice(String clientEmail, String regNumber, String vinNumber,
                               String work1, String work2, String work3, String parts, String costText) {

        // Get mechanic's email from FirebaseAuth
        String mechanicEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Map<String, Object> invoice = new HashMap<>();
        invoice.put("clientEmailAddress", clientEmail);
        invoice.put("vehicleRegNumber", regNumber);
        invoice.put("vehicleVinNumber", vinNumber);
        invoice.put("mechanicId", FirebaseUtils.getUid());
        invoice.put("mechanicEmail", mechanicEmail); // <-- store mechanic email
        invoice.put("workDone1", work1);
        invoice.put("workDone2", work2);
        invoice.put("workDone3", work3);
        invoice.put("partsUsed", parts);
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
