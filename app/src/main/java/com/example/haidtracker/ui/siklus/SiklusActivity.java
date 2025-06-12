package com.example.haidtracker.ui.siklus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.ui.siklus.siklusAdapter;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.data.model.cycle.Cycle;
import com.example.haidtracker.ui.auth.SignInActivity;
import com.example.haidtracker.ui.calender.CalenderActivity;
import com.example.haidtracker.ui.home.AnalisisActivity;
import com.example.haidtracker.ui.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class SiklusActivity extends AppCompatActivity {

    private ImageView iconCalendar, iconAnalisis, iconProfil, iconContent;
    private FrameLayout fabPlus;
    private ImageButton btnLogout;
    private RecyclerView recyclerView;

    private siklusAdapter adapter;
    private List<Cycle> cycleList = new ArrayList<>();

    private SiklusViewModel viewModel;

    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String PREF_TOKEN = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siklus);

        // Inisialisasi UI
        iconCalendar = findViewById(R.id.menu_calender);
        iconAnalisis = findViewById(R.id.analisis);
        iconContent = findViewById(R.id.content);
        iconProfil = findViewById(R.id.menu_icon);
        fabPlus = findViewById(R.id.fab_plus);
        btnLogout = findViewById(R.id.btn_logout);


        // Navigasi menu bawah
        iconCalendar.setOnClickListener(v -> startActivity(new Intent(this, CalenderActivity.class)));
        iconAnalisis.setOnClickListener(v -> startActivity(new Intent(this, AnalisisActivity.class)));
        iconProfil.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        // Logout
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit().remove(PREF_TOKEN).apply();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Setup RecyclerView
        adapter = new siklusAdapter(cycleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(SiklusViewModel.class);

        // Ambil token
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString(PREF_TOKEN, null);

        // Ambil data siklus dari API
        if (token != null) {
            viewModel.fetchCycles("Bearer " + token); // Penting: sertakan prefix "Bearer "
        } else {
            Log.e("SiklusActivity", "Token tidak tersedia");
        }

        // Observe data dari ViewModel
        viewModel.getCycles().observe(this, cycles->{
            if (cycles != null) {
                cycleList.clear();
                cycleList.addAll(cycles);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("SiklusActivity", "Gagal memuat data siklus");
            }
        });
    }
}
