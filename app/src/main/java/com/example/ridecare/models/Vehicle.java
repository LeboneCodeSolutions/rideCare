package com.example.ridecare.models;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Vehicle {

    private String id;
    private String userId;
    private String make;
    private String model;
    private int year;
    private String vin;
    private String regNumber;
    @ServerTimestamp
    private Date addedAt;

    // Empty constructor required for Firestore
    public Vehicle() {}

    public Vehicle(String userId, String make, String model, int year, String vin, String regNumber) {
        this.userId = userId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.regNumber = regNumber;
    }

    // Getters & Setters for all fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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


    public Date getAddedAt() { return addedAt; }
    public void setAddedAt(Date addedAt) { this.addedAt = addedAt; }

    public void setVehicleId(String id) {
    }
}
