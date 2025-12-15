package com.example.ridecare.activities.mechanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ridecare.R;
import com.example.ridecare.adapters.MechanicJobAdapter;
import com.example.ridecare.models.ServiceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MechanicJobsActivity extends AppCompatActivity {

    RecyclerView rvJobs;
    ArrayList<ServiceRequest> jobsList = new ArrayList<>();
    MechanicJobAdapter adapter;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_jobs);

        rvJobs = findViewById(R.id.rvMechanicJobs);
        rvJobs.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        adapter = new MechanicJobAdapter(jobsList);
        rvJobs.setAdapter(adapter);

        loadJobs();
    }

    private void loadJobs() {

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String mechanicId = auth.getCurrentUser().getUid();

        db.collection("service_requests")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    jobsList.clear();

                    for (QueryDocumentSnapshot doc : value) {

                        ServiceRequest s = doc.toObject(ServiceRequest.class);

                        // 🔹 VERY IMPORTANT: set document ID
                        s.setServiceRequestId(doc.getId());

                        // 🔹 Show:
                        // 1. All pending jobs
                        // 2. In-progress jobs assigned to this mechanic
                        if ("pending".equals(s.getStatus()) ||
                                ("in-progress".equals(s.getStatus())
                                        && mechanicId.equals(s.getMechanicId()))) {

                            jobsList.add(s);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}
