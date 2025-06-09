package com.example.haidtracker.ui.profile;

import android.app.Activity;
import android.os.Bundle;

import com.example.haidtracker.R; // Pastikan ini ada

public class ProfileActivity extends Activity {

    private ProfileViewBinder viewBinder;
    private ProfileController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Sesuaikan dengan nama file XML-mu

        viewBinder = new ProfileViewBinder(this);
        controller = new ProfileController(this, viewBinder);
        controller.setUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.refreshUserData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
