package com.example.ridecare.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

import com.example.ridecare.TestFeatures.RegNumberValidation;
import com.example.ridecare.TestFeatures.VinDecoder;
import com.example.ridecare.models.Vehicle;
import com.example.ridecare.utils.FirebaseUtils;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddVehicleViewModel extends ViewModel {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainHandler = new Handler(Looper.getMainLooper());

    // LiveData to communicate back to the Activity
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MutableLiveData<String> saveStatus = new MutableLiveData<>();
    private MutableLiveData<String> transmissionType = new MutableLiveData<>("");
    private MutableLiveData<String> fuelType = new MutableLiveData<>("");
    private MutableLiveData<String> bodyType = new MutableLiveData<>("");
    private MutableLiveData<String> serviceHistory = new MutableLiveData<>("");


    // Functionality Importing from TestFeatures Folder
    private final RegNumberValidation validator = new RegNumberValidation();
    private final VinDecoder decoder = new VinDecoder();
    private final VinDecoder wmi = new VinDecoder();

    //Setters & Getters
    public LiveData<String> getSaveStatus() {
        return saveStatus;
    }
    public LiveData<String>getBodyType(){
        return bodyType;
    }
    public LiveData<String> getFuelType() {
        return fuelType;
    }
    public LiveData<String> getTransmissionType() {
        return transmissionType;
    }
    public LiveData<String> getServiceHistoryType(){
        return serviceHistory;
    }
    public void setFuelType(String value) {
        fuelType.setValue(value);
    }
    public void setTransmissionType(String value) {
        transmissionType.setValue(value);
    }
    public void setBodyType(String value) {
        bodyType.setValue(value);
    }
    public void setServiceHistoryType(String value) {
        serviceHistory.setValue(value);
    }


    // Province Identifier Function
    public String provincialCode(String plateInput) {
        String province = validator.identifyProvince(plateInput);
        if (province != null) {
            return province;
        } else {
            return "Unknown or unrecognised licence plate";
        }
    }
    // Check if vin matches year input
    public String verifyVIN(String vin, int year){
        String decodeVin = decoder.vinDecoderFinal(vin, year);
        return decodeVin;
    }

    public void saveVehicle(String make, String model, int year, String vin,
                            String reg, String transmissionType, String fuelType,
                            String bodyType, String serviceHistory, String mileage) {
        if (!MyUtils.errEmptyVal(saveStatus, make))    return;
        if (!MyUtils.errEmptyVal(saveStatus, model))   return;
        if (!MyUtils.errEmptyVal(saveStatus, vin))     return;
        if (!MyUtils.errEmptyVal(saveStatus, reg))     return;
        if (!MyUtils.errEmptyVal(saveStatus, mileage)) return;



        executor.execute(() -> {
            try {
                mainHandler.post(() -> {
                    String province = provincialCode(reg);
                    String verifiedVIN = verifyVIN(vin, year);
                    String userId = FirebaseUtils.getUid();
                    Vehicle vehicle = new Vehicle(userId, make, model, year, vin, reg,
                            province, transmissionType, fuelType,
                            bodyType, serviceHistory, verifiedVIN, mileage);
                    DocumentReference docRef = FirebaseUtils.newEntityRef(firestore, "vehicles");
                    vehicle.setVehicleId(docRef.getId());
                    FirebaseUtils.saveAndClose(saveStatus, docRef, vehicle);
                });

            } finally {
                executor.shutdown();
            }
        });

    }
}