package com.example.haidtracker.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.auth.LoginRequest;
import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.ui.auth.SignInActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenRefresher {
    private static final String TAG = "TokenRefresher";
    
    public interface TokenRefreshCallback {
        void onSuccess(String newToken);
        void onFailure(String errorMessage);
    }
    
    public static void refreshToken(Context context, String email, String password, TokenRefreshCallback callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(email, password);
        
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getToken() != null) {
                    String newToken = response.body().getToken();
                    TokenManager.saveToken(context, newToken);
                    Log.d(TAG, "Token refreshed successfully");
                    callback.onSuccess(newToken);
                } else {
                    Log.e(TAG, "Failed to refresh token: " + response.code());
                    callback.onFailure("Failed to refresh token: " + response.code());
                    
                    // Redirect to login if we can't refresh the token
                    redirectToLogin(context);
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Network error refreshing token", t);
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
    
    public static void redirectToLogin(Context context) {
        TokenManager.clearToken(context);
        Intent intent = new Intent(context, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}