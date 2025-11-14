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

public class ActiveJobAdapter extends RecyclerView.Adapter<ActiveJobAdapter.ViewHolder> {

    private List<CarJob> jobs;
    private OnCompleteClickListener listener;

    public interface OnCompleteClickListener {
        void onComplete(CarJob job);
    }

    public ActiveJobAdapter(List<CarJob> jobs, OnCompleteClickListener listener) {
        this.jobs = jobs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarJob job = jobs.get(position);
        holder.tvClientName.setText(job.getClientName() + " " + job.getClientSurname());
        holder.tvEmail.setText(job.getClientEmail());
        holder.tvRegistration.setText("Reg: " + job.getCarRegistration());
        holder.tvVin.setText("VIN: " + job.getVin());

        holder.btnComplete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComplete(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvClientName, tvEmail, tvRegistration, tvVin;
        Button btnComplete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRegistration = itemView.findViewById(R.id.tvRegistration);
            tvVin = itemView.findViewById(R.id.tvVin);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }
}