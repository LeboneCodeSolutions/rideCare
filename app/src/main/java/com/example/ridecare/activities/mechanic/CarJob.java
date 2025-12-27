package com.example.ridecare.activities.mechanic;

public class CarJob {

    private String clientDescription;
    private String serviceType;
    private String status;
    private String firstname;
    private String vehicleReg;
    private String vinNumber;
    private String vehicleMake;
    private String vehicleModel;

    private String mechanicId;
    private String serviceRequestId;
    private String userId;
    private String vehicleID;

    // 🔴 REQUIRED empty constructor for Firestore
    public CarJob() {}

    // Getters


    public String getFirstname(){
        return firstname;
    }
    public String getClientDescription() {
        return clientDescription;
    }

    public String getServiceType() {
        return serviceType;
    }




    public String getStatus() {
        return status;
    }

    public String getVehicleReg() {
        return vehicleReg;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getMechanicId() {
        return mechanicId;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public String getUserId() {
        return userId;
    }

    public String getVehicleID() {
        return vehicleID;
    }
}
