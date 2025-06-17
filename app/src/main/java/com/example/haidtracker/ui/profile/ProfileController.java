package com.example.haidtracker.ui.profile;

import android.content.Context;
import android.util.Log;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.user.User;
import com.example.haidtracker.data.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileController {
    private static final String TAG = "ProfileController";
    
    private ApiService apiService;
    private AuthRepository authRepository;
    private Context context;

    public ProfileController(Context context) {
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiService.class);
        this.authRepository = new AuthRepository(context);
    }

    public void loadUserProfile(ProfileCallback callback) {
        String authToken = authRepository.getToken();
        
        if (authToken == null || authToken.isEmpty()) {
            callback.onError("No authentication token found");
            return;
        }

        Log.d(TAG, "Loading user profile...");

        // Use getUserProfile method from ApiService
        apiService.getUserProfile(authToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Log.d(TAG, "User profile loaded successfully: " + user.getEmail());
                    callback.onSuccess(user);
                } else {
                    String errorMsg = "Failed to load profile: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                    
                    // If unauthorized, clear token
                    if (response.code() == 401) {
                        authRepository.logout();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }

    public void updateUserProfile(User user, ProfileCallback callback) {
        String authToken = authRepository.getToken();
        
        if (authToken == null || authToken.isEmpty()) {
            callback.onError("No authentication token found");
            return;
        }

        Log.d(TAG, "Updating user profile...");

        apiService.updateUserProfile(authToken, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User updatedUser = response.body();
                    Log.d(TAG, "User profile updated successfully");
                    callback.onSuccess(updatedUser);
                } else {
                    String errorMsg = "Failed to update profile: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                    
                    // If unauthorized, clear token
                    if (response.code() == 401) {
                        authRepository.logout();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }

    public void logout() {
        authRepository.logout();
        Log.d(TAG, "User logged out");
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    public String getToken() {
        return authRepository.getToken();
    }

    public interface ProfileCallback {
        void onSuccess(User user);
        void onError(String error);
    }
}
