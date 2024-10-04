package com.example.travelactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmRegisterMobilenoActivity extends AppCompatActivity {
    EditText etConfirmRegisterMobileno;
    AppCompatButton btnVerify;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_register_mobileno);

        etConfirmRegisterMobileno = findViewById(R.id.etConfirmRegisterMobilenoMobileno);
        btnVerify = findViewById(R.id.acbtnConfirmRegisterMobilenoVerify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etConfirmRegisterMobileno.getText().toString().isEmpty())
                {
                    etConfirmRegisterMobileno.setError("Please Enter Mobile No");
                } else if (etConfirmRegisterMobileno.getText().toString().length() != 10) {
                    etConfirmRegisterMobileno.setError("Please Enter Valid Mobile Number");
                }
                else
                {
                    progressDialog = new ProgressDialog(ConfirmRegisterMobilenoActivity.this);
                    progressDialog.setTitle("Sending OTP");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + etConfirmRegisterMobileno.getText().toString(),
                            60, TimeUnit.SECONDS, ConfirmRegisterMobilenoActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmRegisterMobilenoActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmRegisterMobilenoActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull
                                PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent intent = new Intent(ConfirmRegisterMobilenoActivity.this, ForgetPasswordVerifyOTPActivity.class);
                                    intent.putExtra("verificationCode",verificationCode);
                                    intent.putExtra("mobileno",etConfirmRegisterMobileno.getText().toString());
                                    startActivity(intent);

                                }
                            }
                    );
                }
            }
        });
    }
}