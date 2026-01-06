package com.example.ridecare.activities.common;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.ridecare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SettingsActivity extends AppCompatActivity {
    TextView tvFullNameDisplay,tvEmailDisplay;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = auth.getUid();

        tvFullNameDisplay = findViewById(R.id.tvFullNameDisplay);
        tvEmailDisplay = findViewById(R.id.tvEmailDisplay);

        ConstraintLayout resetPasswordConstraint = findViewById(R.id.resetPasswordConstraint);
        ConstraintLayout editProfileConstraint = findViewById(R.id.editProfileConstraint);
        ConstraintLayout LogOutConstraint = findViewById(R.id.LogOutConstraint);


resetPasswordConstraint.setOnClickListener(V->{
    startActivity(new Intent(this, ForgotPasswordActivity.class));
});
editProfileConstraint.setOnClickListener(V->{
    startActivity(new Intent(this,EditProfileActivity.class));
});
        LogOutConstraint.setOnClickListener(V->{
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        loadUserData();

    }
    private void loadUserData() {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String firstName = doc.getString("firstname");
                        String lastName = doc.getString("lastname");

                        tvFullNameDisplay.setText(firstName + " " + lastName);
                        tvEmailDisplay.setText(doc.getString("email"));
                    }
                });
    }
}



//Developer Notes :
//Create a in app password reset page

    /* private void updatePassword() {
        assert auth.getCurrentUser() != null;
        auth.sendPasswordResetEmail(Objects.requireNonNull(auth.getCurrentUser().getEmail()))
                .addOnSuccessListener(x ->
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
*/