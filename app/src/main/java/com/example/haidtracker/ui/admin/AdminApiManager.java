package com.example.haidtracker.ui.admin;

import android.content.Context;
import android.util.Log;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.data.model.user.User;
import com.example.haidtracker.util.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminApiManager {
    
    private static final String TAG = "AdminApiManager";
    private ApiService apiService;
    private String authToken;

    public AdminApiManager(Context context) {
        this.apiService = ApiClient.getClient().create(ApiService.class);
        this.authToken = TokenManager.getToken(context);
    }

    // Cycles Management
    public void getAllCycles(CyclesListCallback callback) {
        Log.d(TAG, "Fetching all cycles");
        
        apiService.getAllCyclesAdmin(authToken).enqueue(new Callback<List<Cycle>>() {
            @Override
            public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Successfully fetched " + response.body().size() + " cycles");
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to fetch cycles: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<Cycle>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }

    public void deleteCycle(int cycleId, AdminCallback<Void> callback) {
        Log.d(TAG, "Deleting cycle ID: " + cycleId);
        
        apiService.deleteCycle(authToken, cycleId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Successfully deleted cycle ID: " + cycleId);
                    callback.onSuccess(null);
                } else {
                    String errorMsg = "Failed to delete cycle: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }

    // User Management
    public void getAllUsers(UsersListCallback callback) {
        Log.d(TAG, "Fetching all users");
        
        apiService.getAllUsers(authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Successfully fetched " + response.body().size() + " users");
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to fetch users: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }

    public void deleteUser(int userId, AdminCallback<Void> callback) {
        Log.d(TAG, "Deleting user ID: " + userId);
        
        apiService.deleteUser(authToken, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Successfully deleted user ID: " + userId);
                    callback.onSuccess(null);
                } else {
                    String errorMsg = "Failed to delete user: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }

    // Callback interfaces
    public interface AdminCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public interface CyclesListCallback {
        void onSuccess(List<Cycle> cycles);
        void onError(String error);
    }

    public interface UsersListCallback {
        void onSuccess(List<User> users);
        void onError(String error);
    }
}
