package com.example.travelactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


        }
        return true;
    }

    HomeFragment homeFragment = new HomeFragment();
   // SearchFragment searchFragment = new SearchFragment();
    ExploreFragment exploreFragment = new ExploreFragment();
    TripFragment tripFragment = new TripFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homeBottomNavigationMenuHome)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,homeFragment).commit();
        } //else if (item.getItemId() == R.id.homeBottomNavigationMenuSearch) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,searchFragment).commit();}
         else if (item.getItemId() == R.id.homeBottomNavigationMenuExplore) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,exploreFragment).commit();
        }  else if (item.getItemId() == R.id.homeBottomNavigationMenuTrip) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,tripFragment).commit();
        }  else if (item.getItemId() == R.id.homeBottomNavigationMenuProfile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,profileFragment).commit();
        }
        return true;
    }
}




