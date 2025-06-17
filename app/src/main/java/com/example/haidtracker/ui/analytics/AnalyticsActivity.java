package com.example.haidtracker.ui.analytics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.haidtracker.R;
import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.analytics.Analytics;
import com.example.haidtracker.ui.auth.SignInActivity;
import com.example.haidtracker.ui.calender.CalenderActivity;
import com.example.haidtracker.ui.profile.ProfileActivity;
import com.example.haidtracker.ui.siklus.SiklusActivity;
import com.example.haidtracker.ui.symptom.SymptomActivity;
import com.example.haidtracker.util.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsActivity extends AppCompatActivity {

    private static final String TAG = "AnalyticsActivity";
    
    private TextView tvAverageCycleLength;
    private TextView tvLastCycleLength;
    private TextView tvNextPredictedDate;
    private TextView tvTotalCycles;
    private ProgressBar loadingIndicator;
    private ImageButton btnLogout;
    
    private ApiService apiService;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        initializeViews();
        setupClickListeners();
        loadAnalytics();
    }

    private void initializeViews() {
        tvAverageCycleLength = findViewById(R.id.tvAverageCycleLength);
        tvLastCycleLength = findViewById(R.id.tvLastCycleLength);
        tvNextPredictedDate = findViewById(R.id.tvNextPredictedDate);
        tvTotalCycles = findViewById(R.id.tvTotalCycles);
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

        setupNavigation();
    }

    private void setupNavigation() {
        // Navigation untuk bottom navbar
        findViewById(R.id.menu_siklus).setOnClickListener(v -> 
            startActivity(new Intent(this, SiklusActivity.class)));
        findViewById(R.id.menu_calender).setOnClickListener(v -> 
            startActivity(new Intent(this, CalenderActivity.class)));
        findViewById(R.id.content).setOnClickListener(v -> 
            startActivity(new Intent(this, SymptomActivity.class)));
        findViewById(R.id.menu_icon).setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class)));
        
        // Current page indicator
        findViewById(R.id.analisis).setOnClickListener(v -> {
            Toast.makeText(this, "Anda sedang di halaman Analytics", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadAnalytics() {
        loadingIndicator.setVisibility(View.VISIBLE);
        
        apiService.getAnalytics(authToken).enqueue(new Callback<Analytics>() {
            @Override
            public void onResponse(Call<Analytics> call, Response<Analytics> response) {
                loadingIndicator.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    displayAnalytics(response.body());
                } else {
                    Toast.makeText(AnalyticsActivity.this, "Gagal memuat data analytics", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error loading analytics: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Analytics> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(AnalyticsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error loading analytics", t);
            }
        });
    }

    private void displayAnalytics(Analytics analytics) {
        if (analytics.getAverageCycleLength() != null) {
            tvAverageCycleLength.setText(String.format("%.1f hari", analytics.getAverageCycleLength()));
        } else {
            tvAverageCycleLength.setText("Belum ada data");
        }

        if (analytics.getLastCycleLength() != null) {
            tvLastCycleLength.setText(String.format("%d hari", analytics.getLastCycleLength()));
        } else {
            tvLastCycleLength.setText("Belum ada data");
        }

        if (analytics.getNextPredictedDate() != null) {
            tvNextPredictedDate.setText(analytics.getNextPredictedDate());
        } else {
            tvNextPredictedDate.setText("Belum dapat diprediksi");
        }

        if (analytics.getTotalCycles() != null) {
            tvTotalCycles.setText(String.valueOf(analytics.getTotalCycles()));
        } else {
            tvTotalCycles.setText("0");
        }
    }
}
