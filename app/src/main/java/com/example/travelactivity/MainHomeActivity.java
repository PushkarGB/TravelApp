package com.example.travelactivity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import com.bumptech.glide.Glide;
import com.example.travelactivity.Adapters.CategoriesAdapter;
import com.example.travelactivity.Adapters.PopularPlacesAdapter;
import com.example.travelactivity.Common.TableHeaders;
import com.example.travelactivity.Common.Urls;
import com.example.travelactivity.Models.CategoryDetails;
import com.example.travelactivity.Models.CategoryWisePlaces;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainHomeActivity extends AppCompatActivity {


    RecyclerView rvPopularPlaces, rvCategories, rvLatestPlaces;
    TextView tvGreetUser;
    CircleImageView profileImg;
    ImageView ivLogout;
    List<CategoryDetails> listOfCategories;
    List<CategoryWisePlaces> listOfPopularPlaces, listOfLatestPlaces;
    CategoriesAdapter adapterGetAllCategoryDetails;
    PopularPlacesAdapter adapterPopularPlaces, adapterLatestPlaces;


    SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        setTitle("HomePage");

        preferences = getSharedPreferences("SharedData", MODE_PRIVATE);
        rvPopularPlaces = findViewById(R.id.rv_popularPlaces);
        rvCategories = findViewById(R.id.rv_categories);
        rvLatestPlaces = findViewById(R.id.rv_latestPlaces);
        tvGreetUser = findViewById(R.id.greetUserText);
        profileImg = findViewById(R.id.profileImg);
        ivLogout = findViewById(R.id.iv_logout);

        listOfCategories = new ArrayList<>();
        listOfPopularPlaces = new ArrayList<>();
        listOfLatestPlaces = new ArrayList<>();

        rvPopularPlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvLatestPlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        String username = preferences.getString("username", "");
        String profilePic = preferences.getString("profilePic", "user.png");

        tvGreetUser.setText("Hi , " + username);
        Glide.with(this).load(Urls.imagesDirectory + profilePic).into(profileImg);

        profileImg.setOnClickListener(
                v -> {
                    startActivity(new Intent(MainHomeActivity.this, MyProfileActivity.class));
                });

        ivLogout.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Logout?");
            alert.setMessage("Are you sure ?");
            alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", "");
                    editor.putBoolean("isLogin", false);
                    editor.apply();
                    Intent intent = new Intent(MainHomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).create().show();
        });

        fetchCategories();
        fetchPopularPlaces();
        fetchLatestPlaces();

    }

    @Override
    protected void onStart() {
        super.onStart();
        String username = preferences.getString("username", "");
        String profilePic = preferences.getString("profilePic", "user.png");

        tvGreetUser.setText("Hi , " + username);
        Glide.with(this).load(Urls.imagesDirectory + profilePic).into(profileImg);

        fetchCategories();
        fetchPopularPlaces();
        fetchLatestPlaces();
    
    }

    private void fetchCategories() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getAllCategoryDetailsWebService,
                params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            JSONArray jsonArray = response.getJSONArray("getAllCategory");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String strid = jsonObject.getString(TableHeaders.CATEGORIES_id);
                                String strCategoryImage = jsonObject.getString(TableHeaders.CATEGORIES_image);
                                String strCategoryName = jsonObject.getString(TableHeaders.CATEGORIES_name);

                                listOfCategories.add(new CategoryDetails(strid, strCategoryImage, strCategoryName));
                            }
                            adapterGetAllCategoryDetails = new CategoriesAdapter(listOfCategories,
                                    MainHomeActivity.this);

                            rvCategories.setAdapter(adapterGetAllCategoryDetails);


                        } catch (JSONException e) {
                            Toast.makeText(MainHomeActivity.this, "JSON Exception", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("HomeError", "Server Error in fetching Categories : " + throwable);
                        Toast.makeText(MainHomeActivity.this, "Error in fetching Categories", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void fetchPopularPlaces() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getPopularPlaces,
                params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("popularPlaces");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String strid = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_id);
                                String strcategoryname = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_CategoryName);
                                String strplacename = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_PlaceName);
                                String strplaceimage = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_PlaceImage);
                                String strplacerating = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_PlaceRating);
                                String strplacediscription = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_PlaceDescription);

                                listOfPopularPlaces.add(new CategoryWisePlaces(strid, strcategoryname, strplacename,
                                        strplaceimage, strplacerating, strplacediscription));
                            }
                            adapterPopularPlaces = new
                                    PopularPlacesAdapter(listOfPopularPlaces,
                                    MainHomeActivity.this);
                            rvPopularPlaces.setAdapter(adapterPopularPlaces);


                        } catch (JSONException e) {
                            Log.d("HomeError", "Runtime Error in Popular Places : " + e.getMessage());
                            Toast.makeText(MainHomeActivity.this, "Runtime Error in Popular Places ",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("HomeError", "Server Error in fetching Popular Places : " + throwable);
                        Toast.makeText(MainHomeActivity.this, "Server Error in fetching Popular Places",
                                Toast.LENGTH_SHORT).show();
                    }
                }


        );

    }

    //
    private void fetchLatestPlaces() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getLatestPlaces,
                params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("latestPlaces");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String strid = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_id);
                                String strcategoryname = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_CategoryName);
                                String strplacename = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_PlaceName);
                                String strplaceimage = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_PlaceImage);
                                String strplacerating = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_PlaceRating);
                                String strplacediscription = jsonObject.getString(TableHeaders.CATEGORY_WISE_PLACES_PlaceDescription);

                                listOfLatestPlaces.add(new CategoryWisePlaces(strid, strcategoryname, strplacename,
                                        strplaceimage, strplacerating, strplacediscription));
                            }
                            adapterLatestPlaces = new
                                    PopularPlacesAdapter(listOfLatestPlaces,
                                    MainHomeActivity.this);
                            rvLatestPlaces.setAdapter(adapterLatestPlaces);


                        } catch (JSONException e) {
                            Log.d("HomeError", "Runtime Error in Recently Added Places : " + e.getMessage());
                            Toast.makeText(MainHomeActivity.this, "Runtime Error in Recently Added Places ",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("HomeError", "Server Error in fetching Latest Places : " + throwable);
                        Toast.makeText(MainHomeActivity.this, "Server Error in fetching Recently Added Places",
                                Toast.LENGTH_SHORT).show();
                    }
                }


        );



    }
}
