package com.example.ridecare.models;

import java.io.Serializable;

public class ServiceRequest implements Serializable {


    // IDs
    private String serviceRequestId;

    // Client info
    private String userId;
// Mechanic Info
    private String mechanicId;
    // Vehicle info
    private String vehicleReg;
    private String vinNumber;
    private String vehicleMake;
    private String vehicleModel;
    private Integer vehicleYear;
    private String vehicleID;
    // Service info
    private String serviceType;
    private String status;
    private String servicePrevDate;
    private String odometerReading;
    private String clientDescription;

    // REQUIRED empty constructor (Firebase)
    public ServiceRequest() {}
// This will handle what the mechanic will see
    // MAIN constructor
    public ServiceRequest(
            String serviceRequestId,
            String mechanicId,
            String vehicleId,
            String userId,
            String vehicleReg,
            String vinNumber,
            String vehicleMake,
            String vehicleModel,
            String serviceType,
            String status,
            String clientDescription,
            String servicePrevDate,
            String odometerReading
    ) {
        this.serviceRequestId = serviceRequestId;
        this.vehicleID = vehicleId;
        this.userId = userId;
        this.vehicleReg = vehicleReg;
        this.vinNumber = vinNumber;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.mechanicId = mechanicId;
        this.serviceType = serviceType;
        this.status = status;
        this.clientDescription = clientDescription;
        this.servicePrevDate = servicePrevDate;
        this.odometerReading = odometerReading;
    }

    //  Getters & Setters


    public String getMechanicId(){
        return mechanicId;
    }
    public void setMechanicId(){
        this.mechanicId = mechanicId;
    }
    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getVehicleID(){
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleReg() {
        return vehicleReg;
    }

    public void setVehicleReg(String vehicleReg) {
        this.vehicleReg = vehicleReg;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }


    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientDescription() {
        return clientDescription;
    }

    public void setClientDescription(String clientDescription) {
        this.clientDescription = clientDescription;
    }

    public String getServicePrevDate(String serviceDate){
        return servicePrevDate;
    }

    public String getOdometerReading(){
        return odometerReading;
    }

    public void setOdometerReading(String odometerReading) {
        this.odometerReading = odometerReading;
    }

    public void setServicePrevDate(String servicePrevDate){
        this.servicePrevDate = servicePrevDate;
    }
}
