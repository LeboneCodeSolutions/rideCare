package com.example.ridecare.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R;
import com.example.ridecare.activities.mechanic.MechanicJobDetailsActivity;
import com.example.ridecare.models.ServiceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MechanicJobAdapter extends RecyclerView.Adapter<MechanicJobAdapter.ViewHolder> {

    private final ArrayList<ServiceRequest> list;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public MechanicJobAdapter(ArrayList<ServiceRequest> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mechanic_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServiceRequest serviceReq = list.get(position);

        holder.service.setText("Service Type: " + serviceReq.getServiceType());
        holder.vehicle.setText("Vehicle ID: " + serviceReq.getVehicleID());
        holder.status.setText("Status: " + serviceReq.getStatus());

        // 🔹 Open job details
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), MechanicJobDetailsActivity.class);
            i.putExtra("job_data", serviceReq);
            v.getContext().startActivity(i);
        });

        // 🔹 Accept job logic
        if ("pending".equals(serviceReq.getStatus())) {

            holder.accept.setVisibility(View.VISIBLE);

            holder.accept.setOnClickListener(v -> {

                if (auth.getCurrentUser() == null) {return;}

                String mechanicId = auth.getCurrentUser().getUid();
                String requestId = serviceReq.getServiceRequestId();

                db.collection("service_requests")
                        .document(requestId)
                        .update(
                                "status", "in-progress",
                                "mechanicId", mechanicId
                        )
                        .addOnSuccessListener(unused -> {
                            serviceReq.setStatus("in-progress");
                          //  s.setMechanicId(mechanicId);
                            holder.status.setText("Status: in-progress");
                            holder.accept.setVisibility(View.GONE);
                        });
            });

        } else {
            holder.accept.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 🔹 ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView service, vehicle, status;
        Button accept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            service = itemView.findViewById(R.id.tvJobService);
            vehicle = itemView.findViewById(R.id.tvJobVehicle);
            status = itemView.findViewById(R.id.tvJobStatus);
            accept = itemView.findViewById(R.id.btnAcceptJob);
        }
    }
}
