package com.example.haidtracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String TAG = "TokenManager";

    public static String getToken(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null");
            return "";
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_AUTH_TOKEN, "");

        // Log token for debugging (remove in production)
        Log.d(TAG, "Retrieved token: " + (token.isEmpty() ? "EMPTY" : "NOT EMPTY"));

        // Return empty string if token is null or empty
        if (token == null || token.trim().isEmpty()) {
            Log.w(TAG, "Token is null or empty");
            return "";
        }

        // Ensure token has "Bearer " prefix
        if (!token.startsWith("Bearer ")) {
            String bearerToken = "Bearer " + token.trim();
            Log.d(TAG, "Added Bearer prefix to token");
            return bearerToken;
        }

        return token.trim();
    }

    public static void saveToken(Context context, String token) {
        if (context == null) {
            Log.e(TAG, "Context is null, cannot save token");
            return;
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Handle null or empty token
        if (token == null || token.trim().isEmpty()) {
            Log.w(TAG, "Token is null or empty, clearing stored token");
            editor.remove(KEY_AUTH_TOKEN);
            editor.apply();
            return;
        }

        // Remove "Bearer " prefix if present when saving
        String cleanToken = token.trim();
        if (cleanToken.startsWith("Bearer ")) {
            cleanToken = cleanToken.substring(7).trim();
        }

        // Validate token is not empty after cleaning
        if (cleanToken.isEmpty()) {
            Log.w(TAG, "Token is empty after cleaning, clearing stored token");
            editor.remove(KEY_AUTH_TOKEN);
            editor.apply();
            return;
        }

        // Log token for debugging (remove in production)
        Log.d(TAG, "Saving token: NOT EMPTY");

        editor.putString(KEY_AUTH_TOKEN, cleanToken);
        editor.apply();
    }

    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();
        Log.d(TAG, "Token cleared");
    }
    
    public static boolean hasToken(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null");
            return false;
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_AUTH_TOKEN, "");
        boolean hasValidToken = token != null && !token.trim().isEmpty();

        Log.d(TAG, "Has valid token: " + hasValidToken);
        return hasValidToken;
    }
}
