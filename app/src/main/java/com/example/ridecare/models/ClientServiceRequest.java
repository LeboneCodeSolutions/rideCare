package com.example.ridecare.models;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents a service request created by a client.
 * Stored in Firestore under serviceRequests collection.
 */
public class ClientServiceRequest implements Serializable {

    // Firestore document ID
    private String serviceRequestId;

    // Relationships
    private String userId;      // client UID
    private String vehicleId;   // linked vehicle document ID

    // Service details
    private String serviceType;
    private String status;      // pending, in_progress, completed
    private String description;

    private String servicePrevDate;
    private String odometerReading;
    // Metadata
    @ServerTimestamp
    private Date createdAt;

    // REQUIRED empty constructor for Firebase
    public void clientServiceRequest() {}

    // Constructor used when creating a new request
    public void clientServiceRequest(
            String userId,
            String vehicleId,
            String serviceType,
            String description,
            String servicePrevDate,
            String odometerReading
    ) {
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.serviceType = serviceType;
        this.description = description;
        this.servicePrevDate = servicePrevDate;
        this.odometerReading = odometerReading;
        this.status = Status.PENDING;
    }

    // ---------- Getters & Setters ----------

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }
    public String getServicePrevDate(){
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
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    // Firestore handles this automatically
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // ---------- Status Constants ----------
    public static class Status {
        public static final String PENDING = "pending";
        public static final String IN_PROGRESS = "in_progress";
        public static final String COMPLETED = "completed";
    }
}
