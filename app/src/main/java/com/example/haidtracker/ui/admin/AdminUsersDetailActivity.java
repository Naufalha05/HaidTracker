package com.example.haidtracker.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.haidtracker.R;

public class AdminUsersDetailActivity extends AppCompatActivity {

    private TextView tvUsersList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        tvUsersList = findViewById(R.id.tvUsersList);

        // Example: Display a list of users (could be replaced with dynamic data)
        tvUsersList.setText("List of Users will be shown here.");
    }
}
