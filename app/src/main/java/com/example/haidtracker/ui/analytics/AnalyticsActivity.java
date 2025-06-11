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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class AnalyticsActivity extends AppCompatActivity {

    private AnalyticsViewModel viewModel;

    // UI Components
    private TextView tvHariKe;
    private TextView tvMasaSubur;
    private TextView tvMasaOvulasi;
    private TextView tvSiklus;
    private EditText etVolume;
    private EditText etKeluhan;
    private Button btnSimpan;
    private GraphView graphView;

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

        initViewModel();
        initViews();
        setupObservers();
        setupClickListeners();

        // Load initial data
        viewModel.loadAnalyticsData();
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
        graphView = findViewById(R.id.graph);

        setupGraph();
    }

    private void setupGraph() {
        // Setup graph properties
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Hari");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Intensitas");
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(5);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalable(true);
    }

    private void setupObservers() {
        // Observe analytics data
        viewModel.getAnalyticsData().observe(this, analyticsData -> {
            if (analyticsData != null) {
                updateUI(analyticsData);
            }
        });

        // Observe graph data
        viewModel.getGraphData().observe(this, graphData -> {
            if (graphData != null && !graphData.isEmpty()) {
                updateGraph(graphData);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(this, isLoading -> {
            btnSimpan.setEnabled(!isLoading);
            // You can add loading indicator here if needed
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe success message
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

    private void updateGraph(java.util.List<DataPoint> dataPoints) {
        DataPoint[] points = dataPoints.toArray(new DataPoint[0]);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        // Customize series appearance
        series.setColor(getResources().getColor(R.color.pink_primary, null));
        series.setThickness(8);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);

        graphView.removeAllSeries();
        graphView.addSeries(series);
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