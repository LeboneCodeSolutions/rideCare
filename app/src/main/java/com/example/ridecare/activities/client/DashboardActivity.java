package com.example.ridecare.activities.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import com.example.ridecare.R;
import com.example.ridecare.activities.common.LoginActivity;
import com.example.ridecare.activities.common.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;


public class DashboardActivity extends AppCompatActivity {


    ImageView ivProfile, menuIcon, heroCar, ivServiceRequest, invoiceImage;
    TextView myGarageBtn;


            /* Developer Note
            * Create a page to view the menuIcon for the client pending
            * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);


        heroCar = findViewById(R.id.heroCar);
        myGarageBtn = findViewById(R.id.myGarageBtn);

        // LinearLayout
        LinearLayout overHaulCard;
        LinearLayout oilChangeCard;
        LinearLayout towingCard;

        overHaulCard = findViewById(R.id.overHaulCard);
        oilChangeCard = findViewById(R.id.towingCard);
        towingCard = findViewById(R.id.towingCard);


        // Constraint Layout
        ConstraintLayout serviceRequestCard;
        ConstraintLayout invoiceCard;
        serviceRequestCard = findViewById(R.id.serviceRequestCard);
        invoiceCard = findViewById(R.id.invoiceCard);


        ImageView ivProfile = findViewById(R.id.ivProfile);

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



        // vehicle list
        View.OnClickListener openVehicleList = v -> startActivity(new Intent(DashboardActivity.this, VehicleListActivity.class));
        heroCar.setOnClickListener(openVehicleList);
        myGarageBtn.setOnClickListener(openVehicleList);

        //Service Request
        View.OnClickListener openServiceRequest = v -> startActivity(new Intent(DashboardActivity.this,ServiceListActivity.class));
        ivServiceRequest.setOnClickListener(openServiceRequest);
        serviceRequestCard.setOnClickListener(openServiceRequest);


       /*
        need to check if it works without this
        // View profile button
        ivProfile.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, SettingsActivity.class)));
        */


        // invoice list button
        View.OnClickListener openInvoice = v ->startActivity(new Intent(DashboardActivity.this, InvoiceListActivity.class));
        invoiceImage.setOnClickListener(openInvoice);
        invoiceCard.setOnClickListener(openInvoice);

        /* code will be used in another sections
        // Logout button
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });
         */
    }
}
