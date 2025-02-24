package com.example.travelactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelactivity.Fragments.HomeFragment;
import com.example.travelactivity.Fragments.TripFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Homepage");

        bottomNavigationView = findViewById(R.id.homeBottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNavigationMenuHome);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
          if (item.getItemId() == R.id.menuHomeMyProfile) {
            Intent intent = new Intent(HomeActivity.this,MyProfileActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.menuHomeSettings)
        {

        } else if (item.getItemId() == R.id.menuHomeContactUs) {


        } else if (item.getItemId() == R.id.menuHomeAboutUs) {


        } else if (item.getItemId() == R.id.menuHomeLogout) {
              SharedPreferences preferences = getSharedPreferences("SharedData",MODE_PRIVATE);
              SharedPreferences.Editor editor = preferences.edit();
              editor.putBoolean("isLogin",false);
              editor.putString("username","");
              editor.apply();
              startActivity(new Intent(HomeActivity.this,LoginActivity.class));
              finish();
        }
        return true;
    }

    HomeFragment homeFragment = new HomeFragment();
    TripFragment tripFragment = new TripFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homeBottomNavigationMenuHome)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,homeFragment).commit();
        }  else if (item.getItemId() == R.id.homeBottomNavigationMenuTrip) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,tripFragment).commit();
        }
        return true;
    }
}




