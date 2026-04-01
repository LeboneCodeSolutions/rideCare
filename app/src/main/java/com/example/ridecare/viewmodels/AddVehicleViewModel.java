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
    private final RegNumberValidation validator = new RegNumberValidation();
    private final VinDecoder decoder = new VinDecoder();
    private final VinDecoder wmi = new VinDecoder();
    public LiveData<String> getSaveStatus() {
        return saveStatus;
    }


    // Change to myUtils
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
    public void saveVehicle(String make, String model, int year, String vin, String reg) {
        // Note revisit intefaces for your myutils implementation
        // validation lives here now, not in Activity
        if (!MyUtils.errEmptyVal(saveStatus, make))  return;
        if (year == 0) {
            saveStatus.setValue("error:year");
            return;
        }
        if (!MyUtils.errEmptyVal(saveStatus, vin))  return;
        if (!MyUtils.errEmptyVal(saveStatus, reg))  return;

         String registrationRegion =  validatePlate(reg);

         // add to database system note
         String vinDecoded = validateVIN(vin, year);

         // vin approved vin
         String wmiMake = wmi(vin);
         // Compare make with wmiMake


        // Refer To Firebase MyUtils
        String userId = FirebaseUtils.getUid();
        Vehicle vehicle = new Vehicle(userId, wmiMake, model, year, vin, reg, registrationRegion);

        DocumentReference docRef = FirebaseUtils.newEntityRef(firestore, "vehicles");
        vehicle.setVehicleId(docRef.getId());
        FirebaseUtils.saveAndClose(saveStatus, docRef, vehicle);
        }
}