package com.example.haidtracker.util;

import android.content.Context;
import android.util.Log;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.cycle.Cycle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CycleDebugger {
    private static final String TAG = "CycleDebugger";
    
    public static void debugCycleLoading(Context context) {
        Log.d(TAG, "=== CYCLE LOADING DEBUG ===");
        
        // 1. Check token
        String token = TokenManager.getToken(context);
        Log.d(TAG, "Token exists: " + (!token.isEmpty()));
        Log.d(TAG, "Token length: " + token.length());
        if (!token.isEmpty()) {
            Log.d(TAG, "Token starts with Bearer: " + token.startsWith("Bearer "));
            Log.d(TAG, "Token preview: " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
        }
        
        // 2. Check API client
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Log.d(TAG, "API Service created: " + (apiService != null));
        Log.d(TAG, "Base URL: " + ApiClient.getClient().baseUrl());
        
        // 3. Test API call
        if (!token.isEmpty()) {
            Log.d(TAG, "Testing getMyCycles API call...");
            
            apiService.getMyCycles(token).enqueue(new Callback<List<Cycle>>() {
                @Override
                public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {
                    Log.d(TAG, "=== API RESPONSE ===");
                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response message: " + response.message());
                    Log.d(TAG, "Is successful: " + response.isSuccessful());
                    
                    if (response.isSuccessful()) {
                        List<Cycle> cycles = response.body();
                        if (cycles != null) {
                            Log.d(TAG, "Cycles count: " + cycles.size());
                            for (int i = 0; i < Math.min(cycles.size(), 3); i++) {
                                Cycle cycle = cycles.get(i);
                                Log.d(TAG, "Cycle " + i + ": ID=" + cycle.getId() + 
                                      ", StartDate=" + cycle.getStartDate() + 
                                      ", EndDate=" + cycle.getEndDate() + 
                                      ", Note=" + cycle.getNote());
                            }
                        } else {
                            Log.w(TAG, "Response body is null");
                        }
                    } else {
                        Log.e(TAG, "API call failed with code: " + response.code());
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "Error body: " + errorBody);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body", e);
                        }
                    }
                }
                
                @Override
                public void onFailure(Call<List<Cycle>> call, Throwable t) {
                    Log.e(TAG, "=== API FAILURE ===");
                    Log.e(TAG, "Network error: " + t.getMessage(), t);
                    Log.e(TAG, "Error class: " + t.getClass().getSimpleName());
                    if (t.getCause() != null) {
                        Log.e(TAG, "Cause: " + t.getCause().getMessage());
                    }
                }
            });
        } else {
            Log.e(TAG, "Cannot test API - no token available");
        }
    }
    
    public static void debugTokenManager(Context context) {
        Log.d(TAG, "=== TOKEN MANAGER DEBUG ===");
        
        // Test hasToken
        boolean hasToken = TokenManager.hasToken(context);
        Log.d(TAG, "hasToken(): " + hasToken);
        
        // Test getToken
        String token = TokenManager.getToken(context);
        Log.d(TAG, "getToken() result: " + (token.isEmpty() ? "EMPTY" : "NOT EMPTY"));
        Log.d(TAG, "Token length: " + token.length());
        
        if (!token.isEmpty()) {
            Log.d(TAG, "Token format check:");
            Log.d(TAG, "  - Starts with 'Bearer ': " + token.startsWith("Bearer "));
            Log.d(TAG, "  - Contains spaces: " + token.contains(" "));
            Log.d(TAG, "  - First 10 chars: " + (token.length() >= 10 ? token.substring(0, 10) : token));
        }
    }
}
