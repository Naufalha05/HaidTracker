package com.example.haidtracker.ui.symptom;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.symptom.CreateSymptomRequest;
import com.example.haidtracker.data.model.symptom.Symptom;
import com.example.haidtracker.ui.analytics.AnalyticsActivity;
import com.example.haidtracker.ui.auth.SignInActivity;
import com.example.haidtracker.ui.calender.CalenderActivity;
import com.example.haidtracker.ui.profile.ProfileActivity;
import com.example.haidtracker.ui.siklus.SiklusActivity;
import com.example.haidtracker.util.TokenManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SymptomActivity extends AppCompatActivity {

    private static final String TAG = "SymptomActivity";
    
    private RecyclerView recyclerView;
    private SymptomAdapter adapter;
    private FloatingActionButton fabAddSymptom;
    private ApiService apiService;
    private String authToken;
    private TextView tvFactOfTheDay;
    private ImageButton btnLogout;

    // Fakta unik tentang haid
    private String[] menstrualFacts = {
            "💡 Fakta: Rata-rata wanita mengalami sekitar 400 kali menstruasi sepanjang hidupnya.",
            "🌙 Fakta: Siklus menstruasi dipengaruhi oleh fase bulan dan dapat bervariasi 21-35 hari.",
            "🩸 Fakta: Volume darah yang keluar saat menstruasi hanya sekitar 30-40ml, sisanya adalah jaringan rahim.",
            "🧠 Fakta: Hormon estrogen saat menstruasi dapat meningkatkan kemampuan verbal dan memori.",
            "💪 Fakta: Olahraga ringan saat menstruasi dapat membantu mengurangi kram dan meningkatkan mood.",
            "🍫 Fakta: Keinginan makan cokelat saat PMS disebabkan oleh penurunan kadar serotonin.",
            "🌡️ Fakta: Suhu tubuh wanita naik 0.5°C setelah ovulasi dan turun saat menstruasi dimulai.",
            "🦴 Fakta: Menstruasi yang teratur menunjukkan kesehatan tulang yang baik karena produksi estrogen.",
            "😴 Fakta: Kualitas tidur dapat mempengaruhi siklus menstruasi dan sebaliknya.",
            "🌸 Fakta: Wanita yang tinggal bersama sering mengalami sinkronisasi siklus menstruasi."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        loadSymptoms();
        showRandomFact();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewSymptoms);
        fabAddSymptom = findViewById(R.id.fabAddSymptom);
        tvFactOfTheDay = findViewById(R.id.tvFactOfTheDay);
        btnLogout = findViewById(R.id.btn_logout);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);
        authToken = TokenManager.getToken(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SymptomAdapter(new ArrayList<>());
        
        // Set delete listener
        adapter.setOnSymptomDeleteListener((symptom, position) -> {
            showDeleteConfirmationDialog(symptom, position);
        });
        
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        fabAddSymptom.setOnClickListener(v -> showAddSymptomDialog());
        
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("HaidTrackerPrefs", MODE_PRIVATE);
            prefs.edit().remove("auth_token").apply();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Navigation - pastikan semua icon navbar terhubung dengan benar
        findViewById(R.id.menu_siklus).setOnClickListener(v -> 
            startActivity(new Intent(this, SiklusActivity.class)));
        findViewById(R.id.menu_calender).setOnClickListener(v -> 
            startActivity(new Intent(this, CalenderActivity.class)));
        findViewById(R.id.analisis).setOnClickListener(v -> 
            startActivity(new Intent(this, AnalyticsActivity.class)));
        findViewById(R.id.menu_icon).setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class)));
        
        // Icon content (current page) - bisa diberi highlight atau tidak ada action
        findViewById(R.id.content).setOnClickListener(v -> {
            // Current page, bisa tidak ada action atau refresh page
            Toast.makeText(this, "Anda sedang di halaman Gejala", Toast.LENGTH_SHORT).show();
        });
    }

    private void showRandomFact() {
        Random random = new Random();
        int index = random.nextInt(menstrualFacts.length);
        tvFactOfTheDay.setText(menstrualFacts[index]);
    }

    private void showAddSymptomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_symptom, null);
        builder.setView(dialogView);

        EditText etDate = dialogView.findViewById(R.id.etDate);
        ChipGroup chipGroupMood = dialogView.findViewById(R.id.chipGroupMood);
        ChipGroup chipGroupSymptoms = dialogView.findViewById(R.id.chipGroupSymptoms);
        EditText etAdditionalNotes = dialogView.findViewById(R.id.etAdditionalNotes);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();

        // Date picker
        etDate.setOnClickListener(v -> showDatePicker(etDate));

        btnSave.setOnClickListener(v -> {
            String date = etDate.getText().toString().trim();
            String mood = getSelectedChipText(chipGroupMood);
            String symptoms = getSelectedChipsText(chipGroupSymptoms);
            String additionalNotes = etAdditionalNotes.getText().toString().trim();

            if (date.isEmpty()) {
                Toast.makeText(this, "Tanggal wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Combine symptoms and additional notes
            String finalSymptoms = symptoms;
            if (!additionalNotes.isEmpty()) {
                finalSymptoms += (symptoms.isEmpty() ? "" : ", ") + additionalNotes;
            }

            createSymptom(date, mood, finalSymptoms);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    editText.setText(dateFormat.format(calendar.getTime()));
                },
                year, month, day);

        datePickerDialog.show();
    }

    private String getSelectedChipText(ChipGroup chipGroup) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                return chip.getText().toString();
            }
        }
        return "";
    }

    private String getSelectedChipsText(ChipGroup chipGroup) {
        List<String> selectedChips = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedChips.add(chip.getText().toString());
            }
        }
        return String.join(", ", selectedChips);
    }

    private void createSymptom(String date, String mood, String symptoms) {
        CreateSymptomRequest request = new CreateSymptomRequest(date, mood, symptoms);

        apiService.createSymptom(authToken, request).enqueue(new Callback<Symptom>() {
            @Override
            public void onResponse(Call<Symptom> call, Response<Symptom> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SymptomActivity.this, "Gejala berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    loadSymptoms(); // Refresh list
                } else {
                    Toast.makeText(SymptomActivity.this, "Gagal menambahkan gejala", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error creating symptom: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Symptom> call, Throwable t) {
                Toast.makeText(SymptomActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error creating symptom", t);
            }
        });
    }

    private void loadSymptoms() {
        apiService.getAllSymptoms(authToken).enqueue(new Callback<List<Symptom>>() {
            @Override
            public void onResponse(Call<List<Symptom>> call, Response<List<Symptom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                } else {
                    Toast.makeText(SymptomActivity.this, "Gagal memuat gejala", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error loading symptoms: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Symptom>> call, Throwable t) {
                Toast.makeText(SymptomActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error loading symptoms", t);
            }
        });
    }

    private void showDeleteConfirmationDialog(Symptom symptom, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Gejala");
        builder.setMessage("Apakah Anda yakin ingin menghapus gejala ini?");
        
        builder.setPositiveButton("Hapus", (dialog, which) -> {
            deleteSymptom(symptom, position);
        });
        
        builder.setNegativeButton("Batal", (dialog, which) -> {
            dialog.dismiss();
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
        // Customize button colors
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void deleteSymptom(Symptom symptom, int position) {
        apiService.deleteSymptom(authToken, symptom.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SymptomActivity.this, "Gejala berhasil dihapus", Toast.LENGTH_SHORT).show();
                    adapter.removeSymptom(position);
                } else {
                    Toast.makeText(SymptomActivity.this, "Gagal menghapus gejala", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error deleting symptom: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SymptomActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error deleting symptom", t);
            }
        });
    }
}

