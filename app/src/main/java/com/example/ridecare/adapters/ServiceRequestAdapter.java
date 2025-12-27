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

    private List<ServiceRequest> list ;

    public ServiceRequestAdapter(List<ServiceRequest> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // handles any null issues
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRequest serviceReq = list.get(position);
        if (serviceReq == null){
            return; /// return an error message
        }

        // Null-safe text setting to prevent crashes from incomplete data
        holder.title.setText(serviceReq.getServiceType() != null ? serviceReq.getServiceType() : "Service type not selected");
        holder.status.setText("Status: " + (serviceReq.getStatus() != null ? serviceReq.getStatus() : "Status Pending"));


        // create a temp-id which gets stored instead
        holder.vehicle.setText("Vehicle ID: " + (serviceReq.getVehicleID() != null ? serviceReq.getVehicleID() : "Unknown"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    
    public void setItems(List<ServiceRequest> serviceList) {
        this.list = (serviceList != null) ? serviceList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, vehicle, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvServiceTitle);
            vehicle = itemView.findViewById(R.id.tvServiceVehicle);
            status = itemView.findViewById(R.id.tvServiceStatus);
        }
    }
}
