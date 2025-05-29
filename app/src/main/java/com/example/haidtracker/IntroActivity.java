package com.example.haidtracker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import Page.SignInActivity;

public class IntroActivity extends AppCompatActivity {

    private ViewPager2 introViewPager;
    private TabLayout tabIndicator;
    private Button nextButton;
    private TextView skipTextView;
    private IntroSlideAdapter introSlideAdapter;
    private List<IntroSlide> introSlides;
    private int currentPosition = 0;
    private Handler autoSlideHandler;
    private static final long AUTO_SLIDE_DELAY = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Find views
        introViewPager = findViewById(R.id.intro_viewpager);
        tabIndicator = findViewById(R.id.tab_indicator);
        nextButton = findViewById(R.id.btn_next);
        skipTextView = findViewById(R.id.tv_skip);

        // Create intro slides
        setupIntroSlides();

        // Setup adapter
        introSlideAdapter = new IntroSlideAdapter(this, introSlides);
        introViewPager.setAdapter(introSlideAdapter);

        // Setup ViewPager2 transformation
        setupViewPagerTransformation();

        // Setup tab indicator with animation
        setupTabIndicator();

        // Setup button animations
        setupButtonAnimations();

        // Handle next button clicks
        nextButton.setOnClickListener(v -> {
            animateButtonClick(nextButton);
            if (currentPosition < introSlides.size() - 1) {
                currentPosition++;
                introViewPager.setCurrentItem(currentPosition, true);
            } else {
                navigateToMainActivity();
            }
        });

        // Handle skip button clicks
        skipTextView.setOnClickListener(v -> {
            animateTextClick(skipTextView);
            navigateToMainActivity();
        });

        // Update button text for last slide with animation
        introViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;

                animateButtonTextChange(position);

