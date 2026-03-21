package com.example.ridecare.activities.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import com.example.ridecare.R;
import com.example.ridecare.activities.common.LoginActivity;
import com.example.ridecare.activities.common.SettingsActivity;
import com.example.ridecare.activities.service.engineOverhaulActivity;
import com.example.ridecare.activities.service.oilChangeActivity;
import com.example.ridecare.activities.service.towDispatchActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;


public class DashboardActivity extends AppCompatActivity {
    ImageView heroCar, mapPlaceholder, ivProfile;
    MaterialButton btnNewDispatch, btnViewAll;
    // Constraint Layout
    ConstraintLayout serviceRequestCard,invoiceCard;
    TextView myGarageBtn, tvViewMore;
    LinearLayout overHaulcard, oilChangeCard, towingCard;
            /* Developer Note
            * Create a page to view the menuIcon for the client pending
            * Create Page for view more
            * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);

        heroCar = findViewById(R.id.heroCar);
        myGarageBtn = findViewById(R.id.myGarageBtn);
        serviceRequestCard = findViewById(R.id.serviceRequestCard);
        invoiceCard = findViewById(R.id.invoiceCard);

        mapPlaceholder = findViewById(R.id.mapPlaceholder);
        btnNewDispatch = findViewById(R.id.btnNewDispatch);
        btnViewAll = findViewById(R.id.btnViewAll);

        // Image View
        ivProfile = findViewById(R.id.ivProfile);
        onClickListenerFunction();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid())
                .get()
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch user info", Toast.LENGTH_SHORT).show());
                        ivProfile.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, SettingsActivity.class))
        );




    }

    public void onClickListenerFunction(){

        View.OnClickListener openTowDispatch = v -> startActivity(new Intent(DashboardActivity.this, towDispatchActivity.class));
        mapPlaceholder.setOnClickListener(openTowDispatch);
        btnNewDispatch.setOnClickListener(openTowDispatch);

        // vehicle list
        View.OnClickListener openVehicleList = v -> startActivity(new Intent(DashboardActivity.this, VehicleListActivity.class));
        myGarageBtn.setOnClickListener(openVehicleList);

        //Service Request
        View.OnClickListener openServiceRequest = v -> startActivity(new Intent(DashboardActivity.this,ServiceListActivity.class));
        serviceRequestCard.setOnClickListener(openServiceRequest);

        // View profile button
        ivProfile.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, SettingsActivity.class)));

        // invoice list button
        View.OnClickListener openInvoice = v ->startActivity(new Intent(DashboardActivity.this, InvoiceListActivity.class));
        invoiceCard.setOnClickListener(openInvoice);

    }
}
