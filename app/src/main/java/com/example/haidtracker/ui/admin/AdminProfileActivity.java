package com.example.haidtracker.ui.admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.haidtracker.R;

public class AdminProfileActivity extends AppCompatActivity {

    private TextView tvProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        tvProfileName = findViewById(R.id.tvProfileName);

        // Example of updating the profile name (could be replaced with dynamic data)
        tvProfileName.setText("Admin Profile");
    }
}
