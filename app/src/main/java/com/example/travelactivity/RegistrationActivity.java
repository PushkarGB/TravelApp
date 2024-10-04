package com.example.travelactivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class RegistrationActivity extends AppCompatActivity {

    EditText etName, etMobileNo, etEmailId,  etUsername, etPassword;
    Button btnRegister;


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

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + etMobileNo.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            RegistrationActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                }
                                //123456
                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull
                                PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent intent = new Intent(RegistrationActivity.this, VerifyOTPActivity.class);
                                    intent.putExtra("verificationcode", verificationCode); //key => string,value
                                    intent.putExtra("name",etName.getText().toString());
                                    intent.putExtra("mobileno",etMobileNo.getText().toString());
                                    intent.putExtra("email",etEmailId.getText().toString());
                                    intent.putExtra("username",etUsername.getText().toString());
                                    intent.putExtra("password",etPassword.getText().toString());
                                    startActivity(intent);
                                }
                            }
                    );
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

        client.post("http://192.168.81.203:80/TravelAPI/userregistertbl.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String status = response.getString("success");
                    if (status.equals("1"))
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,"Registration Success" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,"Already Data Present",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();

                Toast.makeText(RegistrationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}

