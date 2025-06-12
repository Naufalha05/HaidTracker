package com.example.haidtracker.ui.reminder;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ReminderViewModel extends AndroidViewModel {

    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String KEY_REMINDER_ENABLED = "reminder_enabled";
    private static final String KEY_REMINDER_HOUR = "reminder_hour";
    private static final String KEY_REMINDER_MINUTE = "reminder_minute";
    private static final String KEY_DAYS_BEFORE = "days_before";
    private static final String KEY_NOTIFICATION_TYPE = "notification_type";

    private SharedPreferences sharedPreferences;

    private MutableLiveData<Boolean> isReminderEnabled = new MutableLiveData<>();
    private MutableLiveData<String> reminderTime = new MutableLiveData<>();
    private MutableLiveData<Integer> daysBefore = new MutableLiveData<>();
    private MutableLiveData<String> notificationType = new MutableLiveData<>();

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadSettings();
    }

    private void loadSettings() {
        isReminderEnabled.setValue(sharedPreferences.getBoolean(KEY_REMINDER_ENABLED, false));

        int hour = sharedPreferences.getInt(KEY_REMINDER_HOUR, 8);
        int minute = sharedPreferences.getInt(KEY_REMINDER_MINUTE, 0);
        String timeFormat = String.format("%02d:%02d %s",
                hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour),
                minute,
                hour >= 12 ? "PM" : "AM");
        reminderTime.setValue(timeFormat);

        daysBefore.setValue(sharedPreferences.getInt(KEY_DAYS_BEFORE, 3));
        notificationType.setValue(sharedPreferences.getString(KEY_NOTIFICATION_TYPE, "Suara & Getar"));
    }

    // Getters for LiveData
    public LiveData<Boolean> getIsReminderEnabled() {
        return isReminderEnabled;
    }

    public LiveData<String> getReminderTime() {
        return reminderTime;
    }

    public LiveData<Integer> getDaysBefore() {
        return daysBefore;
    }

    public LiveData<String> getNotificationType() {
        return notificationType;
    }

    // Setters
    public void setReminderEnabled(boolean enabled) {
        isReminderEnabled.setValue(enabled);
        sharedPreferences.edit().putBoolean(KEY_REMINDER_ENABLED, enabled).apply();
    }

    public void setReminderTime(int hour, int minute) {
        String timeFormat = String.format("%02d:%02d %s",
                hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour),
                minute,
                hour >= 12 ? "PM" : "AM");
        reminderTime.setValue(timeFormat);

        sharedPreferences.edit()
                .putInt(KEY_REMINDER_HOUR, hour)
                .putInt(KEY_REMINDER_MINUTE, minute)
                .apply();
    }

    public void setDaysBefore(int days) {
        daysBefore.setValue(days);
        sharedPreferences.edit().putInt(KEY_DAYS_BEFORE, days).apply();
    }

    public void setNotificationType(String type) {
        notificationType.setValue(type);
        sharedPreferences.edit().putString(KEY_NOTIFICATION_TYPE, type).apply();
    }

    public int getReminderHour() {
        return sharedPreferences.getInt(KEY_REMINDER_HOUR, 8);
    }

    public int getReminderMinute() {
        return sharedPreferences.getInt(KEY_REMINDER_MINUTE, 0);
    }

    public int getDaysBeforeValue() {
        return sharedPreferences.getInt(KEY_DAYS_BEFORE, 3);
    }
}