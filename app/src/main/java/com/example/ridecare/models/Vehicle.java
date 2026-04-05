package com.example.ridecare.models;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
public class Vehicle {

    private String vehicleId;
    private String userId;
    private String make;
    private String model;
    private int year;
    private String vin;
    private String regNumber, registrationRegion;
    private String transmissionType;
    private String fuelType;
    private String bodyType;
    private String serviceHistory;
    private Boolean isServiceBookPresent;
    private String vinDecoded;

    @ServerTimestamp
    private Date addedAt;

    // Empty constructor required for Firestore
    public Vehicle() {}

    public Vehicle(String userId, String make, String model, int year, String vin, String regNumber,
                   String registrationRegion, String transmissionType, String fuelType,
                   String bodyType, String serviceHistory, Boolean isServiceBookPresent, String vinDecoded) {
        this.userId = userId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.regNumber = regNumber;
        this.registrationRegion = registrationRegion;
        this.transmissionType = transmissionType;
        this.fuelType = fuelType;
        this.bodyType = bodyType;
        this.serviceHistory = serviceHistory;
        this.isServiceBookPresent = isServiceBookPresent;
        this.vinDecoded = vinDecoded;
    }

    // --- Existing Getters & Setters ---

    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getVehicleId() { return vehicleId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getRegistrationNumber() { return regNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.regNumber = registrationNumber; }

    public String getVehicleRegRegion() { return registrationRegion; }
    public void setVehicleRegRegion(String registrationRegion) { this.registrationRegion = registrationRegion; }

    public Date getAddedAt() { return addedAt; }
    public void setAddedAt(Date addedAt) { this.addedAt = addedAt; }

    // --- New Getters & Setters ---

    public String getTransmissionType() { return transmissionType; }
    public void setTransmissionType(String transmissionType) { this.transmissionType = transmissionType; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }

    public String getServiceHistory() { return serviceHistory; }
    public void setServiceHistory(String serviceHistory) { this.serviceHistory = serviceHistory; }

    public Boolean getIsServiceBookPresent() { return isServiceBookPresent; }
    public void setIsServiceBookPresent(Boolean isServiceBookPresent) { this.isServiceBookPresent = isServiceBookPresent; }

    public String getVinDecoded() { return vinDecoded; }
    public void setVinDecoded(String vinDecoded) { this.vinDecoded = vinDecoded; }
}