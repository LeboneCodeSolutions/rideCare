package com.example.ridecare.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R;
import com.example.ridecare.models.ServiceRequest;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.ViewHolder> {

    // ─── Click listener interface ──────────────────────────────────────────────
    public interface OnItemClickListener {
        void onItemClick(ServiceRequest serviceRequest);
    }

    private List<ServiceRequest> list;
    private final OnItemClickListener listener;

    // ─── Constructor (now accepts click listener) ──────────────────────────────
    public ServiceRequestAdapter(List<ServiceRequest> list, OnItemClickListener listener) {
        this.list     = list != null ? list : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_request_mini_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRequest serviceReq = list.get(position);
        if (serviceReq == null) return;

        holder.serviceType.setText(serviceReq.getServiceType() != null
                ? serviceReq.getServiceType()
                : "Service type not selected");

        holder.status.setText("Status: " + (serviceReq.getStatus() != null
                ? serviceReq.getStatus()
                : "Active"));

        holder.vehicleName.setText(serviceReq.getVehicleMake() != null
                ? serviceReq.getVehicleMake() + " " + serviceReq.getVehicleModel()
                : "Vehicle Name Unknown");

        holder.vehicleIdentityNumber.setText(serviceReq.getVinNumber() != null
                ? serviceReq.getVinNumber()
                : "Vin Number Unknown");

        holder.registrationNumber.setText(serviceReq.getVehicleReg() != null
                ? serviceReq.getVehicleReg()
                : "Registration Unknown");

        // ✅ Notify activity which item was tapped
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(serviceReq);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setItems(List<ServiceRequest> serviceList) {
        this.list = (serviceList != null) ? serviceList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleName, status, registrationNumber, vehicleIdentityNumber, serviceType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleName           = itemView.findViewById(R.id.tvVehicleName);
            status                = itemView.findViewById(R.id.chipStatus);
            registrationNumber    = itemView.findViewById(R.id.tvRegistrationNumber);
            vehicleIdentityNumber = itemView.findViewById(R.id.tvVehicleIdentityNumber);
            serviceType           = itemView.findViewById(R.id.tvServiceType);
        }
    }
}