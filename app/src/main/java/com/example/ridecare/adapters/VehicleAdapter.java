package com.example.ridecare.adapters; // Use your actual package name

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridecare.R; // Use your actual R file import
import com.example.ridecare.models.Vehicle; // Use your actual model import

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VH> {
    public interface OnVehicleAction {
        void onBook(Vehicle vehicle);
        void onEdit(Vehicle vehicle);
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
                .inflate(R.layout.item_vehicle_update_1, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        // Gets the current vehicle object
        Vehicle vehicle = vehicleList.get(position);

        // Binds the data to the TextViews in the item layout
        String title = vehicle.getMake() + " " + vehicle.getModel();
        //String odometerReading = vehicle.getOdometerReading
        String vinNum = vehicle.getVin();
        // Developer note: this doesn't show
        String vehicleReg = vehicle.getRegistrationNumber();

        holder.tvVehicleTitle.setText(title);
        holder.tvVehicleReg.setText(vehicleReg);
        holder.tvVinNum.setText(vinNum);
        holder.cardService.setOnClickListener(v -> listener.onBook(vehicle));

    }

    @Override
    public int getItemCount() {
        // Returns the total number of items in the list
        return vehicleList.size();
    }

    // ViewHolder class to hold the views for each item
    static class VH extends RecyclerView.ViewHolder {
        TextView tvVehicleTitle, tvOdometerReading, tvVinNum, tvVehicleReg;
        ConstraintLayout cardService;
        public VH(@NonNull View itemView) {
            super(itemView);
            
            // Finds the views from the inflated layout (item_vehicle.xml)
            tvVehicleTitle = itemView.findViewById(R.id.titleText); // Make sure these IDs exist in item_vehicle.xml

            tvOdometerReading = itemView.findViewById(R.id.tvOdometerReading);
            tvVinNum = itemView.findViewById(R.id.tvVinNum);
            tvVehicleReg = itemView.findViewById(R.id.tvVehicleReg);
            cardService = itemView.findViewById(R.id.cardService);

        }
    }
}