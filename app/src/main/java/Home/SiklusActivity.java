package Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.haidtracker.R;

import Page.SignInActivity;

public class SiklusActivity extends AppCompatActivity {

    ImageView iconCalendar, iconAnalisis, iconProfil, iconContent;
    FrameLayout fabPlus;
    ImageButton btnLogout;

    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String PREF_TOKEN = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siklus);

        iconCalendar = findViewById(R.id.menu_calender);
        iconAnalisis = findViewById(R.id.analisis);
        iconContent = findViewById(R.id.content);
        iconProfil = findViewById(R.id.menu_icon);
        fabPlus = findViewById(R.id.fab_plus);
        btnLogout = findViewById(R.id.btn_logout);

        iconCalendar.setOnClickListener(v -> startActivity(new Intent(SiklusActivity.this, CalenderActivity.class)));

        iconAnalisis.setOnClickListener(v -> startActivity(new Intent(SiklusActivity.this, AnalisisActivity.class)));

        iconProfil.setOnClickListener(v -> startActivity(new Intent(SiklusActivity.this, ProfileActivity.class)));

        // ✅ Listener tombol logout
        btnLogout.setOnClickListener(v -> {
            // Hapus token dari SharedPreferences
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(PREF_TOKEN);
            editor.apply();

            // Redirect ke SignInActivity dan hapus riwayat Activity
            Intent intent = new Intent(SiklusActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
