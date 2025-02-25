package com.example.travelactivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.travelactivity.Common.Urls;

import java.util.Objects;

public class PlaceDetails extends AppCompatActivity {

    TextView tvPlaceName, tvPlaceDescription, tvPlaceRating;
    ImageView ivPlaceImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        tvPlaceDescription = findViewById(R.id.tvPlaceDescription);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvPlaceRating = findViewById(R.id.tvPlaceRating);
        ivPlaceImage = findViewById(R.id.ivPlaceImage);

        String placeName = getIntent().getStringExtra("placeName");
        String placeDescription = getIntent().getStringExtra("placeDescription");
        String placeImage = getIntent().getStringExtra("placeImage");
        String placeRating = getIntent().getStringExtra("placeRating");

        tvPlaceRating.setText(placeRating);
        tvPlaceName.setText(placeName);
        tvPlaceDescription.setText(placeDescription);

        Glide.with(this).load(Urls.imagesDirectory + placeImage).into(ivPlaceImage);

    }
}