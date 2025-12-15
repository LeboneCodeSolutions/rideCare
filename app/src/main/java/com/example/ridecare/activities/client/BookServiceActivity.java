package com.example.ridecare.activities.client;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import com.example.ridecare.R;
import com.example.ridecare.models.ServiceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookServiceActivity extends AppCompatActivity {

    Spinner spServiceType;
    EditText etDescription;
    Button btnSubmit;

    FirebaseFirestore db;
    FirebaseAuth auth;

    String vehicleId;
    String status = "pending";

    // Mechanic assigned later when job is accepted
    String mechanicID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service);

        vehicleId = getIntent().getStringExtra("vehicleId");

        spServiceType = findViewById(R.id.spServiceType);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmitRequest);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(v -> submitRequest());
    }

    private void submitRequest() {

        String serviceType = spServiceType.getSelectedItem().toString();
        String desc = etDescription.getText().toString().trim();

        if (desc.isEmpty()) {
            etDescription.setError("Description required");
            return;
        }

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vehicleId == null || vehicleId.isEmpty()) {
            Toast.makeText(this, "Vehicle not selected", Toast.LENGTH_LONG).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();
        String clientEmail = auth.getCurrentUser().getEmail();

        // 🔹 Create Firestore document reference FIRST
        DocumentReference docRef =
                db.collection("service_requests").document();

        // 🔹 Fetch user profile
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(userDoc -> {

                    if (!userDoc.exists()) {
                        Toast.makeText(this, "User profile not found", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String fName = userDoc.getString("firstname");
                    String lName = userDoc.getString("lastname");

                    // 🔹 Fetch vehicle
                    db.collection("vehicles")
                            .document(vehicleId)
                            .get()
                            .addOnSuccessListener(vehicleDoc -> {

                                if (!vehicleDoc.exists()) {
                                    Toast.makeText(this, "Vehicle not found", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                String vehicleReg = vehicleDoc.getString("vehicleReg");
                                String vinNumber = vehicleDoc.getString("vinNumber");

                                // 🔹 Build ServiceRequest (MATCHES YOUR MODEL)
                                ServiceRequest request = new ServiceRequest(
                                        uid,
                                        mechanicID,
                                        vehicleId,
                                        serviceType,
                                        status,
                                        desc,
                                        fName,
                                        lName,
                                        clientEmail,
                                        vehicleReg,
                                        vinNumber
                                );

                                request.setServiceRequestId(docRef.getId());

                                // 🔹 WRITE TO FIRESTORE
                                docRef.set(request)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(
                                                    this,
                                                    "Service request submitted!",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(
                                                        this,
                                                        "Write failed: " + e.getMessage(),
                                                        Toast.LENGTH_LONG
                                                ).show()
                                        );

                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(
                                            this,
                                            "Vehicle fetch failed: " + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "User fetch failed: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
    }
}
