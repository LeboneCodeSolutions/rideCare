package com.example.ridecare.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridecare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    EditText etProfileFirstName, etProfileLastName, etProfilePhoneNumber;
    TextView tvEmailAddress;
    Button btnSave;

    // Firebase
    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = auth.getUid();

        tvEmailAddress = findViewById(R.id.tvEmailAddress);
        etProfileFirstName = findViewById(R.id.etProfileFirstName);
        etProfileLastName = findViewById(R.id.etProfileLastName);
        etProfilePhoneNumber = findViewById(R.id.etProfilePhoneNumber);
        btnSave = findViewById(R.id.btnSave);

        loadUserData();

        btnSave.setOnClickListener(v -> {
            updateProfile();
            startActivity(new Intent(EditProfileActivity.this, SettingsActivity.class));
        });

    }

    private void loadUserData() {
        if (uid == null) return;

        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        etProfileFirstName.setText(doc.getString("firstname"));
                        etProfileLastName.setText(doc.getString("lastname"));
                        etProfilePhoneNumber.setText(doc.getString("phone"));
                        tvEmailAddress.setText(doc.getString("email"));
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
                );
    }

    private void updateProfile() {
        if (uid == null) return;

        String newFname = etProfileFirstName.getText().toString().trim();
        String newLname = etProfileLastName.getText().toString().trim();
        String newPhone = etProfilePhoneNumber.getText().toString().trim();
        String newEmail = tvEmailAddress.getText().toString().trim();

        if (newFname.isEmpty() || newLname.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("firstname", newFname);
        map.put("lastname", newLname);
        map.put("phone", newPhone);
        map.put("email", newEmail);

        db.collection("users").document(uid)
                .update(map)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );

        // Update Firebase Auth email (requires recent login)
        if (auth.getCurrentUser() != null &&
                !newEmail.equals(auth.getCurrentUser().getEmail())) {

            auth.getCurrentUser()
                    .verifyBeforeUpdateEmail(newEmail)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(
                                    this,
                                    "Verification email sent. Please confirm.",
                                    Toast.LENGTH_LONG
                            ).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    this,
                                    "Email update failed: " + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show()
                    );
        }
    }
}
