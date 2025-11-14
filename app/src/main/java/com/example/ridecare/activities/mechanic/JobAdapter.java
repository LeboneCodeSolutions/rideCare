package com.example.ridecare.activities.mechanic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<CarJob> jobs;
    private OnAcceptClickListener listener;

    public interface OnAcceptClickListener {
        void onAccept(CarJob job);
    }

    public JobAdapter(List<CarJob> jobs, OnAcceptClickListener listener) {
        this.jobs = jobs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        CarJob job = jobs.get(position);
        holder.tvClientName.setText(job.getClientName());
        holder.tvClientEmail.setText(job.getClientEmail());


        holder.btnAcceptJob.setOnClickListener(v -> {
            if (listener != null) listener.onAccept(job);
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvClientName, tvLastName, tvClientEmail, tvVehicleReg, tvVinNumber;
        Button btnAcceptJob;

        JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvLastName = itemView.findViewById(R.id.tvLastName);
            tvClientEmail = itemView.findViewById(R.id.tvClientEmail);
            tvVehicleReg = itemView.findViewById(R.id.tvVehicleReg);
            tvVinNumber = itemView.findViewById(R.id.tvVinNumber);
            btnAcceptJob = itemView.findViewById(R.id.btnAcceptJob);
        }
    }
}
