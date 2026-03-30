package com.example.ridecare.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }

    public static String getUid() {
        return getAuth().getCurrentUser() != null
                ? getAuth().getCurrentUser().getUid()
                : null;
    }

    public static String getEmail(){
        return getAuth().getCurrentUser() != null
                ? getAuth().getCurrentUser().getEmail()
                : null;
    }


    public static DocumentReference newEntityRef(FirebaseFirestore firestore, String collectionPath) {
        return firestore.collection(collectionPath).document();
    }


    // Called from VIEWMODEL (new MVVM screens)
    public static void saveAndClose(MutableLiveData<String> saveStatus,
                                     DocumentReference docRef, Object data) {
        docRef.set(data)
                .addOnSuccessListener(aVoid -> saveStatus.setValue("success"))
                .addOnFailureListener(e -> saveStatus.setValue("error:" + e.getMessage()));
    }
}
