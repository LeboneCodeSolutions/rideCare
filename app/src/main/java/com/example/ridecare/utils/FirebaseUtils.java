package com.example.ridecare.utils;

import com.google.firebase.auth.FirebaseAuth;
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
}
