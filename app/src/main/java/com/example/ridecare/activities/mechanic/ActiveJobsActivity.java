package com.example.ridecare.activities.mechanic;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R;

import java.util.List;

public class ActiveJobsActivity extends AppCompatActivity {

    RecyclerView rvActiveJobs;
    TextView tvEmptyMessage;
    ActiveJobAdapter adapter;
    List<CarJob> activeJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_jobs);

        rvActiveJobs = findViewById(R.id.rvActiveJobs);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        rvActiveJobs.setLayoutManager(new LinearLayoutManager(this));

        loadActiveJobs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActiveJobs();
    }

    private void loadActiveJobs() {
        activeJobs = ActiveJobsManager.getInstance().getActiveJobs();

        if (activeJobs.isEmpty()) {
            rvActiveJobs.setVisibility(View.GONE);
            tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            rvActiveJobs.setVisibility(View.VISIBLE);
            tvEmptyMessage.setVisibility(View.GONE);

            adapter = new ActiveJobAdapter(activeJobs, new ActiveJobAdapter.OnCompleteClickListener() {
                @Override
                public void onComplete(CarJob job) {
                    // Remove from active jobs
                    int position = activeJobs.indexOf(job);
                    if (position != -1) {
                        activeJobs.remove(position);
                        ActiveJobsManager.getInstance().removeJob(job);
                        adapter.notifyItemRemoved(position);

                        // Check if list is now empty
                        if (activeJobs.isEmpty()) {
                            rvActiveJobs.setVisibility(View.GONE);
                            tvEmptyMessage.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            rvActiveJobs.setAdapter(adapter);
        }
    }
}