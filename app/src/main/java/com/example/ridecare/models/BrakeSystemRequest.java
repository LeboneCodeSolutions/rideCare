package com.example.ridecare.models;



import java.io.Serializable;

public class BrakeSystemRequest extends ServiceRequest implements Serializable {


    private String odometerReading;
    private String prevBrakeJob;
    private String brakeNoise;
    private String brakeNoiseRating;
    private String brakeJobSide;


    public BrakeSystemRequest(){super();}


    public BrakeSystemRequest(
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
            // Brake System Specific
            String odometerReading,
            String prevBrakeJob,
            String brakeNoise,
            String brakeNoiseRating,
            String brakeJobSide
            ){
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

        // Set Fields
        this.brakeJobSide =brakeJobSide; //
        this.odometerReading = odometerReading; //
        this.prevBrakeJob = prevBrakeJob; //
        this.brakeNoise = brakeNoise; //
        this.brakeNoiseRating = brakeNoiseRating;

    }

    public String getOdometerReading(){return odometerReading;}
    public void setOdometerReading(String odometerReading){this.odometerReading = odometerReading;}

    public String getPrevBrakeJob(){return prevBrakeJob;}
    public void setPrevBrakeJob(String prevBrakeJob){this.prevBrakeJob = prevBrakeJob;}

    public String getBrakeNoise(){return brakeNoise;}
    public void setBrakeNoise(String brakeNoise){this.brakeNoise = brakeNoise;}

    public String getBrakeNoiseRating(){return brakeNoiseRating;}
    public void setBrakeNoiseRating(String brakeNoiseRating){this.brakeNoiseRating = brakeNoiseRating;}


    public String getBrakeJobSide(){return brakeJobSide;}
    public void setBrakeJobSide(String brakeJobSide){this.brakeJobSide = brakeJobSide;}

}
