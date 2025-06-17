package com.example.haidtracker.ui.siklus;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.cycle.CreateCycleRequest;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.ui.analytics.AnalyticsActivity;
import com.example.haidtracker.ui.auth.SignInActivity;
import com.example.haidtracker.ui.calender.CalenderActivity;
import com.example.haidtracker.ui.content.ContentActivity;
import com.example.haidtracker.ui.profile.ProfileActivity;
import com.example.haidtracker.util.TokenManager;
import com.example.haidtracker.util.CycleDebugger;
import com.example.haidtracker.util.ApiTester;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiklusActivity extends AppCompatActivity implements siklusAdapter.OnCycleDeleteListener {

    private static final String TAG = "SiklusActivity";
    private RecyclerView recyclerView;
    private siklusAdapter adapter;
    private SiklusViewModel viewModel;
    private ApiService apiService;
    private String authToken;

    // UI Components
    private ImageView iconCalendar, iconAnalisis, iconProfil, iconContent;
    private FrameLayout fabPlus;
    private ImageButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siklus);

        // Initialize UI components
        iconCalendar = findViewById(R.id.menu_calender);
        iconAnalisis = findViewById(R.id.analisis);
        iconContent = findViewById(R.id.content);
        iconProfil = findViewById(R.id.menu_icon);
        fabPlus = findViewById(R.id.fab_plus);
        btnLogout = findViewById(R.id.btn_logout);

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCycles);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new siklusAdapter(new ArrayList<>());
            adapter.setOnCycleDeleteListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e(TAG, "RecyclerView not found in layout");
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(SiklusViewModel.class);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);
        authToken = TokenManager.getToken(this);

        // Check if token is valid
        if (authToken == null || authToken.trim().isEmpty()) {
            Log.e(TAG, "No valid auth token found, redirecting to login");
            TokenManager.clearToken(this);
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // Observe cycles data
        viewModel.getCycles().observe(this, cycles -> {
            if (cycles != null) {
                adapter.updateData(cycles);
                Log.d(TAG, "Updated adapter with " + cycles.size() + " cycles");
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            if (errorMsg != null && !errorMsg.trim().isEmpty()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error from ViewModel: " + errorMsg);

                // If it's an auth error, redirect to login
                if (errorMsg.contains("Unauthorized") || errorMsg.contains("Authentication token")) {
                    TokenManager.clearToken(this);
                    Intent intent = new Intent(this, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(this, isLoading -> {
            // You can add a progress bar here if needed
            Log.d(TAG, "Loading state: " + isLoading);
        });

        // Debug token and API before loading cycles
        Log.d(TAG, "=== DEBUGGING CYCLE LOADING ISSUE ===");
        CycleDebugger.debugTokenManager(this);
        CycleDebugger.debugCycleLoading(this);

        // Load cycles
        Log.d(TAG, "Starting to fetch cycles");
        viewModel.fetchCycles(authToken);

        // Navigasi menu bawah
        iconCalendar.setOnClickListener(v -> startActivity(new Intent(this, CalenderActivity.class)));
        iconAnalisis.setOnClickListener(v -> startActivity(new Intent(this, AnalyticsActivity.class)));
        iconProfil.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        iconContent.setOnClickListener(v -> startActivity(new Intent(this, ContentActivity.class)));

        // Setup add cycle button (FAB)
        fabPlus.setOnClickListener(v -> showAddCycleDialog());

        // Add long click for API testing (temporary for debugging)
        fabPlus.setOnLongClickListener(v -> {
            Log.d(TAG, "Running API test...");
            ApiTester.testCycleApi(this, (success, message) -> {
                runOnUiThread(() -> {
                    ApiTester.showTestResult(this, success, message);
                    if (success) {
                        // If test successful, try to reload cycles
                        viewModel.fetchCycles(authToken);
                    }
                });
            });
            return true;
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("HaidTrackerPrefs", MODE_PRIVATE);
            prefs.edit().remove("auth_token").apply();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showAddCycleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_cycle, null);
        builder.setView(dialogView);

        EditText etStartDate = dialogView.findViewById(R.id.etStartDate);
        EditText etEndDate = dialogView.findViewById(R.id.etEndDate);
        EditText etNote = dialogView.findViewById(R.id.etNote);
        EditText etUserId = dialogView.findViewById(R.id.etUserId);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Hide userId field for regular users
        etUserId.setVisibility(View.GONE);

        AlertDialog dialog = builder.create();

        // Date picker for start date
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        
        // Date picker for end date
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnSave.setOnClickListener(v -> {
            String startDate = etStartDate.getText().toString();
            String endDate = etEndDate.getText().toString();
            String note = etNote.getText().toString();

            if (startDate.isEmpty()) {
                Toast.makeText(SiklusActivity.this, "Start date is required", Toast.LENGTH_SHORT).show();
                return;
            }

            // End date is optional, if not provided, use start date
            if (endDate.isEmpty()) {
                endDate = startDate;
            }

            CreateCycleRequest request = new CreateCycleRequest(startDate, endDate, note);
            createCycle(request);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDatePicker(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    editText.setText(dateFormat.format(calendar.getTime()));
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void createCycle(CreateCycleRequest request) {
        // Log request untuk debugging
        Log.d(TAG, "Creating cycle with data: startDate=" + request.getStartDate() + 
              ", endDate=" + request.getEndDate() + ", note=" + request.getNote());
              
        viewModel.createCycle(authToken, request, new Callback<Cycle>() {
            @Override
            public void onResponse(Call<Cycle> call, Response<Cycle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SiklusActivity.this, "Cycle added successfully", Toast.LENGTH_SHORT).show();
                    // Refresh the cycle list
                    viewModel.fetchCycles(authToken);
                } else {
                    String errorMsg = "Failed to add cycle: ";
                    if (response.code() == 401) {
                        errorMsg += "Unauthorized. Please login again.";
                        // Token expired, redirect to login
                        TokenManager.clearToken(SiklusActivity.this);
                        Intent intent = new Intent(SiklusActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else if (response.code() == 400) {
                        errorMsg += "Invalid data provided.";
                        try {
                            // Try to get more detailed error message from response body
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "Error response body: " + errorBody);
                                errorMsg += " Details: " + errorBody;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body", e);
                        }
                    } else {
                        errorMsg += "Server error (" + response.code() + ").";
                    }
                    Toast.makeText(SiklusActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error creating cycle: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Cycle> call, Throwable t) {
                Toast.makeText(SiklusActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error creating cycle", t);
            }
        });
    }

    @Override
    public void onDeleteCycle(Cycle cycle, int position) {
        showDeleteConfirmationDialog(cycle, position);
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
        viewModel.deleteCycle(authToken, cycle.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SiklusActivity.this, "Cycle deleted successfully", Toast.LENGTH_SHORT).show();
                    adapter.removeCycle(position);
                } else {
                    Toast.makeText(SiklusActivity.this, "Failed to delete cycle", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error deleting cycle: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SiklusActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error deleting cycle", t);
            }
        });
    }
}
