package com.example.ridecare.activities.mechanic;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ridecare.R;
import com.example.ridecare.activities.common.LoginActivity;
import com.example.ridecare.activities.common.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MechanicDashboardActivity extends AppCompatActivity {

    private Button btnViewJobsBoard, btnViewCurrentJobs, btnLogout, btnCreateInvoice;
    private TextView tvWelcome;
    private ImageView editProfile;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dashboard);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Match all IDs to XML
        btnViewJobsBoard = findViewById(R.id.btnViewJobsBoard);
        btnViewCurrentJobs = findViewById(R.id.btnViewCurrentJobs);
        btnLogout = findViewById(R.id.btnLogout);
        btnCreateInvoice = findViewById(R.id.btnCreateInvoice);
        tvWelcome = findViewById(R.id.tvWelcome);
        editProfile = findViewById(R.id.editProfile);

        // Button listeners
        btnViewJobsBoard.setOnClickListener(v ->
                startActivity(new Intent(this, JobBoardActivity.class)));

        btnViewCurrentJobs.setOnClickListener(v ->
                startActivity(new Intent(this, ActiveJobsActivity.class)));

        btnCreateInvoice.setOnClickListener(v ->
                startActivity(new Intent(this, InvoiceCreateActivity.class)));

        editProfile.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Load name after view initialization
        loadMechanicName();
    }

    private void loadMechanicName() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "No user logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // NOTE: match the keys you store during registration ("firstname", "lastname")
                        String firstName = documentSnapshot.getString("firstname");
                        String lastName = documentSnapshot.getString("lastname");

                        if (firstName != null && !firstName.isEmpty() &&
                                lastName != null && !lastName.isEmpty()) {

                            tvWelcome.setText("Welcome, " + firstName + " " + lastName + "!");
                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            prefs.edit()
                                    .putString("firstname", firstName)
                                    .putString("lastname", lastName)
                                    .apply();
                        } else if (firstName != null && !firstName.isEmpty()) {
                            tvWelcome.setText("Welcome, " + firstName + "!");
                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            prefs.edit()
                                    .putString("firstname", firstName)
                                    .apply();
                        } else {
                            tvWelcome.setText("Welcome, Mechanic!");
                        }
                    } else {
                        tvWelcome.setText("Welcome, Mechanic!");
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching name: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
