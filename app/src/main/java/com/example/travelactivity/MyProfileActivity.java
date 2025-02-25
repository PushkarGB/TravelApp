package com.example.travelactivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.travelactivity.Common.TableHeaders;
import com.example.travelactivity.Common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    // Declare your variables here
    CircleImageView ivProfilePhoto;
    TextView tvName, tvMobile, tvEmailId, tvUsername;
    AppCompatButton btnEditProfilePic, btnUpdateProfile, btnSignOut;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String strUsername;
    ProgressDialog progressDialog;

    Uri imageURI;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Initialize your variables
        preferences = getSharedPreferences("SharedData", MODE_PRIVATE);
        strUsername = preferences.getString("username", "");
        ivProfilePhoto = findViewById(R.id.ivMyProfileProfilePhoto);
        btnEditProfilePic = findViewById(R.id.updateProfilePictureBtn);
        tvMobile = findViewById(R.id.tvMyProfileMobileNo);
        tvName = findViewById(R.id.tvMyProfileName);
        tvEmailId = findViewById(R.id.tvMyProfileEmailID);
        tvUsername = findViewById(R.id.tvMyProfileUsername);
        btnUpdateProfile = findViewById(R.id.btnupdate);
        btnSignOut = findViewById(R.id.btnSignOut);


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyProfileActivity.this);
                alert.setTitle("Sign out?");
                alert.setMessage("Are you sure ?");
                alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setNegativeButton("Sign out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", "");
                        editor.putBoolean("isLogin", false);
                        editor.apply();
                        Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
            }
        });

        btnEditProfilePic.setOnClickListener(
                v -> {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 110);
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        strUsername = preferences.getString("username", "");
        progressDialog = new ProgressDialog(MyProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getMyDetails();
    }

    private void getMyDetails() {
        AsyncHttpClient client = new AsyncHttpClient(); // client-server communication over network data pass
        RequestParams params = new RequestParams(); // parameters for php script

        params.put("username", strUsername);
        client.post(Urls.myDetailsWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("MyProfileActivity", response.toString());
                progressDialog.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetails");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String strname = jsonObject.getString(TableHeaders.USER_DATA_name);
                        String strimage = jsonObject.getString(TableHeaders.USER_DATA_image);
                        String strmobileno = jsonObject.getString(TableHeaders.USER_DATA_phone);
                        String stremailId = jsonObject.getString(TableHeaders.USER_DATA_email);
                        String strusername = jsonObject.getString(TableHeaders.USER_DATA_username);

                        tvName.setText(strname);
                        tvMobile.setText(strmobileno);
                        tvEmailId.setText(stremailId);
                        tvUsername.setText(strusername);

                        Glide.with(MyProfileActivity.this)
                                .load(Urls.imagesDirectory + strimage)
                                .skipMemoryCache(true)
                                .error(R.drawable.image_not_found)
                                .into(ivProfilePhoto);

                        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(MyProfileActivity.this, UpdateProfileActivity.class);

                                i.putExtra("name", strname);
                                i.putExtra("mobileno", strmobileno);
                                i.putExtra("emailid", stremailId);
                                i.putExtra("username", strusername);

                                startActivity(i);
                            }
                        });
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(MyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110) {
            if (data != null) {
                imageURI = data.getData();
                updateProfilePic();
            }
        }
    }

    private void updateProfilePic() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        ProgressDialog progressDialog2 = new ProgressDialog(MyProfileActivity.this);
        progressDialog2.setTitle("Updating Pic");
        progressDialog2.setMessage("Please wait...");
        progressDialog2.setCanceledOnTouchOutside(false);
        progressDialog2.show();

        params.put("username", strUsername);
        try {
            if (imageURI != null) {
                String imagePath = getRealPathFromURI(imageURI);
                params.put("profilePic", new java.io.File(imagePath));  // Uploads the actual file
            }
        } catch (Exception e) {
            params.put("profilePic", "profile.jpg");
            Log.e("UploadError", "Error uploading image: " + e.getMessage());
        }

        client.post(Urls.updateProfilePic, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {
                        progressDialog.dismiss();
                        progressDialog2.dismiss();
                        editor = preferences.edit();
                        editor.putString("profilePic",response.getString("image"));
                        editor.apply();
                        Toast.makeText(MyProfileActivity.this, "Profile Pic Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyProfileActivity.this, MyProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        progressDialog2.dismiss();
                        Toast.makeText(MyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    progressDialog2.dismiss();
                    Toast.makeText(MyProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                progressDialog2.dismiss();
                Toast.makeText(MyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
