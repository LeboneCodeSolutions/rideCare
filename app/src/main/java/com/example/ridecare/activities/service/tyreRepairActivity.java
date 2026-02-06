package com.example.ridecare.activities.service;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.ridecare.R;
import com.example.ridecare.models.TyreRepairRequest;
import com.example.ridecare.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class tyreRepairActivity  extends AppCompatActivity {

    String serviceType = "Tyre Repair";
    String status = "Service Requested";
    String mechanicID = null;
    String vehicleId, tyreSize;;

    EditText tyreWidth, tyreHeight, tyreDiametre, etTyreQty,etPrevPurchDate;
            AutoCompleteTextView ddTyreQuality, ddTyreBrand;
    ConstraintLayout  clSubmitTyreRepair;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_service_tyre_repair);

        tyreWidth = findViewById(R.id.tyreWidth);
        tyreHeight = findViewById(R.id.tyreHeight);
        tyreDiametre = findViewById(R.id.tyreDiametre);
        etTyreQty = findViewById(R.id.etTyreQty);
        // Date picker
        etPrevPurchDate = findViewById(R.id.etPrevPurchDate);
        // Globalise openDatePicker Function
        etPrevPurchDate.setOnClickListener(v ->  MyUtils.openDatePicker(this,  etPrevPurchDate));
        ddTyreQuality = findViewById(R.id.ddTyreQuality);

        String[] tyreQuality ={
                "Brand New",
                "Second Hand"
        };
         MyUtils.setDropdown(this, ddTyreQuality, tyreQuality);


       ddTyreBrand = findViewById(R.id.ddTyreBrand);
        String[] tyreBrands = {
                "Michelin",
                "Bridgestone",
                "Goodyear",
                "Pirelli",
                "Continental",
                "Dunlop",
                "Yokohama",
                "Hankook",
                "BFGoodrich",
                "Firestone"
        };
        MyUtils.setDropdown(this,ddTyreBrand,tyreBrands);

        clSubmitTyreRepair = findViewById(R.id.clSubmitTyreRepair);

        // Submit Botton
        clSubmitTyreRepair.setOnClickListener(v -> submitRequest());
    }
// Leave comments when you are done here
    private void submitRequest(){

        String tyreWidthString = MyUtils.newStr(tyreWidth);
        String tyreHeightString = MyUtils.newStr(tyreHeight);
        String tyreDiametreString = MyUtils.newStr(tyreDiametre);
        String TyreQtyString = MyUtils.newStr(etTyreQty);
        String prevPurchaseDateString = MyUtils.newStr(etPrevPurchDate);
        String tyreBrand = MyUtils.newStr(ddTyreBrand);
        String tyreQuality = MyUtils.newStr(ddTyreQuality);

        if (!MyUtils.requireString(tyreWidthString, tyreWidth, "Tyre width required")) return;
        if (!MyUtils.requireString(tyreHeightString, tyreHeight, "Tyre height required")) return;
        if (!MyUtils.requireString(tyreDiametreString, tyreDiametre, "Tyre diameter required")) return;
        if(!MyUtils.requireString(TyreQtyString, etTyreQty,"Tyre Quantity required")) return;
        if(!MyUtils.requireString(prevPurchaseDateString,etPrevPurchDate,"Date Required")) return;
        if(!MyUtils.requireString(tyreBrand,ddTyreBrand,"Please make selection")) return;
        if(!MyUtils.requireString(tyreQuality,ddTyreQuality,"Please make selection")) return;

        tyreSize = tyreWidthString + " " + tyreHeightString + " " + "R" + tyreDiametreString;

        // User Id Check
        String uid = MyUtils.UidCheck(this, auth);
        if (uid == null) return;

        vehicleId = MyUtils.vehicleIdCheck(this);
        if (vehicleId == null) return;

        DocumentReference docRef = db.collection("request_service_vehicle").document();

        db.collection("users").document(uid).get().addOnSuccessListener(userDoc -> {
            if (!MyUtils.requireDocument(userDoc, this, "User not found")) return;
            db.collection("vehicles").document(vehicleId).get().addOnSuccessListener(vehicleDoc -> {
                if (!MyUtils.requireDocument(vehicleDoc, this, "Vehicle")) return;

                TyreRepairRequest request = new TyreRepairRequest();
                request.setServiceRequestId(docRef.getId());
                request.setUserId(uid);
                request.setVehicleID(vehicleId);
                request.setMechanicId(mechanicID);
                request.setStatus(status);
                request.setServiceType(serviceType);
                request.setVehicleReg(vehicleDoc.getString("registrationNumber"));
                request.setVinNumber(vehicleDoc.getString("vin"));
                request.setVehicleMake(vehicleDoc.getString("make"));
                request.setVehicleModel(vehicleDoc.getString("model"));
                request.setTyreSize(tyreSize);
                request.setTyreQty(TyreQtyString);
                request.setPrevPurchaseDate(prevPurchaseDateString);
                request.setTyreBrand(tyreBrand);
                request.setTyreQuality(tyreQuality);

                MyUtils.saveAndClose(this, docRef, request, this);

            });
        });
    }

}

