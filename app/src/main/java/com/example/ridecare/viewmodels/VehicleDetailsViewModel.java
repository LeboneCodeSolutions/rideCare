package com.example.ridecare.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ridecare.models.Vehicle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class VehicleDetailsViewModel extends ViewModel {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    // Vehicle Data
    private final MutableLiveData<Vehicle> vehicleLiveData = new MutableLiveData<>();

    // Navigation events
    private final MutableLiveData<Boolean> openOilChange = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openTyreRepair = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openBrakes = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openBattery = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openOverhaul = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openTowing = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> copyVin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> editMileage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> goBack = new MutableLiveData<>();

    //  Getters
    public LiveData<Vehicle> getVehicle() { return vehicleLiveData; }

    public LiveData<Boolean> openOilChange() { return openOilChange; }
    public LiveData<Boolean> openTyreRepair() { return openTyreRepair; }
    public LiveData<Boolean> openBrakes() { return openBrakes; }
    public LiveData<Boolean> openBattery() { return openBattery; }
    public LiveData<Boolean> openOverhaul() { return openOverhaul; }
    public LiveData<Boolean> openTowing() { return openTowing; }

    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getCopyVin() { return copyVin; }
    public LiveData<Boolean> getEditMileage() { return editMileage; }
    public LiveData<Boolean> getGoBack() { return goBack; }

    //  Validation
    private boolean isValidUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private boolean isValidVehicleId(String vehicleId) {
        return vehicleId != null && !vehicleId.trim().isEmpty();
    }

    public void loadVehicle(String vehicleId) {
        if (!isValidVehicleId(vehicleId)) {
            errorMessage.setValue("Invalid Vehicle ID");
            return;
        }

        firestore.collection("vehicles")
                .document(vehicleId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Vehicle vehicle = doc.toObject(Vehicle.class);
                        vehicleLiveData.setValue(vehicle);
                    } else {
                        errorMessage.setValue("Vehicle not found");
                    }
                })
                .addOnFailureListener(e ->
                        errorMessage.setValue("Failed to load vehicle")
                );
    }

    // Button actions

    public void onOilChangeClicked(String vehicleId) {
        if (!isValidUser()) {
            errorMessage.setValue("User not authenticated");
            return;
        }

        if (isValidVehicleId(vehicleId)) {
            openOilChange.setValue(true);
        } else {
            errorMessage.setValue("Invalid Vehicle ID");
        }
    }

    public void onTyreRepairClicked(String vehicleId) {
        if (isValidVehicleId(vehicleId)) {
            openTyreRepair.setValue(true);
        } else {
            errorMessage.setValue("Invalid Vehicle ID");
        }
    }

    public void onBrakesClicked(String vehicleId) {
        if (isValidVehicleId(vehicleId)) {
            openBrakes.setValue(true);
        } else {
            errorMessage.setValue("Invalid Vehicle ID");
        }
    }

    public void onBatteryClicked(String vehicleId) {
        if (isValidVehicleId(vehicleId)) {
            openBattery.setValue(true);
        } else {
            errorMessage.setValue("Invalid Vehicle ID");
        }
    }

    public void onOverhaulClicked(String vehicleId) {
        if (isValidVehicleId(vehicleId)) {
            openOverhaul.setValue(true);
        } else {
            errorMessage.setValue("Invalid Vehicle ID");
        }
    }

    public void onTowingClicked(String vehicleId) {
        if (isValidVehicleId(vehicleId)) {
            openTowing.setValue(true);
        } else {
            errorMessage.setValue("Invalid Vehicle ID");
        }
    }


    public void onCopyVinClicked() {
        copyVin.setValue(true);
    }

    public void onEditMileageClicked() {
        editMileage.setValue(true);
    }

    public void onBackClicked() {
        goBack.setValue(true);
    }
}