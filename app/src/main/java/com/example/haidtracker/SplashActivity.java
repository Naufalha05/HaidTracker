package com.example.haidtracker;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find views
        ImageView logoImageView = findViewById(R.id.splash_logo);
        TextView titleTextView = findViewById(R.id.splash_title);
        TextView subtitleTextView = findViewById(R.id.splash_subtitle);

        // Load animations
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInAnimation.setDuration(1000);

        // Apply animations
        logoImageView.startAnimation(fadeInAnimation);
        titleTextView.startAnimation(fadeInAnimation);
        subtitleTextView.startAnimation(fadeInAnimation);

        // Navigate to intro screen after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIMEOUT);
    }
}