package com.example.ridecare.models;

import java.io.Serializable;

public class TyreRepairRequest extends ServiceRequest implements Serializable {

    // Tyre-specific fields
    private String tyreSize;
    private String tyreQty;
    private String prevPurchaseDate;
    private String tyreBrand;
    private String tyreQuality;


    public TyreRepairRequest() {
        super();
    }

    // Main constructor
    public TyreRepairRequest(
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

            // Tyre-specific
            String tyreSize,
            String tyreQty,
            String prevPurchaseDate,
            String tyreBrand,
            String tyreQuality
    ) {

        // Calling parent constructor
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

        // Set tyre fields
        this.tyreSize = tyreSize;
        this.tyreQty = tyreQty;
        this.prevPurchaseDate = prevPurchaseDate;
        this.tyreBrand = tyreBrand;
        this.tyreQuality = tyreQuality;
    }

    // Getters & Setters

    public String getTyreSize() { return tyreSize; }
    public void setTyreSize(String tyreSize) { this.tyreSize = tyreSize; }

    public String getTyreQty() { return tyreQty; }
    public void setTyreQty(String tyreQty) { this.tyreQty = tyreQty; }

    public String getPrevPurchaseDate() { return prevPurchaseDate; }
    public void setPrevPurchaseDate(String prevPurchaseDate) { this.prevPurchaseDate = prevPurchaseDate; }

    public String getTyreBrand() { return tyreBrand; }
    public void setTyreBrand(String tyreBrand) { this.tyreBrand = tyreBrand; }

    public String getTyreQuality() { return tyreQuality; }
    public void setTyreQuality(String tyreQuality) { this.tyreQuality = tyreQuality; }
}
