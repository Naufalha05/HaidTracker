package com.example.haidtracker.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ProfileController {
    private final Activity activity;
    private final ProfileViewBinder view;

    public ProfileController(Activity activity, ProfileViewBinder view) {
        this.activity = activity;
        this.view = view;
    }

    public void setUp() {
        setUserData("Indah Purnama Sari", "indahips409@gmail.com");
        setClickListeners();
    }

    private void setUserData(String name, String email) {
        if (view.txtName != null) view.txtName.setText(name);
        if (view.txtEmail != null) view.txtEmail.setText(email);
        if (view.txtTitle != null) view.txtTitle.setText("Menu Lainnya");
    }

    private void setClickListeners() {
        if (view.btnBack != null) {
            view.btnBack.setOnClickListener(v -> activity.onBackPressed());
        }
        if (view.btnMore != null) {
            view.btnMore.setOnClickListener(v -> showToast("Opening profile details..."));
        }
        if (view.cardPremium != null) {
            view.cardPremium.setOnClickListener(v -> showToast("Opening Clue Plus features..."));
        }
        if (view.cardMode != null) {
            view.cardMode.setOnClickListener(v -> showToast("Opening mode settings..."));
        }
        if (view.rowHealthNotes != null) {
            view.rowHealthNotes.setOnClickListener(v -> showToast("Opening health notes..."));
        }
        if (view.rowPregnancyPrevention != null) {
            view.rowPregnancyPrevention.setOnClickListener(v -> showToast("Opening pregnancy prevention settings..."));
        }
        if (view.rowCustomize != null) {
            view.rowCustomize.setOnClickListener(v -> showToast("Opening monitoring customization..."));
        }
        if (view.rowNotifications != null) {
            view.rowNotifications.setOnClickListener(v -> showToast("Opening notification settings..."));
        }
        if (view.rowSettings != null) {
            view.rowSettings.setOnClickListener(v -> showToast("Opening app settings..."));
        }
        if (view.rowConnect != null) {
            view.rowConnect.setOnClickListener(v -> showToast("Opening Clue Connect..."));
        }
        if (view.rowGuide != null) {
            view.rowGuide.setOnClickListener(v -> showToast("Opening monitoring guide..."));
        }
    }

    public void refreshUserData() {
        // Bisa ambil dari SharedPreferences atau API
        // Contoh simulasi update
        setUserData("Indah Purnama Sari", "indahips409@gmail.com");
    }

    private void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
