package com.example.haidtracker.ui.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.analytics.Analytics;
import com.example.haidtracker.ui.auth.SignInActivity;
import com.example.haidtracker.util.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAnalyticsActivity extends AppCompatActivity {

    private static final String TAG = "AdminAnalyticsActivity";
    
    private RecyclerView recyclerView;
    private AdminAnalyticsAdapter adapter;
    private ProgressBar loadingIndicator;
    private ImageButton btnLogout;
    
    private ApiService apiService;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_analytics);

        initializeViews();
        setupClickListeners();
        loadAnalytics();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewAnalytics);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        btnLogout = findViewById(R.id.btn_logout);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminAnalyticsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

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
    }

    private void loadAnalytics() {
        loadingIndicator.setVisibility(View.VISIBLE);
        
        // Try to get admin analytics first, fallback to regular analytics
        apiService.getAdminAnalytics(authToken).enqueue(new Callback<List<Analytics>>() {
            @Override
            public void onResponse(Call<List<Analytics>> call, Response<List<Analytics>> response) {
                loadingIndicator.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                    Log.d(TAG, "Loaded " + response.body().size() + " analytics records");
                } else {
                    // Fallback to single analytics if admin endpoint fails
                    loadSingleAnalytics();
                }
            }

            @Override
            public void onFailure(Call<List<Analytics>> call, Throwable t) {
                Log.e(TAG, "Failed to load admin analytics, trying fallback", t);
                // Fallback to single analytics
                loadSingleAnalytics();
            }
        });
    }

    private void loadSingleAnalytics() {
        apiService.getAnalytics(authToken).enqueue(new Callback<Analytics>() {
            @Override
            public void onResponse(Call<Analytics> call, Response<Analytics> response) {
                loadingIndicator.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Analytics> analyticsList = new ArrayList<>();
                    analyticsList.add(response.body());
                    adapter.updateData(analyticsList);
                    Log.d(TAG, "Loaded single analytics record");
                } else {
                    Toast.makeText(AdminAnalyticsActivity.this, "Gagal memuat data analytics", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error loading analytics: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Analytics> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(AdminAnalyticsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error loading analytics", t);
            }
        });
    }
}
