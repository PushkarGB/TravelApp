package com.example.travelactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

   ImageView ivLogo;
   TextView tvTitle;

   Handler handler;
   Animation animtraslate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ivLogo = findViewById(R.id.ivSplashLogo);
        tvTitle = findViewById(R.id.tvsplashTitle);

        animtraslate = AnimationUtils.loadAnimation(MainActivity.this,R.anim.toptobottomtranslate);
        ivLogo.startAnimation(animtraslate);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        },4000);
    }
}
