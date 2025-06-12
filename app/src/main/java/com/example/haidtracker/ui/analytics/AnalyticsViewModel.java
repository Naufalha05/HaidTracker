package com.example.haidtracker.ui.analytics;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.haidtracker.data.model.analytics.AnalyticsData;
import com.example.haidtracker.data.model.analytics.DailyNote;
import com.example.haidtracker.data.repository.AnalyticsRepository;

// IMPOR MPAndroidChart
import com.github.mikephil.charting.data.Entry; // Penting: Ini Entry dari MPAndroidChart

// IMPOR GraphView DIHAPUS
// import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyticsViewModel extends AndroidViewModel {

    private final AnalyticsRepository repository;
    private final ExecutorService executor;

    // LiveData for UI
    private final MutableLiveData<AnalyticsData> analyticsData = new MutableLiveData<>();
    private final MutableLiveData<List<Entry>> graphData = new MutableLiveData<>(); // <<< UBAH DARI List<DataPoint> MENJADI List<Entry>
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    public AnalyticsViewModel(@NonNull Application application) {
        super(application);
        repository = new AnalyticsRepository(application);
        executor = Executors.newFixedThreadPool(2);
    }

    // Getters for LiveData
    public LiveData<AnalyticsData> getAnalyticsData() {
        return analyticsData;
    }

    public LiveData<List<Entry>> getGraphData() { // <<< UBAH DARI List<DataPoint> MENJADI List<Entry>
        return graphData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public void loadAnalyticsData() {
        isLoading.setValue(true);

        executor.execute(() -> {
            try {
                // Simulate loading analytics data
                AnalyticsData data = repository.getAnalyticsData();
                List<Entry> points = repository.getGraphData(); // <<< repository.getGraphData() sekarang mengembalikan List<Entry>

                // Post results on main thread
                analyticsData.postValue(data);
                graphData.postValue(points);

            } catch (Exception e) {
                errorMessage.postValue("Gagal memuat data: " + e.getMessage());
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    public void saveNote(String volume, String keluhan) {
        if (volume == null || volume.trim().isEmpty()) {
            errorMessage.setValue("Volume tidak boleh kosong");
            return;
        }

        isLoading.setValue(true);

        executor.execute(() -> {
            try {
                // Validate volume input
                if (!isValidVolume(volume)) {
                    errorMessage.postValue("Volume harus berupa: Sedikit, Sedang, atau Banyak");
                    return;
                }

                // Create note object
                DailyNote note = new DailyNote();
                note.setDate(getCurrentDate());
                note.setVolume(volume);
                note.setKeluhan(keluhan);
                note.setTimestamp(System.currentTimeMillis());

                // Save to repository
                boolean success = repository.saveDailyNote(note);

                if (success) {
                    successMessage.postValue("Catatan berhasil disimpan");
                    // Reload data to update UI
                    loadAnalyticsData();
                } else {
                    errorMessage.postValue("Gagal menyimpan catatan");
                }

            } catch (Exception e) {
                errorMessage.postValue("Error: " + e.getMessage());
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    private boolean isValidVolume(String volume) {
        String lowerVolume = volume.toLowerCase().trim();
        return lowerVolume.equals("sedikit") ||
                lowerVolume.equals("sedang") ||
                lowerVolume.equals("banyak");
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    public void refreshData() {
        loadAnalyticsData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}