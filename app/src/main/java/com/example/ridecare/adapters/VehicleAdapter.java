package com.example.ridecare.adapters; // Use your actual package name

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
                .inflate(R.layout.item_vehicle, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        // Gets the current vehicle object
        Vehicle vehicle = vehicleList.get(position);

        // Binds the data to the TextViews in the item layout
        String makeModel = vehicle.getMake() + " " + vehicle.getModel();


// find my utils to cobvert int to string
        int year = vehicle.getYear();
        String strYear = String.valueOf(year);



        String fuelType = vehicle.getFuelType();
        String vinNum = vehicle.getVin();
        String bodyType = vehicle.getBodyType();
        String mileage = vehicle.getMileage();
        // Developer note: this doesn't show
        String vehicleReg = vehicle.getRegistrationNumber();
        String subTitle = vehicle.getTransmissionType() + " · " + vehicle.getVinDecoded();




        holder.tvModelMake.setText(makeModel.toUpperCase());
        holder.tvRegistration.setText(vehicleReg);
        holder.tvVin.setText(vinNum);
        holder.tvFuel.setText(fuelType.toUpperCase());
        holder.tvBodyType.setText(bodyType.toUpperCase());
        holder.tvMileage.setText(mileage);
        holder.tvYear.setText(strYear);
        holder.subTitle.setText(subTitle);

        holder.cardService.setOnClickListener(v -> listener.onBook(vehicle));

    }

    @Override
    public int getItemCount() {
        // Returns the total number of items in the list
        return vehicleList.size();
    }

    // ViewHolder class to hold the views for each item
    static class VH extends RecyclerView.ViewHolder {
        TextView tvModelMake, tvYear, tvMileage, tvVin,  tvRegistration, tvFuel, tvBodyType, subTitle;
        LinearLayout cardService;
        public VH(@NonNull View itemView) {
            super(itemView);
            
            // Finds the views from the inflated layout (item_vehicle.xml)
            tvModelMake = itemView.findViewById(R.id.tvModelMake);
            tvMileage = itemView.findViewById(R.id.tvMileage);
            tvVin = itemView.findViewById(R.id.tvVin);
            tvRegistration = itemView.findViewById(R.id.tvRegistration);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvFuel = itemView.findViewById(R.id.tvFuel);
            tvBodyType = itemView.findViewById(R.id.tvBodyType);
            subTitle = itemView.findViewById(R.id.subTitle);


            cardService = itemView.findViewById(R.id.cardService);

        }
    }
}