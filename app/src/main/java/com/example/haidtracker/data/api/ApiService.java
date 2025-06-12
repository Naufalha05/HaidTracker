package com.example.haidtracker.data.api;

import com.example.haidtracker.data.model.auth.LoginRequest;
import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.data.model.auth.RegisterRequest;
import com.example.haidtracker.data.model.user.User;
import com.example.haidtracker.data.model.user.CreateUserRequest;
import com.example.haidtracker.data.model.user.UpdateUserRequest;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.data.model.cycle.CreateCycleRequest;
import com.example.haidtracker.data.model.cycle.UpdateCycleRequest;
import com.example.haidtracker.data.model.cycle.CycleStats;
import com.example.haidtracker.data.model.symptom.Symptom;
import com.example.haidtracker.data.model.symptom.CreateSymptomRequest;
import com.example.haidtracker.data.model.symptom.UpdateSymptomRequest;
import com.example.haidtracker.data.model.reminder.Reminder;
import com.example.haidtracker.data.model.reminder.CreateReminderRequest;
import com.example.haidtracker.data.model.reminder.UpdateReminderRequest;
import com.example.haidtracker.data.model.analytic.Analytic;
import com.example.haidtracker.data.model.analytic.CreateAnalyticRequest;
import com.example.haidtracker.data.model.analytic.UpdateAnalyticRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ==================== AUTH ENDPOINTS ====================

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    // ==================== USER ENDPOINTS ====================

    @POST("users")
    Call<User> createUser(
            @Header("Authorization") String token,
            @Body CreateUserRequest request
    );

    @GET("users/{id}")
    Call<User> getUserById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @PUT("users/{id}")
    Call<User> updateUserById(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body UpdateUserRequest request
    );

    @DELETE("users/{id}")
    Call<Void> deleteUserById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("users/profile")
    Call<User> getOwnProfile(
            @Header("Authorization") String token
    );

    @PUT("users/profile")
    Call<User> updateOwnProfile(
            @Header("Authorization") String token,
            @Body UpdateUserRequest request
    );

    // ==================== CYCLE ENDPOINTS ====================

    @POST("api/cycles")
    Call<Cycle> createCycle(
            @Header("Authorization") String token,
            @Body CreateCycleRequest request
    );

    @GET("api/cycles")
    Call<List<Cycle>> getAllCycles(
            @Header("Authorization") String token
    );

    @GET("api/cycles/{id}")
    Call<Cycle> getCycleById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @PUT("api/cycles/{id}")
    Call<Cycle> updateCycle(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body UpdateCycleRequest request
    );

    @DELETE("api/cycles/{id}")
    Call<Void> deleteCycle(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/cycles/search")
    Call<List<Cycle>> searchCycles(
            @Header("Authorization") String token,
            @Query("noteKeyword") String noteKeyword,
            @Query("startDate") String startDate
    );

    @GET("api/cycles/stats")
    Call<List<CycleStats>> getCycleStats(
            @Header("Authorization") String token
    );

    // ==================== SYMPTOM ENDPOINTS ====================

    @POST("api/symptoms")
    Call<Symptom> createSymptom(
            @Header("Authorization") String token,
            @Body CreateSymptomRequest request
    );

    @GET("api/symptoms")
    Call<List<Symptom>> getAllMySymptoms(
            @Header("Authorization") String token
    );

    @GET("api/symptoms/{id}")
    Call<Symptom> getSymptomById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @PUT("api/symptoms/{id}")
    Call<Symptom> updateSymptom(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body UpdateSymptomRequest request
    );

    @DELETE("api/symptoms/{id}")
    Call<Void> deleteSymptom(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/symptoms/user/{userId}")
    Call<List<Symptom>> getSymptomsByUser(
            @Header("Authorization") String token,
            @Path("userId") int userId
    );

    // ==================== REMINDER ENDPOINTS ====================

    @GET("api/reminders/all")
    Call<List<Reminder>> getAllReminders(
            @Header("Authorization") String token
    );

    @GET("api/reminders")
    Call<List<Reminder>> getMyReminders(
            @Header("Authorization") String token
    );

    @POST("api/reminders")
    Call<Reminder> createReminder(
            @Header("Authorization") String token,
            @Body CreateReminderRequest request
    );

    @GET("api/reminders/{id}")
    Call<Reminder> getReminderById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @PUT("api/reminders/{id}")
    Call<Reminder> updateReminder(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body UpdateReminderRequest request
    );

    @DELETE("api/reminders/{id}")
    Call<Void> deleteReminder(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    // ==================== ANALYTIC ENDPOINTS ====================

    @GET("api/analytics/all")
    Call<List<Analytic>> getAllAnalytics(
            @Header("Authorization") String token
    );

    @GET("api/analytics")
    Call<List<Analytic>> getMyAnalytics(
            @Header("Authorization") String token
    );

    @POST("api/analytics")
    Call<Analytic> createAnalytic(
            @Header("Authorization") String token,
            @Body CreateAnalyticRequest request
    );

    @GET("api/analytics/{id}")
    Call<Analytic> getAnalyticById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @PUT("api/analytics/{id}")
    Call<Analytic> updateAnalytic(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body UpdateAnalyticRequest request
    );

    @DELETE("api/analytics/{id}")
    Call<Void> deleteAnalytic(
            @Header("Authorization") String token,
            @Path("id") int id
    );
}