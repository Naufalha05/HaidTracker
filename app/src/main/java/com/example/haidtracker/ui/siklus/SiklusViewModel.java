package com.example.haidtracker.ui.siklus;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.cycle.CreateCycleRequest;
import com.example.haidtracker.data.model.cycle.Cycle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiklusViewModel extends ViewModel {

    private static final String TAG = "SiklusViewModel";
    private MutableLiveData<List<Cycle>> cycles = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private ApiService apiService;

    public SiklusViewModel() {
        apiService = ApiClient.getClient().create(ApiService.class);
        // Initialize with empty list instead of null
        cycles.setValue(new ArrayList<>());
    }

    public LiveData<List<Cycle>> getCycles() {
        return cycles;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchCycles(String authToken) {
        if (authToken == null || authToken.trim().isEmpty()) {
            Log.e(TAG, "Auth token is null or empty");
            errorMessage.setValue("Authentication token is missing. Please login again.");
            return;
        }

        Log.d(TAG, "Fetching cycles with token: " + (authToken.length() > 10 ? authToken.substring(0, 10) + "..." : authToken));
        Log.d(TAG, "Token format check - starts with Bearer: " + authToken.startsWith("Bearer "));
        Log.d(TAG, "API Service: " + (apiService != null ? "OK" : "NULL"));
        isLoading.setValue(true);

        // Use getAllCycles instead of getMyCycles (same as CalenderActivity)
        Call<List<Cycle>> call = apiService.getAllCycles(authToken);
        Log.d(TAG, "API Call created: " + (call != null ? "OK" : "NULL"));
        Log.d(TAG, "Using endpoint: api/cycles (getAllCycles)");

        call.enqueue(new Callback<List<Cycle>>() {
            @Override
            public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    List<Cycle> cycleList = response.body();
                    if (cycleList != null) {
                        Log.d(TAG, "Successfully fetched " + cycleList.size() + " cycles");
                        cycles.setValue(cycleList);
                        errorMessage.setValue(null); // Clear any previous error
                    } else {
                        Log.d(TAG, "Response body is null, setting empty list");
                        cycles.setValue(new ArrayList<>());
                        errorMessage.setValue(null);
                    }
                } else {
                    String error = "Failed to load cycles: ";
                    switch (response.code()) {
                        case 401:
                            error += "Unauthorized. Please login again.";
                            break;
                        case 403:
                            error += "Access forbidden.";
                            break;
                        case 404:
                            error += "Cycles not found.";
                            break;
                        case 500:
                            error += "Server error.";
                            break;
                        default:
                            error += "Error " + response.code();
                    }
                    Log.e(TAG, error);
                    errorMessage.setValue(error);
                    // Don't set cycles to null, keep existing data or empty list
                    if (cycles.getValue() == null) {
                        cycles.setValue(new ArrayList<>());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cycle>> call, Throwable t) {
                isLoading.setValue(false);
                String error = "Network error: " + t.getMessage();
                Log.e(TAG, "Network error fetching cycles", t);
                errorMessage.setValue(error);
                // Don't set cycles to null, keep existing data or empty list
                if (cycles.getValue() == null) {
                    cycles.setValue(new ArrayList<>());
                }
            }
        });
    }

    public void createCycle(String authToken, CreateCycleRequest request, Callback<Cycle> callback) {
        apiService.createCycle(authToken, request).enqueue(callback);
    }

    public void deleteCycle(String authToken, int cycleId, Callback<Void> callback) {
        apiService.deleteCycle(authToken, cycleId).enqueue(callback);
    }
}
