package com.example.ridecare.models;

import java.io.Serializable;

/**
 * OilChangeRequest
 *
 * Extends the base ServiceRequest model and adds oil-specific fields.
 * Used to store oil change service requests in Firestore.
 */
public class OilChangeRequest extends ServiceRequest implements Serializable {

    // Oil-specific fields
    private String odometerReading;
    private String prevServiceDate;

    // Required empty constructor for Firestore
    public OilChangeRequest() {
        super();
    }

    // Full constructor
    public OilChangeRequest(
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

            // Oil change specific
            String odometerReading,
            String prevServiceDate
    ) {
        super(
                serviceRequestId,
                mechanicId,
                vehicleId,
                userId,
                vehicleReg,
                vinNumber,
                vehicleMake,
                vehicleModel,
                serviceType,
                status
        );

        // Set oil-specific fields
        this.odometerReading = odometerReading;
        this.prevServiceDate = prevServiceDate;
    }

    // =============================
    // Getters & Setters
    // =============================

    public String getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(String odometerReading) {
        this.odometerReading = odometerReading;
    }

    public String getPrevServiceDate() {
        return prevServiceDate;
    }

    public void setPrevServiceDate(String prevServiceDate) {
        this.prevServiceDate = prevServiceDate;
    }
}
