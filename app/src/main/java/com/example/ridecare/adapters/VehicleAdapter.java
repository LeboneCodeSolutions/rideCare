package com.example.ridecare.adapters; // Use your actual package name

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R; // Use your actual R file import
import com.example.ridecare.models.Vehicle; // Use your actual model import

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VH> {
    public interface OnVehicleAction {
        void onBook(Vehicle vehicle);
        void onEdit(Vehicle vehicle);
        void onDelete(Vehicle vehicle);
    }

    private List<Vehicle> vehicleList;
    private Context context;

    // Constructor to initialize the list
    private OnVehicleAction listener;

    public VehicleAdapter(List<Vehicle> vehicleList, Context context, OnVehicleAction listener) {
        this.vehicleList = vehicleList;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Correctly inflates the item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicle, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        // Gets the current vehicle object
        Vehicle vehicle = vehicleList.get(position);

        // Binds the data to the TextViews in the item layout
        String title = vehicle.getMake() + " " + vehicle.getModel();
        String subtitle = "Year: " + vehicle.getYear() + " • Vehicle Reg: " + vehicle.getRegistrationNumber();

        holder.tvVehicleTitle.setText(title);
        holder.tvVehicleSubtitle.setText(subtitle);

        holder.btnBookService.setOnClickListener(v -> listener.onBook(vehicle));
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(vehicle));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(vehicle));
    }

    @Override
    public int getItemCount() {
        // Returns the total number of items in the list
        return vehicleList.size();
    }

    // ViewHolder class to hold the views for each item
    static class VH extends RecyclerView.ViewHolder {
        TextView tvVehicleTitle, tvVehicleSubtitle;
        View btnBookService, btnEdit, btnDelete;

        public VH(@NonNull View itemView) {
            super(itemView);
            // Finds the views from the inflated layout (item_vehicle.xml)
            tvVehicleTitle = itemView.findViewById(R.id.tvVehicleTitle); // Make sure these IDs exist in item_vehicle.xml
            tvVehicleSubtitle = itemView.findViewById(R.id.tvVehicleSubtitle);

            btnBookService = itemView.findViewById(R.id.btnBookService);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            // Make sure these IDs exist in item_vehicle.xml
        }
    }
}