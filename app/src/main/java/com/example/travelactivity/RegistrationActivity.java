package com.example.travelactivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelactivity.Common.Urls;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    EditText etName, etMobileNo, etEmailId, etUsername, etPassword;
    Button btnRegister;
    CircleImageView profileImage;

    //Image Uri and string
    Uri imageURI;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        setTitle("Registration Activity");
        etName = findViewById(R.id.etRegistrationName);
        etMobileNo = findViewById(R.id.etRegistrationMobileNo);
        etEmailId = findViewById(R.id.etRegistrationEmailId);
        etUsername = findViewById(R.id.etRegistrationUsername);
        etPassword = findViewById(R.id.etRegistrationPassword);
        btnRegister = findViewById(R.id.btnRegisterRegister);
        profileImage = findViewById(R.id.civRegistrationImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please Enter Your Name");
                } else if (etMobileNo.getText().toString().isEmpty()) {
                    etMobileNo.setError("Please Enter Your Mobile Number");
                } else if (etMobileNo.getText().toString().length() != 10) {
                    etMobileNo.setError("Please Enter 10 Digit Mobile Number");
                } else if (etEmailId.getText().toString().isEmpty()) {
                    etEmailId.setError("Please Enter Your Email ID");
                } else if (!etEmailId.getText().toString().contains("@") || !etEmailId.getText().toString().contains(".com")) {
                    etEmailId.setError("Please Enter Valid Email ID");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please Enter Your Username");
                } else if (etUsername.getText().toString().length() < 8) {
                    etUsername.setError("Username Must Be Greater Than 8 Characters");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please Enter Your Password");
                } else if (etPassword.getText().toString().length() < 8) {
                    etPassword.setError("Password Must Be Greater Than 8 Characters");
                } else if (!etPassword.getText().toString().matches(".*[A-Z].*")) {
                    etPassword.setError("Password Must Contain at Least 1 Uppercase Letter");
                } else if (!etPassword.getText().toString().matches(".*[a-z].*")) {
                    etPassword.setError("Password Must Contain at Least 1 Lowercase Letter");
                } else if (!etPassword.getText().toString().matches(".*[0-9].*")) {
                    etPassword.setError("Password Must Contain at Least 1 Number");
                } else if (!etPassword.getText().toString().matches(".*[@,#,$,&,%].*")) {
                    etPassword.setError("Password Must Contain at Least 1 Special Symbol");
                } else {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Registration is in process");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    userRegistertbl();

                }
            }
        });
    }

    private void userRegistertbl() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", etName.getText().toString());
        params.put("mobile_no", etMobileNo.getText().toString());
        params.put("email_id", etEmailId.getText().toString());
        params.put("username", etUsername.getText().toString());
        params.put("password", etPassword.getText().toString());
        if (imageURI != null) {
            params.put("profilePic", imageURI.toString());
        } else {
            params.put("profilePic", "profile.jpg");
        }
        params.put("latitude", 23);
        params.put("longitude", 23);
        params.put("address", "demo address");

        client.post(Urls.registerUserWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Already Data Present", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Log.d("RegisterError"," "+throwable + " " +  errorResponse);
                Toast.makeText(RegistrationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (data != null) {
                imageURI = data.getData();
                profileImage.setImageURI(imageURI);
            }
        }
    }
}

