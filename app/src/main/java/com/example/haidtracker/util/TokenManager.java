package com.example.haidtracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String TAG = "TokenManager";

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_AUTH_TOKEN, "");
        
        // Log token for debugging (remove in production)
        Log.d(TAG, "Retrieved token: " + (token.isEmpty() ? "EMPTY" : "NOT EMPTY"));
        
        // Ensure token has "Bearer " prefix
        if (!token.isEmpty() && !token.startsWith("Bearer ")) {
            return "Bearer " + token;
        }
        return token;
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        // Remove "Bearer " prefix if present when saving
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // Log token for debugging (remove in production)
        Log.d(TAG, "Saving token: " + (token != null && !token.isEmpty() ? "NOT EMPTY" : "EMPTY"));
        
        editor.putString(KEY_AUTH_TOKEN, token);
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
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_AUTH_TOKEN, "");
        return !token.isEmpty();
    }
}
