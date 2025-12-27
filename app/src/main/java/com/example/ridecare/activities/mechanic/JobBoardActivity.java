package com.example.ridecare.activities.mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class JobBoardActivity extends AppCompatActivity {

    private RecyclerView rvJobList;
    private JobAdapter adapter;
    private List<CarJob> jobs;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_board);

        rvJobList = findViewById(R.id.rvJobList);
        rvJobList.setLayoutManager(new LinearLayoutManager(this));

        jobs = new ArrayList<>();

        adapter = new JobAdapter(jobs, job -> {
            int position = jobs.indexOf(job);
            if (position != -1) {

                // Move to active jobs
                jobs.remove(position);
                adapter.notifyItemRemoved(position);
                ActiveJobsManager.getInstance().addJob(job);

                // Update Firestore status
                db.collection("request_service_vehicle")
                        .document(job.getServiceRequestId())
                        .update(
                                "status", "accepted",
                                "mechanicId", "MECHANIC_UID_HERE"
                        );

                Toast.makeText(
                        JobBoardActivity.this,
                        "Job accepted",
                        Toast.LENGTH_SHORT
                ).show();

                startActivity(
                        new Intent(
                                JobBoardActivity.this,
                                MechanicDashboardActivity.class
                        )
                );
            }
        });

        rvJobList.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchJobsFromFirestore();
    }

    private void fetchJobsFromFirestore() {

        db.collection("request_service_vehicle")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    jobs.clear();

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {

                        CarJob job = doc.toObject(CarJob.class);

                        if (job != null) {
                            jobs.add(job);
                        }
                    }

                    // Remove already accepted jobs
                    jobs.removeAll(
                            ActiveJobsManager.getInstance().getActiveJobs()
                    );

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "Failed to load jobs",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    @Override
    protected void onResume() {
        super.onResume();
        jobs.removeAll(
                ActiveJobsManager.getInstance().getActiveJobs()
        );
        adapter.notifyDataSetChanged();
    }
}