                // Reset auto-slide timer
                resetAutoSlideTimer();
            }
        });

        // Setup auto-slide handler
        autoSlideHandler = new Handler(Looper.getMainLooper());
        startAutoSlideTimer();

        // Initial entrance animation
        playEntranceAnimation();
    }

    private void setupIntroSlides() {
        introSlides = new ArrayList<>();

        // Slide 1
        introSlides.add(new IntroSlide(
                R.drawable.g1,
                "Lacak Siklus Menstruasi Anda",
                "Pantau siklus menstruasi Anda dengan mudah dan prediksi kapan periode Anda selanjutnya akan tiba"
        ));

        // Slide 2
        introSlides.add(new IntroSlide(
                R.drawable.g2,
                "Catat Gejala Anda",
                "Catat gejala fisik dan emosional untuk memahami pola tubuh Anda selama siklus menstruasi"
        ));

        // Slide 3
        introSlides.add(new IntroSlide(
                R.drawable.g3,
                "Dapatkan Wawasan",
                "Dapatkan analisis tentang pola menstruasi Anda dan saran kesehatan berdasarkan data Anda"
        ));

        // Slide 4
        introSlides.add(new IntroSlide(
                R.drawable.g4,
                "Privasi Terjamin",
                "Data Anda aman dan terlindungi. Kami menghargai privasi Anda dan tidak membagikan informasi Anda"
        ));
    }

    private void setupViewPagerTransformation() {
        introViewPager.setPageTransformer((page, position) -> {
            if (position < -1) {
                // Page is way off-screen to the left
                page.setAlpha(0f);
            } else if (position <= 1) {
                // Page is visible or sliding
                page.setAlpha(Math.max(0.5f, 1 - Math.abs(position)));

                // Scale effect
                float scaleFactor = Math.max(0.85f, 1 - Math.abs(position) * 0.15f);
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);

                // Slight rotation effect
                page.setRotationY(position * -30);
            } else {
                // Page is way off-screen to the right
                page.setAlpha(0f);
            }
        });
    }

    private void setupTabIndicator() {
        new TabLayoutMediator(tabIndicator, introViewPager, (tab, position) -> {
            // No text needed for tab indicators
        }).attach();

        // Add animation to tab selection
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                animateTabSelection(tab.view, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                animateTabSelection(tab.view, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });
    }

    private void setupButtonAnimations() {
        // Add subtle breathing animation to button
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(nextButton, "scaleX", 1f, 1.05f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(nextButton, "scaleY", 1f, 1.05f, 1f);

        // Set repeat properties on individual animators
        scaleX.setDuration(2000);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.RESTART);

        scaleY.setDuration(2000);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.RESTART);

        // Start animations
        scaleX.start();
        scaleY.start();
    }

    private void animateButtonClick(Button button) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.9f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 0.9f);
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(button, "scaleX", 0.9f, 1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(button, "scaleY", 0.9f, 1f);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(scaleDownX, scaleDownY);
        scaleDown.setDuration(100);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(scaleUpX, scaleUpY);
        scaleUp.setDuration(100);

        AnimatorSet clickAnimation = new AnimatorSet();
        clickAnimation.playSequentially(scaleDown, scaleUp);
        clickAnimation.start();
    }

    private void animateTextClick(TextView textView) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0.5f, 1f);
        alpha.setDuration(200);
        alpha.start();
    }

    private void animateButtonTextChange(int position) {
        String newText = (position == introSlides.size() - 1) ? "Mulai" : "Selanjutnya";

        if (!nextButton.getText().toString().equals(newText)) {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(nextButton, "alpha", 1f, 0f);
            fadeOut.setDuration(150);

            fadeOut.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    nextButton.setText(newText);
                    ObjectAnimator fadeIn = ObjectAnimator.ofFloat(nextButton, "alpha", 0f, 1f);
                    fadeIn.setDuration(150);
                    fadeIn.start();
                }
            });

            fadeOut.start();
        }
    }

    private void animateTabSelection(View tabView, boolean selected) {
        if (tabView == null) return;

        float targetScale = selected ? 1.2f : 1f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tabView, "scaleX", targetScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tabView, "scaleY", targetScale);

        AnimatorSet scaleAnimation = new AnimatorSet();
        scaleAnimation.playTogether(scaleX, scaleY);
        scaleAnimation.setDuration(200);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.start();
    }

    private void playEntranceAnimation() {
        // Initially hide all views
        tabIndicator.setAlpha(0f);
        nextButton.setAlpha(0f);
        skipTextView.setAlpha(0f);

        tabIndicator.setTranslationY(50f);
        nextButton.setTranslationY(100f);
        skipTextView.setTranslationY(100f);

        // Animate entrance
        new Handler().postDelayed(() -> {
            // Tab indicator animation
            ObjectAnimator tabAlpha = ObjectAnimator.ofFloat(tabIndicator, "alpha", 0f, 1f);
            ObjectAnimator tabTranslation = ObjectAnimator.ofFloat(tabIndicator, "translationY", 50f, 0f);
            AnimatorSet tabSet = new AnimatorSet();
            tabSet.playTogether(tabAlpha, tabTranslation);
            tabSet.setDuration(500);
            tabSet.setInterpolator(new AccelerateDecelerateInterpolator());

            // Button animation
            ObjectAnimator buttonAlpha = ObjectAnimator.ofFloat(nextButton, "alpha", 0f, 1f);
            ObjectAnimator buttonTranslation = ObjectAnimator.ofFloat(nextButton, "translationY", 100f, 0f);
            AnimatorSet buttonSet = new AnimatorSet();
            buttonSet.playTogether(buttonAlpha, buttonTranslation);
            buttonSet.setDuration(500);
            buttonSet.setInterpolator(new AccelerateDecelerateInterpolator());
            buttonSet.setStartDelay(100);

            // Skip text animation
            ObjectAnimator skipAlpha = ObjectAnimator.ofFloat(skipTextView, "alpha", 0f, 1f);
            ObjectAnimator skipTranslation = ObjectAnimator.ofFloat(skipTextView, "translationY", 100f, 0f);
            AnimatorSet skipSet = new AnimatorSet();
            skipSet.playTogether(skipAlpha, skipTranslation);
            skipSet.setDuration(500);
            skipSet.setInterpolator(new AccelerateDecelerateInterpolator());
            skipSet.setStartDelay(200);

            // Start all animations
            tabSet.start();
            buttonSet.start();
            skipSet.start();
        }, 300);
    }

    private void startAutoSlideTimer() {
        autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY);
    }

    private void resetAutoSlideTimer() {
        autoSlideHandler.removeCallbacks(autoSlideRunnable);
        autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY);
    }

    private final Runnable autoSlideRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentPosition < introSlides.size() - 1) {
                currentPosition++;
                introViewPager.setCurrentItem(currentPosition, true);
            } else {
                // If on the last slide, navigate to main activity
                navigateToMainActivity();
            }
        }
    };

    private void navigateToMainActivity() {
        // Save preference that intro has been shown
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("intro_shown", true);
        editor.apply();

        // Add exit animation
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(findViewById(R.id.bottom_controls), "alpha", 1f, 0f);
        fadeOut.setDuration(300);
        fadeOut.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                // Navigate to main activity
                Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        fadeOut.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leaks
        if (autoSlideHandler != null) {
            autoSlideHandler.removeCallbacks(autoSlideRunnable);
        }
    }
}