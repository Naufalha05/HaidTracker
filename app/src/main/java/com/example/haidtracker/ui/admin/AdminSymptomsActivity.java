package com.example.haidtracker.ui.admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.haidtracker.R;

public class AdminSymptomsActivity extends AppCompatActivity {

    private TextView tvSymptomsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_symptoms);

        tvSymptomsList = findViewById(R.id.tvSymptomsList);

        // Example: Display a list of symptoms (could be replaced with dynamic data)
        tvSymptomsList.setText("List of Symptoms will be shown here.");
    }
}
