package com.example.haidtracker.data.api;

import com.example.haidtracker.data.model.analytics.Analytics;
import com.example.haidtracker.data.model.cycle.CreateCycleRequest;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.data.model.cycle.UpdateCycleRequest;
import com.example.haidtracker.data.model.reminder.CreateReminderRequest;
import com.example.haidtracker.data.model.reminder.Reminder;
import com.example.haidtracker.data.model.reminder.UpdateReminderRequest;
import com.example.haidtracker.data.model.symptom.CreateSymptomRequest;
import com.example.haidtracker.data.model.symptom.Symptom;
import com.example.haidtracker.data.model.symptom.UpdateSymptomRequest;
import com.example.haidtracker.data.model.user.CreateUserRequest;
import com.example.haidtracker.data.model.user.User;

// Import dari auth package
import com.example.haidtracker.data.model.auth.LoginRequest;
import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.data.model.auth.RegisterRequest;
import com.example.haidtracker.data.model.auth.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Auth endpoints - ONLY these for authentication
    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // User endpoints
    @GET("api/users/profile")
    Call<User> getUserProfile(@Header("Authorization") String token);

    @PUT("api/users/profile")
    Call<User> updateUserProfile(@Header("Authorization") String token, @Body User user);

    // Admin endpoints (simplified)
    @GET("api/admin/users")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);

    @DELETE("api/admin/users/{id}")
    Call<Void> deleteUser(@Header("Authorization") String token, @Path("id") int id);

    // Cycle endpoints
    @GET("api/cycles")
    Call<List<Cycle>> getAllCycles(@Header("Authorization") String token);

    @GET("api/cycles/my")
    Call<List<Cycle>> getMyCycles(@Header("Authorization") String token);

    @GET("api/cycles/all")
    Call<List<Cycle>> getAllCyclesAdmin(@Header("Authorization") String token);

    @POST("api/cycles")
    Call<Cycle> createCycle(@Header("Authorization") String token, @Body CreateCycleRequest request);

    @PUT("api/cycles/{id}")
    Call<Cycle> updateCycle(@Header("Authorization") String token, @Path("id") int id, @Body UpdateCycleRequest request);

    @DELETE("api/cycles/{id}")
    Call<Void> deleteCycle(@Header("Authorization") String token, @Path("id") int id);

    // Reminder endpoints
    @GET("api/reminders")
    Call<List<Reminder>> getAllReminders(@Header("Authorization") String token);
    
    @GET("api/reminders")
    Call<List<Reminder>> getMyReminders(@Header("Authorization") String token);
    
    @GET("api/reminders/all")
    Call<List<Reminder>> getAllRemindersAdmin(@Header("Authorization") String token);
    
    @POST("api/reminders")
    Call<Reminder> createReminder(@Header("Authorization") String token, @Body CreateReminderRequest request);
    
    @PUT("api/reminders/{id}")
    Call<Reminder> updateReminder(@Header("Authorization") String token, @Path("id") int id, @Body UpdateReminderRequest request);
    
    @DELETE("api/reminders/{id}")
    Call<Void> deleteReminder(@Header("Authorization") String token, @Path("id") int id);

    // Analytics endpoints
    @GET("api/analytics")
    Call<Analytics> getAnalytics(@Header("Authorization") String token);
    
    @GET("api/analytics/detailed")
    Call<Analytics> getDetailedAnalytics(@Header("Authorization") String token);
    
    @GET("api/analytics/all")
    Call<List<Analytics>> getAllAnalytics(@Header("Authorization") String token);
    
    @GET("api/admin/analytics")
    Call<List<Analytics>> getAdminAnalytics(@Header("Authorization") String token);
    
    // Symptom endpoints
    @GET("api/symptoms")
    Call<List<Symptom>> getAllSymptoms(@Header("Authorization") String token);
    
    @POST("api/symptoms")
    Call<Symptom> createSymptom(@Header("Authorization") String token, @Body CreateSymptomRequest request);
    
    @PUT("api/symptoms/{id}")
    Call<Symptom> updateSymptom(@Header("Authorization") String token, @Path("id") int id, @Body UpdateSymptomRequest request);
    
    @DELETE("api/symptoms/{id}")
    Call<Void> deleteSymptom(@Header("Authorization") String token, @Path("id") int id);
}
