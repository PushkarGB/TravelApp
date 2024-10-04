package com.example.travelactivity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText etName,etMobileNo,etEmailId,etUsername;
    AppCompatButton btnUpdateProfile;
    String strName,strMobileNo,strEmailId,strUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        etName = findViewById(R.id.etUpdateName);
        etMobileNo = findViewById(R.id.etUpdateMobileNo);
        etEmailId = findViewById(R.id.etUpdateEmailId);
        etUsername = findViewById(R.id.etUpdateUsername);

        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
         strName = getIntent().getStringExtra("name");
         strMobileNo = getIntent().getStringExtra("mobileno");
         strEmailId = getIntent().getStringExtra("emailid");
         strUsername = getIntent().getStringExtra("username");



    }
}