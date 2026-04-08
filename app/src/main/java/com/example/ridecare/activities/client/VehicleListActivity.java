package com.example.ridecare.activities.client; // Use your actual package name
import static com.example.ridecare.utils.MyUtils.checkVehicleRequestLimit;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R;
import com.example.ridecare.adapters.VehicleAdapter;
import com.example.ridecare.models.Vehicle;
import com.example.ridecare.utils.MyUtils;
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
    private VehicleAdapter adapter;
    private List<Vehicle> vehicleList;
    private ImageView ivAddVehicle, backArrow;
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
        ivAddVehicle = findViewById(R.id.ivAddVehicle);
        backArrow = findViewById(R.id.backArrow);
        vehicleList = new ArrayList<>();
        adapter = new VehicleAdapter(vehicleList, this, new VehicleAdapter.OnVehicleAction() {
            @Override
            public void onBook(Vehicle vehicle) {
                /* Logic checks to see if the vehicle has more than x amount of requests to prevent from the system being flooded with request by one vehicle --
                check myUtils checkVehicleRequestLimit for more information about this
                */
                    String vehicleIdVar = vehicle.getVehicleId();
                    checkVehicleRequestLimit(vehicleIdVar, new MyUtils.OnLimitCheckListener(){
                    @Override
                    public void onCanProceed(int currentCount) {
                        Intent intent = new Intent(VehicleListActivity.this, BookServiceActivity.class);
                        intent.putExtra("vehicleId", vehicle.getVehicleId());
                        intent.putExtra("vehicleMake", vehicle.getMake());
                        intent.putExtra("vehicleModel", vehicle.getModel());

                        startActivity(intent);
                    }

                    @Override
                    public void onLimitReached(int currentCount) {
                        /* Designed to output a message should request exceed the limit
                        - refer to utils/MyUtils/function
                        - requestLimitReachMessage to get a better understanding of the function
                        */
                        MyUtils.requestLimitReachMessage(currentCount,VehicleListActivity.this, ServiceListActivity.class );
                    }


                        /*
                            - refer to utils/MyUtils/function
                            -toastMakeErrorRequestLimit to get a better understanding of the function
                            */
                    @Override
                    public void onError(Exception e) {
                       MyUtils.toastMakeErrorRequestLimit(VehicleListActivity.this);
                    }
                });

            }

            // Developer Note: Create a file which allows user to edit vehicle information
            @Override
            public void onEdit(Vehicle vehicle) {
                Intent intent = new Intent(VehicleListActivity.this, EditVehicleActivity.class);
                startActivity(intent);
            }

        });

        // Recycle view which displays the vehicles
        rvVehicles.setLayoutManager(new LinearLayoutManager(this));
        rvVehicles.setAdapter(adapter);
        loadVehiclesRealtime();


        backArrow.setOnClickListener(v ->{
            startActivity(new Intent(VehicleListActivity.this, DashboardActivity.class));
        });
        ivAddVehicle.setOnClickListener(v -> {
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