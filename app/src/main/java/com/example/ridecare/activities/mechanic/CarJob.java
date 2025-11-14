package com.example.ridecare.activities.mechanic;

import java.util.Objects;

public class CarJob {
    private String clientName;
    private String clientSurname;
    private String clientEmail;
    private String carRegistration;
    private String vin;

    public CarJob(String clientName, String clientSurname, String clientEmail,
                  String carRegistration, String vin) {
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientEmail = clientEmail;
        this.carRegistration = carRegistration;
        this.vin = vin;
    }

    // Getters
    public String getClientName() {
        return clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getCarRegistration() {
        return carRegistration;
    }

    public String getVin() {
        return vin;
    }

    // Setters
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public void setCarRegistration(String carRegistration) {
        this.carRegistration = carRegistration;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    // Override equals and hashCode for proper list operations
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarJob carJob = (CarJob) o;
        return Objects.equals(vin, carJob.vin) &&
                Objects.equals(carRegistration, carJob.carRegistration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin, carRegistration);
    }
}