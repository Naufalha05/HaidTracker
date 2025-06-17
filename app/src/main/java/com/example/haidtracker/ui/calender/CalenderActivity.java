package com.example.haidtracker.ui.calender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.haidtracker.R;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.data.model.reminder.Reminder;
import com.example.haidtracker.data.model.reminder.CreateReminderRequest;
import com.example.haidtracker.ui.analytics.AnalyticsActivity;
import com.example.haidtracker.ui.profile.ProfileActivity;
import com.example.haidtracker.ui.siklus.SiklusActivity;
import com.example.haidtracker.ui.symptom.SymptomActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalenderActivity extends AppCompatActivity {

    private static final String TAG = "CalenderActivity";
    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String TOKEN_KEY = "auth_token";

    // UI Components
    private TextView monthYearText;
    private GridLayout calendarGrid;
    private ImageButton prevMonth, nextMonth, backButton;
    private ProgressBar loadingIndicator;
    private FloatingActionButton fabAddReminder;

    // Data
    private Calendar currentCalendar;
    private ApiService apiService;
    private String authToken;
    private List<Cycle> cycles = new ArrayList<>();
    private List<Reminder> reminders = new ArrayList<>();
    private Set<String> cycleDates = new HashSet<>();
    private Set<String> reminderDates = new HashSet<>();

    // Date formatters
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat monthYearFormatter = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calender);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        setupClickListeners();
        initializeCalendar();
        fetchDataFromApi();
    }

    private void initializeComponents() {
        monthYearText = findViewById(R.id.monthYearText);
        calendarGrid = findViewById(R.id.calendarGrid);
        prevMonth = findViewById(R.id.prevMonth);
        nextMonth = findViewById(R.id.nextMonth);
        backButton = findViewById(R.id.backButton);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        fabAddReminder = findViewById(R.id.fabAddReminder);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Get auth token
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        authToken = "Bearer " + prefs.getString(TOKEN_KEY, "");

        // Initialize calendar to current month
        currentCalendar = Calendar.getInstance();
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CalenderActivity.this, SiklusActivity.class);
            startActivity(intent);
            finish();
        });

        prevMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateCalendarDisplay();
        });

        nextMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateCalendarDisplay();
        });
        
        // Add reminder button click listener
        fabAddReminder.setOnClickListener(v -> {
            showAddReminderDialog();
        });
        
        // Navigation untuk bottom navbar (jika ada)
        // Tambahkan ini jika CalenderActivity memiliki bottom navigation
        findViewById(R.id.menu_siklus).setOnClickListener(v -> 
            startActivity(new Intent(this, SiklusActivity.class)));
        findViewById(R.id.analisis).setOnClickListener(v -> 
            startActivity(new Intent(this, AnalyticsActivity.class)));
        findViewById(R.id.content).setOnClickListener(v -> 
            startActivity(new Intent(this, SymptomActivity.class)));
        findViewById(R.id.menu_icon).setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void initializeCalendar() {
        updateCalendarDisplay();
    }

    private void fetchDataFromApi() {
        showLoading(true);

        // Fetch cycles and reminders concurrently
        fetchCycles();
        fetchReminders();
    }

    private void fetchCycles() {
        apiService.getAllCycles(authToken).enqueue(new Callback<List<Cycle>>() {
            @Override
            public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cycles = response.body();
                    processCycleData();
                    updateCalendarDisplay();
                } else {
                    Log.e(TAG, "Failed to fetch cycles: " + response.code());
                    Toast.makeText(CalenderActivity.this, "Gagal memuat data siklus", Toast.LENGTH_SHORT).show();
                }
                checkDataLoadingComplete();
            }

            @Override
            public void onFailure(Call<List<Cycle>> call, Throwable t) {
                Log.e(TAG, "Error fetching cycles", t);
                Toast.makeText(CalenderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                checkDataLoadingComplete();
            }
        });
    }

    private void fetchReminders() {
        apiService.getMyReminders(authToken).enqueue(new Callback<List<Reminder>>() {
            @Override
            public void onResponse(Call<List<Reminder>> call, Response<List<Reminder>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reminders = response.body();
                    processReminderData();
                    updateCalendarDisplay();
                } else {
                    Log.e(TAG, "Failed to fetch reminders: " + response.code());
                    Toast.makeText(CalenderActivity.this, "Gagal memuat data reminder", Toast.LENGTH_SHORT).show();
                }
                checkDataLoadingComplete();
            }

            @Override
            public void onFailure(Call<List<Reminder>> call, Throwable t) {
                Log.e(TAG, "Error fetching reminders", t);
                Toast.makeText(CalenderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                checkDataLoadingComplete();
            }
        });
    }

    private void processCycleData() {
        cycleDates.clear();
        for (Cycle cycle : cycles) {
            try {
                if (cycle.getStartDate() != null) {
                    Date startDate = cycle.getStartDate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);

                    // Add cycle duration days (assuming 5-7 days cycle)
                    int cycleDuration = 7; // Default cycle duration
                    for (int i = 0; i < cycleDuration; i++) {
                        cycleDates.add(dateFormatter.format(cal.getTime()));
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error processing cycle data", e);
            }
        }
    }

    private void processReminderData() {
        reminderDates.clear();
        for (Reminder reminder : reminders) {
            try {
                if (reminder.getDate() != null) {
                    reminderDates.add(dateFormatter.format(reminder.getDate()));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error processing reminder data", e);
            }
        }
    }

    private void checkDataLoadingComplete() {
        showLoading(false);
    }

    private void showLoading(boolean show) {
        loadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void updateCalendarDisplay() {
        monthYearText.setText(monthYearFormatter.format(currentCalendar.getTime()));
        calendarGrid.removeAllViews();
        generateCalendarForMonth();
    }

    private void generateCalendarForMonth() {
        Calendar cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int startOffset = (firstDayOfWeek == Calendar.SUNDAY) ? 6 : firstDayOfWeek - 2;

        // Add previous month's trailing days
        Calendar prevMonth = (Calendar) cal.clone();
        prevMonth.add(Calendar.MONTH, -1);
        int daysInPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = startOffset - 1; i >= 0; i--) {
            addCalendarDay(daysInPrevMonth - i, true, prevMonth);
        }

        // Add current month days
        for (int day = 1; day <= daysInMonth; day++) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            addCalendarDay(day, false, cal);
        }

        // Add next month's leading days to fill the grid
        Calendar nextMonth = (Calendar) cal.clone();
        nextMonth.add(Calendar.MONTH, 1);
        int totalCells = calendarGrid.getChildCount();
        int remainingCells = 42 - totalCells;

        for (int day = 1; day <= remainingCells && day <= 14; day++) {
            nextMonth.set(Calendar.DAY_OF_MONTH, day);
            addCalendarDay(day, true, nextMonth);
        }
    }

    private void addCalendarDay(int day, boolean isOtherMonth, Calendar cal) {
        CardView cardView = new CardView(this);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = 0;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        layoutParams.setMargins(4, 4, 4, 4);
        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(12);
        cardView.setCardElevation(2);

        TextView dayText = new TextView(this);
        dayText.setText(String.valueOf(day));
        dayText.setTextSize(14);
        dayText.setGravity(android.view.Gravity.CENTER);
        dayText.setPadding(16, 16, 16, 16);
        dayText.setMinHeight(120);

        String dateStr = dateFormatter.format(cal.getTime());

        if (isOtherMonth) {
            cardView.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            dayText.setTextColor(Color.parseColor("#BDBDBD"));
        } else {
            boolean hasCycle = cycleDates.contains(dateStr);
            boolean hasReminder = reminderDates.contains(dateStr);

            if (hasCycle && hasReminder) {
                cardView.setCardBackgroundColor(Color.parseColor("#ff66b1"));
                dayText.setTextColor(Color.WHITE);
                addIndicator(dayText, "#4CAF50", true);
            } else if (hasCycle) {
                cardView.setCardBackgroundColor(Color.parseColor("#ff66b1"));
                dayText.setTextColor(Color.WHITE);
            } else if (hasReminder) {
                cardView.setCardBackgroundColor(Color.parseColor("#E8F5E8"));
                dayText.setTextColor(Color.parseColor("#2E7D32"));
                addIndicator(dayText, "#4CAF50", false);
            } else {
                cardView.setCardBackgroundColor(Color.WHITE);
                dayText.setTextColor(Color.parseColor("#212121"));
            }

            // Highlight today with border
            Calendar today = Calendar.getInstance();
            if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    cal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                GradientDrawable border = new GradientDrawable();
                border.setColor(cardView.getCardBackgroundColor().getDefaultColor());
                border.setCornerRadius(12);
                border.setStroke(4, Color.parseColor("#ff66b1"));
                cardView.setBackground(border);
            }
        }

        cardView.addView(dayText);

        final Calendar clickedDate = (Calendar) cal.clone();
        cardView.setOnClickListener(v -> {
            if (!isOtherMonth) {
                onDateClicked(clickedDate);
            }
        });

        calendarGrid.addView(cardView);
    }

    private void addIndicator(TextView dayText, String color, boolean isSmall) {
        GradientDrawable indicator = new GradientDrawable();
        indicator.setShape(GradientDrawable.OVAL);
        indicator.setColor(Color.parseColor(color));
        dayText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.circle_shape);
    }

    private void onDateClicked(Calendar date) {
        String dateStr = dateFormatter.format(date.getTime());

        StringBuilder info = new StringBuilder();
        info.append("Tanggal: ").append(new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID")).format(date.getTime()));

        if (cycleDates.contains(dateStr)) {
            info.append("\n• Hari siklus menstruasi");
        }

        if (reminderDates.contains(dateStr)) {
            info.append("\n• Ada reminder");
            for (Reminder reminder : reminders) {
                if (dateFormatter.format(reminder.getDate()).equals(dateStr)) {
                    info.append("\n  - ").append(reminder.getTitle());
                }
            }
        }

        if (!cycleDates.contains(dateStr) && !reminderDates.contains(dateStr)) {
            info.append("\n• Tidak ada data khusus");
        }

        Toast.makeText(this, info.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CalenderActivity.this, SiklusActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
    
    private void showAddReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_reminder, null);
        builder.setView(dialogView);
        
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etRemindAt = dialogView.findViewById(R.id.etRemindAt);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        
        AlertDialog dialog = builder.create();
        
        // Set date time picker for reminder date
        etRemindAt.setOnClickListener(v -> {
            showDateTimePicker(etRemindAt);
        });
        
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String remindAt = etRemindAt.getText().toString().trim();
            
            if (title.isEmpty()) {
                Toast.makeText(CalenderActivity.this, "Judul reminder wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (remindAt.isEmpty()) {
                Toast.makeText(CalenderActivity.this, "Waktu reminder wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Create reminder request
            createReminder(title, description, remindAt);
            dialog.dismiss();
        });
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }
    
    private void showDateTimePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        
        // Date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    
                    // Time picker dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                                
                                // Format date time
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                                editText.setText(sdf.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    
    private void createReminder(String title, String description, String remindAt) {
        // Create reminder request object
        CreateReminderRequest request = new CreateReminderRequest(title, description, remindAt);
        
        // Show loading
        loadingIndicator.setVisibility(View.VISIBLE);
        
        // Make API call
        apiService.createReminder(authToken, request).enqueue(new Callback<Reminder>() {
            @Override
            public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                loadingIndicator.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CalenderActivity.this, "Reminder berhasil dibuat", Toast.LENGTH_SHORT).show();
                    // Refresh reminders
                    fetchReminders();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? 
                                response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Error creating reminder: " + errorBody);
                        Toast.makeText(CalenderActivity.this, 
                                "Gagal membuat reminder: " + response.code(), 
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                }
            }
            
            @Override
            public void onFailure(Call<Reminder> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Log.e(TAG, "Network error creating reminder", t);
                Toast.makeText(CalenderActivity.this, 
                        "Error: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
