package com.example.ridecare.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ridecare.R;
import com.example.ridecare.activities.mechanic.MechanicDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPasswordActivity extends AppCompatActivity {
    FirebaseAuth auth;

    EditText etResetEmail, etResetFirstName, etResetLastName;
    Button btnSendReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etResetEmail = findViewById(R.id.etResetEmail);
        etResetFirstName = findViewById(R.id.etResetFirstName);
        etResetLastName = findViewById(R.id.etResetLastName);
        btnSendReset = findViewById(R.id.btnSendReset);
        btnSendReset.setOnClickListener(v -> resetPassword());
    }
    private void resetPassword() {
        String resRegex = "[a-zA-Z ]+";
        String errMsg = "Only letters allowed";
        String email = etResetEmail.getText().toString().trim();
        String fName = etResetFirstName.getText().toString();
        String lName = etResetLastName.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Check if it's valid
        if (TextUtils.isEmpty(fName)) {
            etResetFirstName.setError("Enter your first name");
            return;
        } else if (TextUtils.isEmpty(lName)) {
            etResetLastName.setError("Enter your last name");
            return;

        } else if (TextUtils.isEmpty(email)) {
            etResetEmail.setError("Enter your email");
            return;
        }

//Len0voX!
        // check if it is a string else throw error
        if (!fName.matches(resRegex)) {
            etResetFirstName.setError(errMsg);
            return;
        } else if (!lName.matches(resRegex)) {
            etResetLastName.setError(errMsg);
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etResetEmail.setError("Invalid email format");
            return;
        }


// check if the name exsist in the db

        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("firstname", fName)
                .whereEqualTo("lastname", lName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            // found
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email);
                        } else {
                            // not found
                            Toast.makeText(this, "User isn't found.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // query error
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }
}

