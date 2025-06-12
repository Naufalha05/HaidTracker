package com.example.haidtracker.ui.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.haidtracker.R;


import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "SiklusReminderChannel";
    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String KEY_REMINDER_ENABLED = "reminder_enabled";
    private static final String KEY_REMINDER_HOUR = "reminder_hour";
    private static final String KEY_REMINDER_MINUTE = "reminder_minute";
    private static final String KEY_DAYS_BEFORE = "days_before";
    private static final String KEY_NOTIFICATION_TYPE = "notification_type";

    private SwitchCompat switchReminder;
    private TextView tvReminderTime;
    private TextView tvDaysBefore;
    private TextView tvNotificationType;
    private LinearLayout layoutReminderTime;
    private LinearLayout layoutDaysBefore;
    private LinearLayout layoutNotificationType;
    private ImageView ivClose;

    // Bottom navigation
    private LinearLayout btnRefresh;
    private LinearLayout btnCalendar;
    private LinearLayout btnAdd;
    private LinearLayout btnAnalytics;
    private LinearLayout btnProfile;

    private SharedPreferences sharedPreferences;
    private int selectedHour = 8;
    private int selectedMinute = 0;
    private int selectedDaysBefore = 3;
    private String selectedNotificationType = "Suara & Getar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        initViews();
        setupSharedPreferences();
        loadSettings();
        setupListeners();
        createNotificationChannel();
    }

    private void initViews() {
        switchReminder = findViewById(R.id.switch_reminder);
        tvReminderTime = findViewById(R.id.tv_reminder_time);
        tvDaysBefore = findViewById(R.id.tv_days_before);
        tvNotificationType = findViewById(R.id.tv_notification_type);
        layoutReminderTime = findViewById(R.id.layout_reminder_time);
        layoutDaysBefore = findViewById(R.id.layout_days_before);
        layoutNotificationType = findViewById(R.id.layout_notification_type);
        ivClose = findViewById(R.id.iv_close);

    }

    private void setupSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private void loadSettings() {
        boolean isReminderEnabled = sharedPreferences.getBoolean(KEY_REMINDER_ENABLED, false);
        selectedHour = sharedPreferences.getInt(KEY_REMINDER_HOUR, 8);
        selectedMinute = sharedPreferences.getInt(KEY_REMINDER_MINUTE, 0);
        selectedDaysBefore = sharedPreferences.getInt(KEY_DAYS_BEFORE, 3);
        selectedNotificationType = sharedPreferences.getString(KEY_NOTIFICATION_TYPE, "Suara & Getar");

        switchReminder.setChecked(isReminderEnabled);
        updateTimeDisplay();
        updateDaysBeforeDisplay();
        updateNotificationTypeDisplay();
        updateSettingsVisibility(isReminderEnabled);
    }

    private void setupListeners() {
        ivClose.setOnClickListener(v -> finish());

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSettingsVisibility(isChecked);
            saveReminderEnabled(isChecked);

            if (isChecked) {
                scheduleReminder();
                Toast.makeText(this, "Pengingat diaktifkan", Toast.LENGTH_SHORT).show();
            } else {
                cancelReminder();
                Toast.makeText(this, "Pengingat dinonaktifkan", Toast.LENGTH_SHORT).show();
            }
        });

        layoutReminderTime.setOnClickListener(v -> showTimePickerDialog());
        layoutDaysBefore.setOnClickListener(v -> showDaysBeforeDialog());
        layoutNotificationType.setOnClickListener(v -> showNotificationTypeDialog());

    }


    private void updateSettingsVisibility(boolean isEnabled) {
        int visibility = isEnabled ? View.VISIBLE : View.GONE;
        layoutReminderTime.setVisibility(visibility);
        layoutDaysBefore.setVisibility(visibility);
        layoutNotificationType.setVisibility(visibility);
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    updateTimeDisplay();
                    saveReminderTime();
                    if (switchReminder.isChecked()) {
                        scheduleReminder();
                    }
                }, selectedHour, selectedMinute, false);

        timePickerDialog.show();
    }

    private void showDaysBeforeDialog() {
        String[] options = {"1 hari sebelum", "2 hari sebelum", "3 hari sebelum",
                "5 hari sebelum", "7 hari sebelum"};
        int[] values = {1, 2, 3, 5, 7};

        int selectedIndex = 2; // Default to 3 days
        for (int i = 0; i < values.length; i++) {
            if (values[i] == selectedDaysBefore) {
                selectedIndex = i;
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Waktu Pengingat");
        builder.setSingleChoiceItems(options, selectedIndex, (dialog, which) -> {
            selectedDaysBefore = values[which];
            updateDaysBeforeDisplay();
            saveDaysBefore();
            if (switchReminder.isChecked()) {
                scheduleReminder();
            }
            dialog.dismiss();
        });
        builder.show();
    }

    private void showNotificationTypeDialog() {
        String[] options = {"Hanya Suara", "Hanya Getar", "Suara & Getar", "Hening"};

        int selectedIndex = 2; // Default to "Suara & Getar"
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selectedNotificationType)) {
                selectedIndex = i;
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Jenis Notifikasi");
        builder.setSingleChoiceItems(options, selectedIndex, (dialog, which) -> {
            selectedNotificationType = options[which];
            updateNotificationTypeDisplay();
            saveNotificationType();
            dialog.dismiss();
        });
        builder.show();
    }

    private void updateTimeDisplay() {
        String timeFormat = String.format("%02d:%02d %s",
                selectedHour > 12 ? selectedHour - 12 : (selectedHour == 0 ? 12 : selectedHour),
                selectedMinute,
                selectedHour >= 12 ? "PM" : "AM");
        tvReminderTime.setText(timeFormat);
    }

    private void updateDaysBeforeDisplay() {
        tvDaysBefore.setText(selectedDaysBefore + " hari sebelum siklus");
    }

    private void updateNotificationTypeDisplay() {
        tvNotificationType.setText(selectedNotificationType);
    }

    private void saveReminderEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_REMINDER_ENABLED, enabled).apply();
    }

    private void saveReminderTime() {
        sharedPreferences.edit()
                .putInt(KEY_REMINDER_HOUR, selectedHour)
                .putInt(KEY_REMINDER_MINUTE, selectedMinute)
                .apply();
    }

    private void saveDaysBefore() {
        sharedPreferences.edit().putInt(KEY_DAYS_BEFORE, selectedDaysBefore).apply();
    }

    private void saveNotificationType() {
        sharedPreferences.edit().putString(KEY_NOTIFICATION_TYPE, selectedNotificationType).apply();
    }

    private void scheduleReminder() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        calendar.set(Calendar.SECOND, 0);

        // If the time has already passed today, schedule for tomorrow
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Subtract the days before from the scheduled time
        calendar.add(Calendar.DAY_OF_MONTH, -selectedDaysBefore);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void cancelReminder() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Siklus Reminder";
            String description = "Channel untuk pengingat siklus menstruasi";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}