package com.example.haidtracker.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.cycle.Cycle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiTester {
    private static final String TAG = "ApiTester";
    
    public interface TestCallback {
        void onTestComplete(boolean success, String message);
    }
    
    public static void testCycleApi(Context context, TestCallback callback) {
        Log.d(TAG, "=== TESTING CYCLE API ===");
        
        // Step 1: Check token
        String token = TokenManager.getToken(context);
        if (token == null || token.trim().isEmpty()) {
            String msg = "No auth token found";
            Log.e(TAG, msg);
            callback.onTestComplete(false, msg);
            return;
        }
        
        Log.d(TAG, "Token found: " + token.substring(0, Math.min(20, token.length())) + "...");
        
        // Step 2: Create API service
        ApiService apiService;
        try {
            apiService = ApiClient.getClient().create(ApiService.class);
            Log.d(TAG, "API Service created successfully");
        } catch (Exception e) {
            String msg = "Failed to create API service: " + e.getMessage();
            Log.e(TAG, msg, e);
            callback.onTestComplete(false, msg);
            return;
        }
        
        // Step 3: Test API call
        Log.d(TAG, "Making API call to getAllCycles...");
        Log.d(TAG, "Base URL: " + ApiClient.getClient().baseUrl());
        Log.d(TAG, "Using endpoint: api/cycles (same as CalenderActivity)");

        try {
            Call<List<Cycle>> call = apiService.getAllCycles(token);
            call.enqueue(new Callback<List<Cycle>>() {
                @Override
                public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {
                    Log.d(TAG, "=== API RESPONSE RECEIVED ===");
                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response message: " + response.message());
                    Log.d(TAG, "Is successful: " + response.isSuccessful());
                    Log.d(TAG, "Headers: " + response.headers().toString());
                    
                    if (response.isSuccessful()) {
                        List<Cycle> cycles = response.body();
                        if (cycles != null) {
                            String msg = "SUCCESS: Found " + cycles.size() + " cycles";
                            Log.d(TAG, msg);
                            
                            // Log first few cycles for debugging
                            for (int i = 0; i < Math.min(cycles.size(), 3); i++) {
                                Cycle cycle = cycles.get(i);
                                Log.d(TAG, "Cycle " + i + ": " + cycle.toString());
                            }
                            
                            callback.onTestComplete(true, msg);
                        } else {
                            String msg = "SUCCESS but response body is null";
                            Log.w(TAG, msg);
                            callback.onTestComplete(true, msg);
                        }
                    } else {
                        String msg = "API Error " + response.code() + ": " + response.message();
                        Log.e(TAG, msg);
                        
                        // Try to get error body
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "Error body: " + errorBody);
                                msg += " - " + errorBody;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body", e);
                        }
                        
                        callback.onTestComplete(false, msg);
                    }
                }
                
                @Override
                public void onFailure(Call<List<Cycle>> call, Throwable t) {
                    String msg = "Network Error: " + t.getMessage();
                    Log.e(TAG, "=== API CALL FAILED ===");
                    Log.e(TAG, msg, t);
                    Log.e(TAG, "Error type: " + t.getClass().getSimpleName());
                    
                    if (t.getCause() != null) {
                        Log.e(TAG, "Cause: " + t.getCause().getMessage());
                        msg += " (Cause: " + t.getCause().getMessage() + ")";
                    }
                    
                    callback.onTestComplete(false, msg);
                }
            });
        } catch (Exception e) {
            String msg = "Exception creating API call: " + e.getMessage();
            Log.e(TAG, msg, e);
            callback.onTestComplete(false, msg);
        }
    }
    
    public static void showTestResult(Context context, boolean success, String message) {
        String prefix = success ? "✅ " : "❌ ";
        Toast.makeText(context, prefix + message, Toast.LENGTH_LONG).show();
        Log.i(TAG, prefix + message);
    }
}
