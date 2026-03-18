package com.example.ridecare.models;

import java.io.Serializable;

public class towingRequest extends ServiceRequest implements Serializable {

    // Location Fields
    private String location;
    private String roadType;
    private String isVehicleAccessible;

    // Vehicle Information
    private String driveTrain;
    private String condition;
    private String steerLocked;
    private String canRoll;

    // Vehicle Conditions
    private String incident;
    private String isDamaged;
    private String wheelsIntact;
    private String  isSteeringLocked;



    // Payment Methods
    private String paymentType;
    private String hasAssistance;
    private String needsInvoice;

    // Recommendations

    private String towTruck;
    private String vehicleCondition;
    private String paymentMethod;
    private String towCondition;

    // Required empty constructor for Firestore
    public towingRequest() {
        super();
    }

    public towingRequest(
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

            // Location Fields
            String roadType,
            String isVehicleAccessible,
            String location,

            // Vehicle Conditions
            String incident,
            String isDamaged,
            String wheelsIntact,


            // Vehicle Information
            String driveTrain,
            String condition,
            String isSteeringLocked,
            String canRoll,

            // Payment Methods
            String paymentType,
            String hasAssistance,
            String needsInvoice,


            // Towing Recommendations
            String towTruck,
            String vehicleCondition,
            String paymentMethod,
            String towCondition
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

        // Location Fields
        this.roadType = roadType;
        this.isVehicleAccessible = isVehicleAccessible;
        this.location = location;


        // Vehicle Conditions
        this.incident = incident;
        this.isDamaged = isDamaged;
        this.wheelsIntact = wheelsIntact;


        // Vehicle Information
        this.driveTrain = driveTrain;
        this.condition = condition;
        this.canRoll = canRoll;
        this.isSteeringLocked = isSteeringLocked;
        // Payment Methods
        this.paymentType = paymentType;
        this.hasAssistance = hasAssistance;
        this.needsInvoice = needsInvoice;

        // Recommendations Method
        this.towTruck =  towTruck;
        this.vehicleCondition =  vehicleCondition;
        this.paymentMethod = paymentMethod;
        this.towCondition = towCondition;

    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    // Location Fields
    public String getRoadType() { return roadType; }
    public void setRoadType(String roadType) { this.roadType = roadType; }

    public String getIsVehicleAccessible() { return isVehicleAccessible; }
    public void setIsVehicleAccessible(String isVehicleAccessible) { this.isVehicleAccessible = isVehicleAccessible; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // Vehicle Conditions
    public String getIncident() { return incident; }
    public void setIncident(String incident) { this.incident = incident; }

    public String getIsDamaged() { return isDamaged; }
    public void setIsDamaged(String isDamaged) { this.isDamaged = isDamaged; }

    public String getWheelsIntact() { return wheelsIntact; }
    public void setWheelsIntact(String wheelsIntact) { this.wheelsIntact = wheelsIntact; }

    public String getIsSteeringLocked() { return  isSteeringLocked; }
    public void setIsSteeringLocked(String  isSteeringLocked) { this. isSteeringLocked =  isSteeringLocked; }

    // Vehicle Information
    public String getDriveTrain() { return driveTrain; }
    public void setDriveTrain(String driveTrain) { this.driveTrain = driveTrain; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getSteerLocked() { return steerLocked; }
    public void setSteerLocked(String steerLocked) { this.steerLocked = steerLocked; }

    public String getCanRoll() { return canRoll; }
    public void setCanRoll(String canRoll) { this.canRoll = canRoll; }

    // Payment Methods
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getHasAssistance() { return hasAssistance; }
    public void setHasAssistance(String hasAssistance) { this.hasAssistance = hasAssistance; }

    public String getNeedsInvoice() { return needsInvoice; }
    public void setNeedsInvoice(String needsInvoice) { this.needsInvoice = needsInvoice; }

    // Recommendations Methods

    public String getTowTruck(){return towTruck;}
    public void setTowTruck(String towTruck){this.towTruck = towTruck;}

    public String getVehicleCondition(){return vehicleCondition;}
    public void setVehicleCondition(String vehicleCondition){this.vehicleCondition = vehicleCondition;}

    public String getPaymentMethod(){return paymentMethod;}
    public void setPaymentMethod(String paymentMethod){this.paymentMethod = paymentMethod;}

    public String getTowCondition(){return towCondition;}
    public void setTowCondition(String towCondition){this.towCondition = towCondition;}


}



