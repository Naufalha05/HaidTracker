package com.example.haidtracker.ui.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.analytics.Analytics;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.data.model.user.User;
import com.example.haidtracker.ui.auth.SignInActivity;
import com.example.haidtracker.util.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    private static final String TAG = "AdminDashboardActivity";
    
    // UI Components
    private TextView tvTotalUsers, tvTotalCycles, tvActiveUsers, tvSystemHealth;
    private CardView cardUsers, cardCycles, cardAnalytics, cardSettings;
    private ProgressBar loadingIndicator;
    private ImageButton btnLogout;
    
    // API
    private ApiService apiService;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        initializeViews();
        setupClickListeners();
        loadDashboardData();
    }

    private void initializeViews() {
        // Statistics TextViews
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalCycles = findViewById(R.id.tvTotalCycles);
        tvActiveUsers = findViewById(R.id.tvActiveUsers);
        tvSystemHealth = findViewById(R.id.tvSystemHealth);
        
        // Cards
        cardUsers = findViewById(R.id.cardUsers);
        cardCycles = findViewById(R.id.cardCycles);
        cardAnalytics = findViewById(R.id.cardAnalytics);
        cardSettings = findViewById(R.id.cardSettings);
        
        // Other UI
        loadingIndicator = findViewById(R.id.loadingIndicator);
        btnLogout = findViewById(R.id.btn_logout);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);
        authToken = TokenManager.getToken(this);
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("HaidTrackerPrefs", MODE_PRIVATE);
            prefs.edit().remove("auth_token").apply();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Card click listeners
        cardUsers.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminUsersActivity.class);
            startActivity(intent);
        });

        cardCycles.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminCyclesActivity.class);
            startActivity(intent);
        });

        cardAnalytics.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminAnalyticsActivity.class);
            startActivity(intent);
        });

        cardSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadDashboardData() {
        loadingIndicator.setVisibility(View.VISIBLE);
        
        // Load users count
        loadUsersCount();
        
        // Load cycles count
        loadCyclesCount();
        
        // Load analytics data
        loadAnalyticsData();
    }

    private void loadUsersCount() {
        apiService.getAllUsers(authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int totalUsers = response.body().size();
                    tvTotalUsers.setText(String.valueOf(totalUsers));
                    
                    // Count active users (simplified - all users are considered active)
                    tvActiveUsers.setText(String.valueOf(totalUsers));
                    
                    Log.d(TAG, "Loaded users count: " + totalUsers);
                } else {
                    tvTotalUsers.setText("Error");
                    tvActiveUsers.setText("Error");
                    Log.e(TAG, "Failed to load users: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                tvTotalUsers.setText("Error");
                tvActiveUsers.setText("Error");
                Log.e(TAG, "Network error loading users", t);
            }
        });
    }

    private void loadCyclesCount() {
        apiService.getAllCyclesAdmin(authToken).enqueue(new Callback<List<Cycle>>() {
            @Override
            public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int totalCycles = response.body().size();
                    tvTotalCycles.setText(String.valueOf(totalCycles));
                    Log.d(TAG, "Loaded cycles count: " + totalCycles);
                } else {
                    tvTotalCycles.setText("Error");
                    Log.e(TAG, "Failed to load cycles: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Cycle>> call, Throwable t) {
                tvTotalCycles.setText("Error");
                Log.e(TAG, "Network error loading cycles", t);
            }
        });
    }

    private void loadAnalyticsData() {
        // Try to load analytics data for system health
        apiService.getAnalytics(authToken).enqueue(new Callback<Analytics>() {
            @Override
            public void onResponse(Call<Analytics> call, Response<Analytics> response) {
                loadingIndicator.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    Analytics analytics = response.body();
                    
                    // Calculate system health based on analytics data
                    String healthStatus = calculateSystemHealth(analytics);
                    tvSystemHealth.setText(healthStatus);
                    
                    Log.d(TAG, "Loaded analytics data successfully");
                } else {
                    tvSystemHealth.setText("Unknown");
                    Log.e(TAG, "Failed to load analytics: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Analytics> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                tvSystemHealth.setText("Error");
                Log.e(TAG, "Network error loading analytics", t);
            }
        });
    }

    private String calculateSystemHealth(Analytics analytics) {
        // Simple health calculation based on available data
        if (analytics.getTotalCycles() != null && analytics.getTotalCycles() > 0) {
            if (analytics.getAverageCycleLength() != null && 
                analytics.getAverageCycleLength() >= 21 && 
                analytics.getAverageCycleLength() <= 35) {
                return "Good";
            } else {
                return "Fair";
            }
        } else {
            return "No Data";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to dashboard
        loadDashboardData();
    }
}
