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
import com.google.firebase.firestore.QuerySnapshot;

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
        adapter = new JobAdapter(jobs, new JobAdapter.OnAcceptClickListener() {
            @Override
            public void onAccept(CarJob job) {
                int position = jobs.indexOf(job);
                if (position != -1) {
                    jobs.remove(position);
                    adapter.notifyItemRemoved(position);
                    ActiveJobsManager.getInstance().addJob(job);

                    Toast.makeText(JobBoardActivity.this,
                            "Job accepted: " + job.getClientName(),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(JobBoardActivity.this, MechanicDashboardActivity.class);
                    startActivity(intent);
                }
            }
        });
        rvJobList.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchJobsFromFirestore();
    }

    private void fetchJobsFromFirestore() {
        db.collection("service_requests")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        jobs.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                String firstName = doc.getString("firstName");
                                String lastName = doc.getString("lastName");
                                String email = doc.getString("email");
                                String registration = doc.getString("vehicleRegistration");
                                String vin = doc.getString("vinNumber");

                                CarJob job = new CarJob(firstName, lastName, email, registration, vin);
                                jobs.add(job);
                            }
                            // Remove jobs already accepted
                            List<CarJob> activeJobs = ActiveJobsManager.getInstance().getActiveJobs();
                            jobs.removeAll(activeJobs);

                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(JobBoardActivity.this,
                                "Failed to load service requests",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh job board
        List<CarJob> activeJobs = ActiveJobsManager.getInstance().getActiveJobs();
        jobs.removeAll(activeJobs);
        adapter.notifyDataSetChanged();
    }
}
