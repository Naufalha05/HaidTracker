package Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {

    // UI Components
    private ImageButton btnBack;
    private TextView txtTitle;
    private TextView txtName;
    private TextView txtEmail;
    private ImageButton btnMore;

    // Cards and Sections
    private View cardPremium;
    private View cardMode;
    private View cardHealth;
    private View cardPreferences;
    private View cardResources;

    // Menu Items
    private View rowHealthNotes;
    private View rowPregnancyPrevention;
    private View rowCustomize;
    private View rowNotifications;
    private View rowSettings;
    private View rowConnect;
    private View rowGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pastikan nama layout sesuai dengan file XML Anda
        // Jika file XML bernama activity_profile.xml, gunakan:
        setContentView(getResources().getIdentifier("activity_profile", "layout", getPackageName()));

        // Atau jika nama file berbeda, ganti "activity_profile" dengan nama yang sesuai
        // Contoh: jika file bernama profile_layout.xml, gunakan "profile_layout"

        initializeViews();
        setUpClickListeners();
        setUpUserData();
    }

    private void initializeViews() {
        btnBack = (ImageButton) findViewById(getResources().getIdentifier("btnBack", "id", getPackageName()));
        txtTitle = (TextView) findViewById(getResources().getIdentifier("txtTitle", "id", getPackageName()));
        txtName = (TextView) findViewById(getResources().getIdentifier("txtName", "id", getPackageName()));
        txtEmail = (TextView) findViewById(getResources().getIdentifier("txtEmail", "id", getPackageName()));
        btnMore = (ImageButton) findViewById(getResources().getIdentifier("btnMore", "id", getPackageName()));
        cardPremium = findViewById(getResources().getIdentifier("cardPremium", "id", getPackageName()));
        cardMode = findViewById(getResources().getIdentifier("cardMode", "id", getPackageName()));
        cardHealth = findViewById(getResources().getIdentifier("cardHealth", "id", getPackageName()));
        cardPreferences = findViewById(getResources().getIdentifier("cardPreferences", "id", getPackageName()));
        cardResources = findViewById(getResources().getIdentifier("cardResources", "id", getPackageName()));
        rowHealthNotes = findViewById(getResources().getIdentifier("rowHealthNotes", "id", getPackageName()));
        rowPregnancyPrevention = findViewById(getResources().getIdentifier("rowPregnancyPrevention", "id", getPackageName()));
        rowCustomize = findViewById(getResources().getIdentifier("rowCustomize", "id", getPackageName()));
        rowNotifications = findViewById(getResources().getIdentifier("rowNotifications", "id", getPackageName()));
        rowSettings = findViewById(getResources().getIdentifier("rowSettings", "id", getPackageName()));
        rowConnect = findViewById(getResources().getIdentifier("rowConnect", "id", getPackageName()));
        rowGuide = findViewById(getResources().getIdentifier("rowGuide", "id", getPackageName()));
    }
    private void setUpClickListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        if (btnMore != null) {
            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProfileDetails();
                }
            });
        }
        if (cardPremium != null) {
            cardPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPremiumFeatures();
                }
            });
        }
        if (cardMode != null) {
            cardMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openModeSettings();
                }
            });
        }
        if (rowHealthNotes != null) {
            rowHealthNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openHealthNotes();
                }
            });
        }

        if (rowPregnancyPrevention != null) {
            rowPregnancyPrevention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPregnancyPrevention();
                }
            });
        }

        // App preferences menu items
        if (rowCustomize != null) {
            rowCustomize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCustomizeMonitoring();
                }
            });
        }

        if (rowNotifications != null) {
            rowNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNotificationSettings();
                }
            });
        }

        if (rowSettings != null) {
            rowSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAppSettings();
                }
            });
        }

        if (rowConnect != null) {
            rowConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openClueConnect();
                }
            });
        }

        // Resources menu item
        if (rowGuide != null) {
            rowGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMonitoringGuide();
                }
            });
        }
    }

    private void setUpUserData() {
        // Set user information
        if (txtName != null) {
            txtName.setText("Indah Purnama Sari");
        }
        if (txtEmail != null) {
            txtEmail.setText("indahips409@gmail.com");
        }
        if (txtTitle != null) {
            txtTitle.setText("Menu Lainnya");
        }
    }

    // Navigation methods
    private void openProfileDetails() {
        // Intent intent = new Intent(this, ProfileDetailsActivity.class);
        // startActivity(intent);
        showToast("Opening profile details...");
    }

    private void openPremiumFeatures() {
        // Intent intent = new Intent(this, PremiumActivity.class);
        // startActivity(intent);
        showToast("Opening Clue Plus features...");
    }

    private void openModeSettings() {
        // Intent intent = new Intent(this, ModeSettingsActivity.class);
        // startActivity(intent);
        showToast("Opening mode settings...");
    }

    private void openHealthNotes() {
        // Intent intent = new Intent(this, HealthNotesActivity.class);
        // startActivity(intent);
        showToast("Opening health notes...");
    }

    private void openPregnancyPrevention() {
        // Intent intent = new Intent(this, PregnancyPreventionActivity.class);
        // startActivity(intent);
        showToast("Opening pregnancy prevention settings...");
    }

    private void openCustomizeMonitoring() {
        // Intent intent = new Intent(this, CustomizeMonitoringActivity.class);
        // startActivity(intent);
        showToast("Opening monitoring customization...");
    }

    private void openNotificationSettings() {
        // Intent intent = new Intent(this, NotificationSettingsActivity.class);
        // startActivity(intent);
        showToast("Opening notification settings...");
    }

    private void openAppSettings() {
        // Intent intent = new Intent(this, AppSettingsActivity.class);
        // startActivity(intent);
        showToast("Opening app settings...");
    }

    private void openClueConnect() {
        // Intent intent = new Intent(this, ClueConnectActivity.class);
        // startActivity(intent);
        showToast("Opening Clue Connect...");
    }

    private void openMonitoringGuide() {
        // Intent intent = new Intent(this, MonitoringGuideActivity.class);
        // startActivity(intent);
        showToast("Opening monitoring guide...");
    }

    // Utility method for showing toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method to update user profile data
    public void updateUserProfile(String name, String email) {
        if (txtName != null) {
            txtName.setText(name);
        }
        if (txtEmail != null) {
            txtEmail.setText(email);
        }
    }

    // Method to handle back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Add any custom back button behavior here if needed
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data when returning to this activity
        refreshUserData();
    }

    private void refreshUserData() {
        // You can implement logic here to refresh user data from your data source
        // For example, from SharedPreferences, database, or API call
    }
}