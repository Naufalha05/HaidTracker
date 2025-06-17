package com.example.haidtracker.ui.admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.haidtracker.R;

public class AdminRemindersActivity extends AppCompatActivity {

    private TextView tvRemindersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reminders);

        tvRemindersList = findViewById(R.id.tvRemindersList);

        // Example: Display a list of reminders (could be replaced with dynamic data)
        tvRemindersList.setText("List of Reminders will be shown here.");
    }
}
