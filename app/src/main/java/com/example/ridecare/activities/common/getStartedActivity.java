package com.example.ridecare.activities.common;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import com.example.ridecare.R;
import com.example.ridecare.activities.common.LoginActivity;

public class getStartedActivity extends AppCompatActivity {

    Button getStartedBtn;
    TextView createAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_page);

        getStartedBtn = findViewById(R.id.getStartedBtn);
        createAccountText = findViewById(R.id.createAccountTextView);

        getStartedBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getStartedActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        createAccountText.setOnClickListener(v -> {
            Intent intent = new Intent( getStartedActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}