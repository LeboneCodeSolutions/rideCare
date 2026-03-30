package com.example.ridecare.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ridecare.models.Vehicle;
import com.example.ridecare.utils.FirebaseUtils;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddVehicleViewModel extends ViewModel {
    // LiveData to communicate back to the Activity
    private MutableLiveData<String> saveStatus = new MutableLiveData<>();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public LiveData<String> getSaveStatus() {
        return saveStatus;
    }


    // build all validatoin
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


        // Refer To Firebase MyUtils
        String userId = FirebaseUtils.getUid();
        Vehicle vehicle = new Vehicle(userId, make, model, year, vin, reg);

        DocumentReference docRef = FirebaseUtils.newEntityRef(firestore, "vehicles");
         vehicle.setVehicleId(docRef.getId());
         FirebaseUtils.saveAndClose(saveStatus, docRef, vehicle);
        }
}