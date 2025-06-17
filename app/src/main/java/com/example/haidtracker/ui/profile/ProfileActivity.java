package com.example.haidtracker.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.user.User;
import com.example.haidtracker.ui.auth.SignInActivity;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ProfileViewBinder viewBinder;
    private ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize view binder and controller
        viewBinder = new ProfileViewBinder(this);
        profileController = new ProfileController(this);

        setupClickListeners();
        loadUserProfile();
    }

    private void setupClickListeners() {
        viewBinder.setOnBackClickListener(v -> finish());

        viewBinder.setOnEditProfileClickListener(v -> {
            // TODO: Implement edit profile functionality
            Toast.makeText(this, "Edit profile feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        viewBinder.setOnLogoutClickListener(v -> performLogout());
    }

    private void loadUserProfile() {
        if (!profileController.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        viewBinder.showLoading(true);

        profileController.loadUserProfile(new ProfileController.ProfileCallback() {
            @Override
            public void onSuccess(User user) {
                viewBinder.showLoading(false);
                viewBinder.displayUserInfo(user);
                Log.d(TAG, "User profile loaded successfully");
            }

            @Override
            public void onError(String error) {
                viewBinder.showLoading(false);
                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to load profile: " + error);
                
                // If authentication error, redirect to login
                if (error.contains("401") || error.contains("authentication")) {
                    redirectToLogin();
                }
            }
        });
    }

    private void performLogout() {
        profileController.logout();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // Check if user is still logged in
        if (!profileController.isLoggedIn()) {
            redirectToLogin();
        }
    }
}
