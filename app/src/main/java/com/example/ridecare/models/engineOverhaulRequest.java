package com.example.ridecare.models;

import java.io.Serializable;

public class engineOverhaulRequest extends ServiceRequest implements Serializable {

    private String OdometerReading;
    private String isRunning;
    private String isCheckEngineLightOn;
    private String isEngineOpen;
    private String overhaulPref;


    public engineOverhaulRequest(){super();}


    public engineOverhaulRequest(
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

            // Engine Overhaul Specific

            String odometerReading,
            String isRunning,
            String isCheckEngineLightOn,
            String isEngineOpen,
            String overhaulPref
    ){
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
        this.OdometerReading = odometerReading;
        this.isRunning = isRunning;
        this.isCheckEngineLightOn = isCheckEngineLightOn;
        this.isEngineOpen = isEngineOpen;
        this.overhaulPref = overhaulPref;
    }

    public void setOdometerReading(String odometerReading){
        this.OdometerReading = odometerReading;
    }
    public String getOdometerReading(){return OdometerReading;}
    public void setIsRunning(String isRunning){
        this.isRunning = isRunning;
    }
    public String getIsRunning(){return isRunning;}
    public void setIsCheckEngineLightOn(String isCheckEngineLightOn){
        this.isCheckEngineLightOn = isCheckEngineLightOn;
    }
    public String getIsCheckEngineLightOn(){
        return isCheckEngineLightOn;
    }

    public void setIsEngineOpen(String isEngineOpen){
        this.isEngineOpen = isEngineOpen;
    }

    public String getIsEngineOpen(){return isEngineOpen;}

    public void setOverhaulPref(String overhaulPref){
        this.overhaulPref = overhaulPref;
    }

    public String getOverhaulPref(){return overhaulPref;}
}
