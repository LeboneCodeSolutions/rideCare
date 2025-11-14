package com.example.ridecare.activities.mechanic;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ridecare.R;
import com.example.ridecare.models.ServiceRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MechanicJobDetailsActivity extends AppCompatActivity {

    TextView tvService, tvVehicle, tvDescription, tvStatus;
    Button btnUploadImage, btnComplete;
    ImageView proofImage;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri imageUri = null;
    ServiceRequest request;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    proofImage.setImageURI(imageUri);
                }
            }
    );

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_job_details);

        tvService = findViewById(R.id.tvServiceType);
        tvVehicle = findViewById(R.id.tvVehicle);
        tvDescription = findViewById(R.id.tvDescription);
        tvStatus = findViewById(R.id.tvStatus);
        proofImage = findViewById(R.id.ivProof);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnComplete = findViewById(R.id.btnMarkComplete);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            request = getIntent().getSerializableExtra("job_data", ServiceRequest.class);
        } else {
            request = (ServiceRequest) getIntent().getSerializableExtra("job_data");
        }

        if (request == null) {
            Toast.makeText(this, "Job data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvService.setText("Service: " + request.getServiceType());
        tvVehicle.setText("Vehicle: " + request.getVehicleId());
        tvDescription.setText("Problem: " + request.getDescription());
        tvStatus.setText("Status: " + request.getStatus());

        btnUploadImage.setOnClickListener(v -> pickImage());
        btnComplete.setOnClickListener(v -> completeJob());
    }

    private void pickImage() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        imagePickerLauncher.launch(i);
    }

    private void completeJob() {
        if (imageUri == null) {
            Toast.makeText(this, "Upload photo first!", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference ref = storage.getReference()
                .child("jobProofs/" + request.getId() + ".jpg");

        ref.putFile(imageUri).addOnSuccessListener(task -> {
            ref.getDownloadUrl().addOnSuccessListener(url -> {
                db.collection("serviceRequests").document(request.getId())
                        .update("status", "completed", "proofUrl", url.toString())
                        .addOnSuccessListener(unused ->
                                Toast.makeText(this, "Job Completed ✅", Toast.LENGTH_SHORT).show()
                        );
            });
        });
    }
}
