package com.example.haidtracker.util;

import android.content.Context;
import android.util.Log;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.auth.LoginRequest;
import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.data.model.auth.RegisterRequest;
import com.example.haidtracker.data.model.auth.RegisterResponse;
import com.example.haidtracker.data.model.user.User;
import com.example.haidtracker.data.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiDebugger {
    private static final String TAG = "ApiDebugger";
    
    private ApiService apiService;
    private AuthRepository authRepository;
    private Context context;

    public ApiDebugger(Context context) {
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiService.class);
        this.authRepository = new AuthRepository(context);
    }

    public void testUserProfile() {
        String token = authRepository.getToken();
        
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "No authentication token found");
            return;
        }

        Log.d(TAG, "Testing getUserProfile with token: " + token.substring(0, Math.min(token.length(), 20)) + "...");

        // Use getUserProfile method from ApiService (not getOwnProfile)
        apiService.getUserProfile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Log.d(TAG, "✅ getUserProfile SUCCESS");
                    Log.d(TAG, "User ID: " + user.getId());
                    Log.d(TAG, "User Name: " + user.getName());
                    Log.d(TAG, "User Email: " + user.getEmail());
                    Log.d(TAG, "User Role: " + user.getRole());
                } else {
                    Log.e(TAG, "❌ getUserProfile FAILED");
                    Log.e(TAG, "Response Code: " + response.code());
                    Log.e(TAG, "Response Message: " + response.message());
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error Body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "❌ getUserProfile NETWORK ERROR");
                Log.e(TAG, "Error: " + t.getMessage(), t);
            }
        });
    }

    public void testLogin(String email, String password) {
        Log.d(TAG, "Testing login with email: " + email);
        
        LoginRequest request = new LoginRequest(email, password);
        
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "✅ LOGIN SUCCESS");
                    Log.d(TAG, "Token: " + loginResponse.getToken().substring(0, Math.min(loginResponse.getToken().length(), 20)) + "...");
                    Log.d(TAG, "User: " + loginResponse.getUser().getEmail());
                    Log.d(TAG, "Message: " + loginResponse.getMessage());
                } else {
                    Log.e(TAG, "❌ LOGIN FAILED");
                    Log.e(TAG, "Response Code: " + response.code());
                    Log.e(TAG, "Response Message: " + response.message());
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error Body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "❌ LOGIN NETWORK ERROR");
                Log.e(TAG, "Error: " + t.getMessage(), t);
            }
        });
    }

    public void testRegister(String name, String email, String password) {
        Log.d(TAG, "Testing register with email: " + email);
        
        RegisterRequest request = new RegisterRequest(name, email, password);
        
        apiService.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    Log.d(TAG, "✅ REGISTER SUCCESS");
                    Log.d(TAG, "User: " + registerResponse.getUser().getEmail());
                    Log.d(TAG, "Message: " + registerResponse.getMessage());
                    if (registerResponse.getToken() != null) {
                        Log.d(TAG, "Token: " + registerResponse.getToken().substring(0, Math.min(registerResponse.getToken().length(), 20)) + "...");
                    }
                } else {
                    Log.e(TAG, "❌ REGISTER FAILED");
                    Log.e(TAG, "Response Code: " + response.code());
                    Log.e(TAG, "Response Message: " + response.message());
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error Body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e(TAG, "❌ REGISTER NETWORK ERROR");
                Log.e(TAG, "Error: " + t.getMessage(), t);
            }
        });
    }

    public void testApiConnection() {
        Log.d(TAG, "=== API CONNECTION TEST ===");
        Log.d(TAG, "Base URL: " + ApiClient.getClient().baseUrl());
        
        // Test if user is logged in
        boolean isLoggedIn = authRepository.isLoggedIn();
        Log.d(TAG, "User logged in: " + isLoggedIn);
        
        if (isLoggedIn) {
            String token = authRepository.getToken();
            Log.d(TAG, "Token exists: " + (token != null && !token.isEmpty()));
            if (token != null) {
                Log.d(TAG, "Token preview: " + token.substring(0, Math.min(token.length(), 20)) + "...");
            }
            
            // Test profile endpoint
            testUserProfile();
        } else {
            Log.d(TAG, "User not logged in - cannot test authenticated endpoints");
        }
    }

    public void debugApiEndpoints() {
        Log.d(TAG, "=== API ENDPOINTS DEBUG ===");
        Log.d(TAG, "Available endpoints:");
        Log.d(TAG, "POST /api/auth/login - Login user");
        Log.d(TAG, "POST /api/auth/register - Register user");
        Log.d(TAG, "GET /api/users/profile - Get user profile (authenticated)");
        Log.d(TAG, "PUT /api/users/profile - Update user profile (authenticated)");
        Log.d(TAG, "GET /api/cycles - Get user cycles (authenticated)");
        Log.d(TAG, "POST /api/cycles - Create cycle (authenticated)");
        Log.d(TAG, "GET /api/reminders - Get user reminders (authenticated)");
        Log.d(TAG, "POST /api/reminders - Create reminder (authenticated)");
        Log.d(TAG, "GET /api/analytics - Get user analytics (authenticated)");
    }

    public void logCurrentState() {
        Log.d(TAG, "=== CURRENT STATE ===");
        Log.d(TAG, "Is logged in: " + authRepository.isLoggedIn());
        Log.d(TAG, "Remember me enabled: " + authRepository.isRememberMeEnabled());
        if (authRepository.isRememberMeEnabled()) {
            Log.d(TAG, "Saved email: " + authRepository.getSavedEmail());
        }
        
        String token = authRepository.getToken();
        if (token != null) {
            Log.d(TAG, "Token length: " + token.length());
            Log.d(TAG, "Token starts with Bearer: " + token.startsWith("Bearer"));
        } else {
            Log.d(TAG, "No token found");
        }
    }

    // Helper method to run all tests
    public void runAllTests() {
        Log.d(TAG, "🚀 STARTING API DEBUGGER TESTS");
        debugApiEndpoints();
        logCurrentState();
        testApiConnection();
        Log.d(TAG, "✅ API DEBUGGER TESTS COMPLETED");
    }
}

