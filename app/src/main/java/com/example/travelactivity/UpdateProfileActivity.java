package com.example.travelactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelactivity.Common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText etName, etMobileNo, etEmailId, etUsername;
    AppCompatButton btnUpdateProfile;
    String strName, strMobileNo, strEmailId, strUsername;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

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


        etName.setHint(strName);
        etMobileNo.setText(strMobileNo);
        etMobileNo.setEnabled(false);
        etEmailId.setHint(strEmailId);
        etUsername.setHint(strUsername);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please Enter Your Name");
                } else if (etEmailId.getText().toString().isEmpty()) {
                    etEmailId.setError("Please Enter Your Email ID");
                } else if (!etEmailId.getText().toString().contains("@") || !etEmailId.getText().toString().contains(".com")) {
                    etEmailId.setError("Please Enter Valid Email ID");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please Enter Your Username");
                } else if (etUsername.getText().toString().length() < 8) {
                    etUsername.setError("Username Must Be Greater Than 8 Characters");
                } else {
                    progressDialog = new ProgressDialog(UpdateProfileActivity.this);
                    progressDialog.setTitle("Please Wait..");
                    progressDialog.setMessage("Updating...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();
                    updateProfile();
                }
            }
        });
    }

    private void updateProfile() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name", etName.getText().toString());
        params.put("mobileno", strMobileNo);
        params.put("emailid", etEmailId.getText().toString());
        params.put("username", etUsername.getText().toString());

        client.post(Urls.updateProfileWebService, params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();

                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {

                        Toast.makeText(UpdateProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateProfileActivity.this, MyProfileActivity.class);
                        editor.putString("username", etUsername.getText().toString());
                        editor.apply();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Something went wrong",
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
                Log.d("Error", " " + throwable + " " + errorResponse);
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}