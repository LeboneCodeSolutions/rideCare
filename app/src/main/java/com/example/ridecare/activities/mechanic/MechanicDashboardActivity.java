package com.example.ridecare.activities.mechanic;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.ridecare.R;
import com.example.ridecare.activities.common.LoginActivity;
import com.example.ridecare.activities.common.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MechanicDashboardActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ImageView logOutIcon , homeBtn;
    private TextView tvFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dashboard);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        tvFullName = findViewById(R.id.tvFullName);
        homeBtn = findViewById(R.id.homeBtn);
        logOutIcon = findViewById(R.id. logOutIcon);

        // Constraint Layout Listener
        ConstraintLayout jobBoard = findViewById(R.id.jobBoard);

        // DEVELOPER NOTE: create a function for the job card update
       //  ConstraintLayout jobCardUpdate = findViewById(R.id.jobCardUpdate);

        ConstraintLayout invoiceGenerate = findViewById(R.id.invoiceGenerate);
        ConstraintLayout editProfile = findViewById(R.id.editProfile);
        ConstraintLayout CurrentjobPreview = findViewById(R.id.CurrentjobPreview);


        // OnClickListener
        jobBoard.setOnClickListener(v ->
                startActivity(new Intent(this, JobBoardActivity.class)));

        invoiceGenerate.setOnClickListener(v ->
                startActivity(new Intent(this, InvoiceCreateActivity.class)));

        editProfile.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        CurrentjobPreview.setOnClickListener(v ->
                startActivity(new Intent(this, ActiveJobsActivity.class)));

        homeBtn.setOnClickListener(v->{
            startActivity(new Intent(this , MechanicDashboardActivity.class));
        });

        logOutIcon.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        loadMechanicName();
    }
    private void loadMechanicName() {

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Retrieves userID from database
        String uid = auth.getCurrentUser().getUid();


        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstname = documentSnapshot.getString("firstname");
                        String lastname = documentSnapshot.getString("lastname");

                        if (firstname != null && !firstname.isEmpty() &&
                                lastname != null && !lastname.isEmpty()) {
                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            prefs.edit()
                                    .putString("firstname", firstname)
                                    .putString("lastname", lastname)
                                    .apply();
                            tvFullName.setText(firstname + " " + lastname);
                        } else {
                            tvFullName.setText(" ");
                        }
                    } else {
                        tvFullName.setText(" ");
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching name: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
