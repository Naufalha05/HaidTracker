package com.example.haidtracker.ui.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.ui.auth.SignInActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminCyclesActivity extends AppCompatActivity {

    private static final String TAG = "AdminCyclesActivity";
    
    private RecyclerView recyclerView;
    private AdminCyclesAdapter adapter;
    private ProgressBar loadingIndicator;
    private ImageButton btnLogout;
    
    private AdminApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cycles);

        initializeViews();
        setupClickListeners();
        loadCycles();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewCycles);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        btnLogout = findViewById(R.id.btn_logout);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminCyclesAdapter(new ArrayList<>());
        
        // Set delete listener
        adapter.setOnCycleDeleteListener((cycle, position) -> {
            showDeleteConfirmationDialog(cycle, position);
        });
        
        recyclerView.setAdapter(adapter);

        // Initialize API manager
        apiManager = new AdminApiManager(this);
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

    private void loadCycles() {
        loadingIndicator.setVisibility(View.VISIBLE);
        
        apiManager.getAllCycles(new AdminApiManager.CyclesListCallback() {
            @Override
            public void onSuccess(List<Cycle> cycles) {
                loadingIndicator.setVisibility(View.GONE);
                adapter.updateData(cycles);
                Log.d(TAG, "Loaded " + cycles.size() + " cycles");
            }

            @Override
            public void onError(String error) {
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(AdminCyclesActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading cycles: " + error);
            }
        });
    }

    private void showDeleteConfirmationDialog(Cycle cycle, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Cycle");
        builder.setMessage("Are you sure you want to delete this cycle?");
        
        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteCycle(cycle, position);
        });
        
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
        // Customize button colors
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void deleteCycle(Cycle cycle, int position) {
        loadingIndicator.setVisibility(View.VISIBLE);
        
        apiManager.deleteCycle(cycle.getId(), new AdminApiManager.AdminCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(AdminCyclesActivity.this, "Cycle deleted successfully", Toast.LENGTH_SHORT).show();
                adapter.removeCycle(position);
            }

            @Override
            public void onError(String error) {
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(AdminCyclesActivity.this, "Failed to delete cycle: " + error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error deleting cycle: " + error);
            }
        });
    }
}
