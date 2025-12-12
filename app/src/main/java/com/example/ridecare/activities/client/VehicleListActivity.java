package com.example.ridecare.activities.client; // Use your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R; // Use your actual R file import
import com.example.ridecare.activities.common.ForgotPasswordActivity;
import com.example.ridecare.activities.common.LoginActivity;
import com.example.ridecare.adapters.VehicleAdapter;
import com.example.ridecare.models.Vehicle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VehicleListActivity extends AppCompatActivity {

    private static final String TAG = "VehicleListActivity";

    private RecyclerView rvVehicles;
    private Button btnAddVehicle;
    private VehicleAdapter adapter;
    private List<Vehicle> vehicleList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    private ListenerRegistration firestoreListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Find views from the layout
        rvVehicles = findViewById(R.id.rvVehicles);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);




        vehicleList = new ArrayList<>();
        adapter = new VehicleAdapter(vehicleList, this);


        rvVehicles.setLayoutManager(new LinearLayoutManager(this));


        rvVehicles.setAdapter(adapter);


        loadVehiclesRealtime();


        btnAddVehicle.setOnClickListener(v -> {
           startActivity(new Intent(VehicleListActivity.this, AddVehicleActivity.class)                                                                                                                                                                                          );
        });
    }




    private void loadVehiclesRealtime() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "No user is signed in.");

            return;
        }
        String uid = currentUser.getUid();

        // Query the 'vehicles' collection where 'userId' matches the current user's UID
        firestoreListener = db.collection("vehicles")
                .whereEqualTo("userId", uid)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    // Clear the old list to avoid duplicates
                    vehicleList.clear();

                    if (snapshots != null) {
                        // Loop through all the documents returned by the query
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Vehicle vehicle = doc.toObject(Vehicle.class);
                            vehicle.setVehicleId(doc.getId()); // Store the document ID
                            vehicleList.add(vehicle);
                        }
                    }

                    // 4. **FIX**: Notify the adapter that the data set has changed.
                    // This tells the RecyclerView to refresh and display the new data.
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Vehicle list updated. Total vehicles: " + vehicleList.size());
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove the listener to prevent memory leaks and unnecessary background data fetching
        if (firestoreListener != null) {
            firestoreListener.remove();
        }
    }
}