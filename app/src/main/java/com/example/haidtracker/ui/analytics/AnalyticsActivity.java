package com.example.haidtracker.ui.analytics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.analytics.AnalyticsData;
import com.example.haidtracker.ui.auth.SignInActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    private AnalyticsViewModel viewModel;

    private TextView tvHariKe, tvMasaSubur, tvMasaOvulasi, tvSiklus;
    private EditText etVolume, etKeluhan;
    private Button btnSimpan;
    private LineChart lineChart;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analytics);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.graph), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ambil token
        token = getSharedPreferences("user_session", MODE_PRIVATE).getString("auth_token", null);
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        initViewModel();
        initViews();
        setupObservers();
        setupClickListeners();

        viewModel.loadAnalyticsData(); // ✅ Pastikan method ini menerima token
    }

    private void initViewModel() {
        AnalyticsViewModelFactory factory = new AnalyticsViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(AnalyticsViewModel.class);
    }

    private void initViews() {
        tvHariKe = findViewById(R.id.tv_hari_ke);
        tvMasaSubur = findViewById(R.id.tv_masa_subur);
        tvMasaOvulasi = findViewById(R.id.tv_masa_ovulasi);
        tvSiklus = findViewById(R.id.tv_siklus);
        etVolume = findViewById(R.id.et_volume);
        etKeluhan = findViewById(R.id.et_keluhan);
        btnSimpan = findViewById(R.id.btn_simpan);
        lineChart = findViewById(R.id.graph);

        setupChart();
    }

    private void setupChart() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"1", "2", "3", "4", "5", "6", "7"}));

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(1f);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateX(1500);
    }

    private void updateGraph(List<Entry> dataPoints) {
        LineDataSet dataSet;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            dataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            dataSet.setValues(dataPoints);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            dataSet = new LineDataSet(dataPoints, "Intensitas Aliran");
            dataSet.setColor(getResources().getColor(R.color.pink_primary, null));
            dataSet.setCircleColor(getResources().getColor(R.color.pink_primary, null));
            dataSet.setLineWidth(3f);
            dataSet.setCircleRadius(5f);
            dataSet.setDrawCircleHole(false);
            dataSet.setValueTextSize(9f);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(getResources().getColor(R.color.pink_primary, null));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);
            lineChart.setData(new LineData(dataSets));
        }
        lineChart.invalidate();
    }

    private void setupObservers() {
        viewModel.getAnalyticsData().observe(this, analyticsData -> {
            if (analyticsData != null) {
                updateUI(analyticsData);
            }
        });

        viewModel.getGraphData().observe(this, graphData -> {
            if (graphData != null && !graphData.isEmpty()) {
                updateGraph(graphData);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            btnSimpan.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null && !successMessage.isEmpty()) {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                clearInputFields();
            }
        });
    }

    private void setupClickListeners() {
        btnSimpan.setOnClickListener(v -> {
            String volume = etVolume.getText().toString().trim();
            String keluhan = etKeluhan.getText().toString().trim();

            if (volume.isEmpty()) {
                etVolume.setError("Volume tidak boleh kosong");
                return;
            }

            viewModel.saveNote(volume, keluhan);
        });
    }

    private void updateUI(AnalyticsData data) {
        tvHariKe.setText(data.getHariKe() + " Hari");
        tvMasaSubur.setText(data.getMasaSubur());
        tvMasaOvulasi.setText(data.getMasaOvulasi() + " Hari");
        tvSiklus.setText(data.getSiklusTerakhir() + " Hari");
    }

    private void clearInputFields() {
        etVolume.setText("");
        etKeluhan.setText("");
        etVolume.clearFocus();
        etKeluhan.clearFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) {
            viewModel.onCleared();
        }
    }
}
