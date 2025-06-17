package com.example.haidtracker.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.auth.LoginRequest;
import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.data.model.auth.RegisterRequest;
import com.example.haidtracker.data.model.auth.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String TOKEN_KEY = "auth_token";
    
    private ApiService apiService;
    private Context context;

    public AuthRepository(Context context) {
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void login(String email, String password, AuthCallback<LoginResponse> callback) {
        LoginRequest request = new LoginRequest(email, password);
        
        Log.d(TAG, "Attempting login for email: " + email);
        
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    
                    if (loginResponse.getToken() != null) {
                        // Save token to SharedPreferences with Bearer prefix
                        saveToken("Bearer " + loginResponse.getToken());
                        Log.d(TAG, "Login successful, token saved");
                        callback.onSuccess(loginResponse);
                    } else {
                        String errorMsg = loginResponse.getMessage() != null ? 
                                loginResponse.getMessage() : "Login failed - no token received";
                        Log.e(TAG, "Login failed: " + errorMsg);
                        callback.onError(errorMsg);
                    }
                } else {
                    String errorMsg = getErrorMessage(response.code());
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }

    // Method dengan 3 parameter (password confirmation otomatis sama dengan password)
    public void register(String name, String email, String password, AuthCallback<RegisterResponse> callback) {
        RegisterRequest request = new RegisterRequest(name, email, password);
        performRegistration(request, callback);
    }

    // Method dengan 4 parameter (dengan password confirmation terpisah)
    public void register(String name, String email, String password, String passwordConfirmation, AuthCallback<RegisterResponse> callback) {
        RegisterRequest request = new RegisterRequest(name, email, password, passwordConfirmation);
        performRegistration(request, callback);
    }

    private void performRegistration(RegisterRequest request, AuthCallback<RegisterResponse> callback) {
        Log.d(TAG, "Attempting registration for email: " + request.getEmail());
        
        apiService.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    
                    // For registration, we typically don't auto-login
                    // User needs to login after successful registration
                    Log.d(TAG, "Registration successful");
                    callback.onSuccess(registerResponse);
                } else {
                    String errorMsg = getErrorMessage(response.code());
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }

    public void logout() {
        clearToken();
        clearRememberMe();
        Log.d(TAG, "User logged out, token and preferences cleared");
    }

    public boolean isLoggedIn() {
        String token = getToken();
        boolean loggedIn = token != null && !token.isEmpty();
        Log.d(TAG, "User logged in status: " + loggedIn);
        return loggedIn;
    }

    public String getToken() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, null);
    }

    public void saveRememberMe(String email) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean("remember_me", true)
                .putString("saved_email", email)
                .apply();
        Log.d(TAG, "Remember me preferences saved");
    }

    public void clearRememberMe() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .remove("remember_me")
                .remove("saved_email")
                .apply();
        Log.d(TAG, "Remember me preferences cleared");
    }

    public boolean isRememberMeEnabled() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean("remember_me", false);
    }

    public String getSavedEmail() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("saved_email", "");
    }

    private void saveToken(String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(TOKEN_KEY, token).apply();
        Log.d(TAG, "Token saved to SharedPreferences");
    }

    private void clearToken() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(TOKEN_KEY).apply();
        Log.d(TAG, "Token cleared from SharedPreferences");
    }

    private String getErrorMessage(int responseCode) {
        switch (responseCode) {
            case 400:
                return "Bad request - please check your input";
            case 401:
                return "Invalid email or password";
            case 422:
                return "Validation error - please check your data";
            case 500:
                return "Server error - please try again later";
            default:
                return "Request failed with code: " + responseCode;
        }
    }

    public interface AuthCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }
}
