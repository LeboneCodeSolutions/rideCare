package com.example.ridecare.models;

import java.io.Serializable;

public class ServiceRequest implements Serializable {

    // 🔹 IDs
    private String serviceRequestId;
    private String userId;
    private String mechanicId;
    private String vehicleId;

    // 🔹 Client info
    private String clientEmail;
    private String fName;
    private String lName;

    // 🔹 Vehicle info
    private String vehicleReg;
    private String vinNumber;

    // 🔹 Service info
    private String serviceType;
    private String status;
    private String description;

    // 🔹 REQUIRED empty constructor (Firebase)
    public ServiceRequest() {}

    // 🔹 MAIN constructor (used when creating a request)
    public ServiceRequest(
            String userId,
            String mechanicId,
            String vehicleId,
            String serviceType,
            String status,
            String description,
            String fName,
            String lName,
            String clientEmail,
            String vehicleReg,
            String vinNumber
    ) {
        this.userId = userId;
        this.mechanicId = mechanicId;
        this.vehicleId = vehicleId;
        this.serviceType = serviceType;
        this.status = status;
        this.description = description;
        this.fName = fName;
        this.lName = lName;
        this.clientEmail = clientEmail;
        this.vehicleReg = vehicleReg;
        this.vinNumber = vinNumber;
    }

    // 🔹 Getters & Setters

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(String mechanicId) {
        this.mechanicId = mechanicId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
