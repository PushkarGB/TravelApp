package com.example.travelactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.travelactivity.BroadCastReceivers.NetworkChangeListener;
import com.example.travelactivity.Common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    CheckBox cbShowHidePassword;
    Button btnLogin;
    TextView tvloginNewuser, tvForgetPassword;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    AppCompatButton btnSignInWithGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        preferences = getSharedPreferences("SharedData",MODE_PRIVATE);
        editor = preferences.edit();

        // Initialize UI elements
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        cbShowHidePassword = findViewById(R.id.cbloginShowHidePassword);
        btnLogin = findViewById(R.id.loginButton);
        tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);
        tvloginNewuser = findViewById(R.id.tvloginNewuser);
        btnSignInWithGoogle = findViewById(R.id.acbtnLoginInWithGoogle);

        // Configure Google Sign-In options
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);

        // Set up click listeners
        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please Enter Your Username");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please Enter Your Password");
                } else if (etUsername.getText().toString().length() < 8) {
                    etUsername.setError("Please Enter At least 8 Characters");
                } else if (etPassword.getText().toString().length() < 8) {
                    etPassword.setError("Please Enter At least 8 Characters");
                } else {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Please Wait..");
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();
                    userLogin();
                }
            }
        });


        tvloginNewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        cbShowHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void SignIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
            } catch (ApiException e) {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    private void userLogin() {
        AsyncHttpClient client = new AsyncHttpClient(); //client-server communication
        RequestParams params = new RequestParams(); //data put

        params.put("username", etUsername.getText().toString());
        params.put("password", etPassword.getText().toString());

        client.post(Urls.loginUserWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();

                try {
                    String status = response.getString("success");
                    String username = response.getString("username");
                    String role = response.getString("userrole");
                    String profilePic = response.getString("profilePic");
                    if (status.equals("1")) {
                        editor.putString("username", username);
                        editor.putBoolean("isLogin", true); // Set isLogin to true
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Log.d("LoginError"," " + throwable + " " + errorResponse);
                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}