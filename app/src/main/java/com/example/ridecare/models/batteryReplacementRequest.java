package com.example.ridecare.models;

import java.io.Serializable;

public class batteryReplacementRequest  extends ServiceRequest implements Serializable {

    private String vehicleTrim;
    private String hasStartStop;
    private String recommendedBattery;

    public batteryReplacementRequest(){
        super();
    }


    public batteryReplacementRequest(
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
            // Battery Replacement Specific
            String vehicleTrim,
            String hasStartStop,
            String recommendedBattery
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

        this.vehicleTrim = vehicleTrim;
        this.hasStartStop = hasStartStop;
        this.recommendedBattery = recommendedBattery;
    }

    public void setVehicleTrim(String vehicleTrim){this.vehicleTrim = vehicleTrim;}
    public String getVehicleTrim(){return vehicleTrim;}
    public void setHasStartStop(String hasStartStop){this.hasStartStop = hasStartStop;}
    public  String getHasStartStop(){return hasStartStop;}
    public void setRecommendedBattery(String recommendedBattery){this.recommendedBattery = recommendedBattery;}
    public String getRecommendedBattery(){return recommendedBattery;}

}
