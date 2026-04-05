package com.example.ridecare.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ridecare.TestFeatures.RegNumberValidation;
import com.example.ridecare.TestFeatures.VinDecoder;
import com.example.ridecare.models.Vehicle;
import com.example.ridecare.utils.FirebaseUtils;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddVehicleViewModel extends ViewModel {
    // LiveData to communicate back to the Activity
    private MutableLiveData<String> saveStatus = new MutableLiveData<>();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    private MutableLiveData<String> transmissionType = new MutableLiveData<>("");
    private MutableLiveData<String> fuelType = new MutableLiveData<>("");
    private MutableLiveData<String> bodyType = new MutableLiveData<>("");
    private MutableLiveData<String> serviceHistory = new MutableLiveData<>("");
    private MutableLiveData<Boolean> serviceBook = new MutableLiveData<>(false);


    private final RegNumberValidation validator = new RegNumberValidation();
    private final VinDecoder decoder = new VinDecoder();
    private final VinDecoder wmi = new VinDecoder();
    public LiveData<String> getSaveStatus() {
        return saveStatus;
    }


    // Change to myUtils
    public LiveData<String> getFuelType() {
        return fuelType;
    }
    public void setFuelType(String value) {
        fuelType.setValue(value);
    }
    public LiveData<String> getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String value) {
        transmissionType.setValue(value);
    }


    public LiveData<String>getBodyType(){
        return bodyType;
    }
    public void setBodyType(String value) {
        bodyType.setValue(value);
    }


    public LiveData<String> getServiceHistoryType(){
        return serviceHistory;
    }
    public void setServiceHistoryType(String value) {
        serviceHistory.setValue(value);
    }


    public LiveData<Boolean> getIsServiceBookPresent(){
        return serviceBook;
    }

    public void setIsServiceBookPresent(boolean isChecked) {
        serviceBook.setValue(isChecked);
    }



    public String validatePlate(String plateInput) {
        String province = validator.identifyProvince(plateInput);
        if (province != null) {
            return province;
        } else {
            return "Unknown or unrecognised licence plate";
        }
    }
    public String validateVIN(String vin, int year){
        String decodeVin = decoder.vinDecoderFinal(vin, year);
        return decodeVin;
    }
    public String wmi(String vin){
        String wmiOutput = wmi.wmiChecker(vin);
        return wmiOutput;
    }
    // build all validation
    public void saveVehicle(String make, String model, int year, String vin, String reg, String transmissionType, String fuelType,  String bodyType, String serviceHistory, Boolean isServiceBookPresent) {
        // Note revisit intefaces for your myutils implementation
        // validation lives here now, not in Activity
        if (!MyUtils.errEmptyVal(saveStatus, make))  return;
        if (!MyUtils.errEmptyVal(saveStatus, model))  return;
       // if(!MyUtils.yearLimit(saveStatus, year)) return;
        if (!MyUtils.errEmptyVal(saveStatus, vin))  return;
        if (!MyUtils.errEmptyVal(saveStatus, reg))  return;

         String registrationRegion =  validatePlate(reg);
         // add to database system note
         String vinDecoded = validateVIN(vin, year);

         // vin approved vin
         String verifiedMake = wmi(vin);

        
        // Refer To Firebase MyUtils
        String userId = FirebaseUtils.getUid();
        // Update the vehicle class



        Vehicle vehicle = new Vehicle(userId, verifiedMake, model, year, vin, reg, registrationRegion, transmissionType, fuelType, bodyType, serviceHistory, isServiceBookPresent, vinDecoded);

        DocumentReference docRef = FirebaseUtils.newEntityRef(firestore, "vehicles");
        vehicle.setVehicleId(docRef.getId());
        FirebaseUtils.saveAndClose(saveStatus, docRef, vehicle);
        }



}