package com.example.ridecare.activities.client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ridecare.R;
import com.example.ridecare.adapters.ServiceRequestAdapter;
import com.example.ridecare.models.ServiceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ServiceListActivity extends AppCompatActivity {

    private static final String TAG = "ServiceListActivity";

    private String serviceRequestId;
    private RecyclerView rvServices;
    private ImageButton btnDelete;
    private ServiceRequestAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ListenerRegistration serviceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        // Initialize views
        rvServices = findViewById(R.id.rvServices);
        btnDelete  = findViewById(R.id.btnDelete);

        rvServices.setLayoutManager(new LinearLayoutManager(this));

        db   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Adapter with click callback — sets serviceRequestId when a row is tapped
        adapter = new ServiceRequestAdapter(new ArrayList<>(), serviceRequest -> {
            serviceRequestId = serviceRequest.getServiceRequestId();
            Toast.makeText(this, "Selected: " + serviceRequestId, Toast.LENGTH_SHORT).show();
        });

        rvServices.setAdapter(adapter);

        loadServices();
        setupDeleteButton();
    }

    // ─── Button ────────────────────────────────────────────────────────────────

    private void setupDeleteButton() {
        btnDelete.setOnClickListener(v -> {

            // Guard: make sure a request has been selected first
            if (serviceRequestId == null || serviceRequestId.isEmpty()) {
                Toast.makeText(this, "Please select a service request to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Delete Service Request")
                    .setMessage("Are you sure you want to delete this service request? This cannot be undone.")
                    .setPositiveButton("Delete", (dialog, which) -> deleteServiceRequest())
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // ─── Delete ────────────────────────────────────────────────────────────────

    private void deleteServiceRequest() {
        db.collection("request_service_vehicle")
                .whereEqualTo("serviceRequestId", serviceRequestId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Service request deleted", Toast.LENGTH_SHORT).show();
                                    serviceRequestId = null; // Reset selection after delete
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Service request not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error finding request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "deleteServiceRequest: ", e);
                });
    }

    // ─── Load ──────────────────────────────────────────────────────────────────

    private void loadServices() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please log in to view service requests", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = currentUser.getUid();

        serviceListener = db.collection("request_service_vehicle")
                .whereEqualTo("userId", uid)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value == null) return;

                    ArrayList<ServiceRequest> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        ServiceRequest request = doc.toObject(ServiceRequest.class);
                        list.add(request);
                    }

                    adapter.setItems(list);
                });
    }

    // ─── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceListener != null) {
            serviceListener.remove();
        }
    }
}