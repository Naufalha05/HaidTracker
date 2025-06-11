package com.example.haidtracker.ui.profile;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.haidtracker.R;

public class ProfileViewBinder {
    public ImageButton btnBack, btnMore;
    public TextView txtTitle, txtName, txtEmail;
    public View cardPremium, cardMode, cardHealth, cardPreferences, cardResources;
    public View rowHealthNotes, rowPregnancyPrevention, rowCustomize, rowNotifications, rowSettings, rowConnect, rowGuide;

    public ProfileViewBinder(Activity activity) {
        btnBack = activity.findViewById(R.id.btnBack);
        btnMore = activity.findViewById(R.id.btnMore);
        txtTitle = activity.findViewById(R.id.txtTitle);
        txtName = activity.findViewById(R.id.txtName);
        txtEmail = activity.findViewById(R.id.txtEmail);
        cardPremium = activity.findViewById(R.id.cardPremium);
        cardMode = activity.findViewById(R.id.cardMode);
        cardHealth = activity.findViewById(R.id.cardHealth);
        cardPreferences = activity.findViewById(R.id.cardPreferences);
        cardResources = activity.findViewById(R.id.cardResources);
        rowHealthNotes = activity.findViewById(R.id.rowHealthNotes);
        rowPregnancyPrevention = activity.findViewById(R.id.rowPregnancyPrevention);
        rowCustomize = activity.findViewById(R.id.rowCustomize);
        rowNotifications = activity.findViewById(R.id.rowNotifications);
        rowSettings = activity.findViewById(R.id.rowSettings);
        rowConnect = activity.findViewById(R.id.rowConnect);
        rowGuide = activity.findViewById(R.id.rowGuide);
    }
}
