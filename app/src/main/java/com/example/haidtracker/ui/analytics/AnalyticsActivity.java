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

// IMPOR MPAndroidChart
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry; // Penting: Ini Entry dari MPAndroidChart
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate; // Untuk warna default

// IMPOR GraphView DIHAPUS
// import com.jjoe64.graphview.GraphView;
// import com.jjoe64.graphview.series.DataPoint;
// import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList; // Pastikan ini ada
import java.util.List; // Pastikan ini ada

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
    private LineChart lineChart; // <<< UBAH DARI GraphView menjadi LineChart

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
        lineChart = findViewById(R.id.graph); // <<< UBAH DARI graphView menjadi lineChart

        setupChart(); // <<< UBAH NAMA METODE
    }

    // --- AWAL PERUBAHAN CHARTING DENGAN MPAndroidChart ---
    private void setupChart() { // <<< UBAH NAMA METODE
        // Basic chart setup
        lineChart.getDescription().setEnabled(false); // Sembunyikan deskripsi
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);

        // X-axis setup
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Posisi label X di bawah
        xAxis.setDrawGridLines(false); // Sembunyikan garis grid vertikal
        xAxis.setGranularity(1f); // Interval 1 untuk label
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"1", "2", "3", "4", "5", "6", "7"})); // Contoh label hari

        // Left Y-axis setup
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Minimum Y
        leftAxis.setAxisMaximum(5f); // Maximum Y
        leftAxis.setDrawGridLines(true); // Tampilkan garis grid horizontal
        leftAxis.setGranularity(1f); // Interval 1 untuk label

        // Right Y-axis (disable if not needed)
        lineChart.getAxisRight().setEnabled(false); // Matikan Y-axis kanan

        lineChart.animateX(1500); // Animasi pada sumbu X
    }

    private void updateGraph(List<Entry> dataPoints) { // <<< UBAH TIPE PARAMETER KE List<Entry>
        LineDataSet dataSet;

        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            // Jika data sudah ada, update data yang ada
            dataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            dataSet.setValues(dataPoints);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // Jika belum ada data, buat dataset baru
            dataSet = new LineDataSet(dataPoints, "Intensitas Aliran"); // Label dataset

            // Kustomisasi penampilan dataset
            dataSet.setColor(getResources().getColor(R.color.pink_primary, null)); // Warna garis
            dataSet.setCircleColor(getResources().getColor(R.color.pink_primary, null)); // Warna titik
            dataSet.setLineWidth(3f); // Ketebalan garis
            dataSet.setCircleRadius(5f); // Ukuran titik
            dataSet.setDrawCircleHole(false); // Jangan gambar lubang di titik
            dataSet.setValueTextSize(9f); // Ukuran teks nilai di atas titik
            dataSet.setDrawValues(false); // Jangan tampilkan nilai di atas titik
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Garis melengkung
            dataSet.setDrawFilled(true); // Isi area di bawah garis
            dataSet.setFillColor(getResources().getColor(R.color.pink_primary, null)); // Warna isi area

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            LineData data = new LineData(dataSets);
            lineChart.setData(data);
        }

        lineChart.invalidate(); // Refresh chart
    }
    // --- AKHIR PERUBAHAN CHARTING DENGAN MPAndroidChart ---

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
                updateGraph(graphData); // <<< MEMANGGIL UPDATE DENGAN List<Entry>
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

    // Metode updateGraph yang lama dihapus karena sudah diubah di atas

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