package com.example.haidtracker.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.haidtracker.data.model.analytics.AnalyticsData;
import com.example.haidtracker.data.model.analytics.DailyNote;
import com.github.mikephil.charting.data.Entry; // <<< BARIS INI DITAMBAHKAN (untuk MPAndroidChart)

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AnalyticsRepository {

    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String KEY_LAST_PERIOD_DATE = "last_period_date";
    private static final String KEY_CYCLE_LENGTH = "cycle_length";
    private static final String KEY_PERIOD_LENGTH = "period_length";

    private final SharedPreferences sharedPreferences;
    private final Context context;

    public AnalyticsRepository(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public AnalyticsData getAnalyticsData() {
        try {
            // Get stored data or use defaults
            int cycleLength = sharedPreferences.getInt(KEY_CYCLE_LENGTH, 28);
            int periodLength = sharedPreferences.getInt(KEY_PERIOD_LENGTH, 5);
            long lastPeriodDate = sharedPreferences.getLong(KEY_LAST_PERIOD_DATE, System.currentTimeMillis());

            // Calculate current day in cycle
            long currentTime = System.currentTimeMillis();
            int daysDiff = (int) ((currentTime - lastPeriodDate) / (1000 * 60 * 60 * 24));
            int currentDay = (daysDiff % cycleLength) + 1;

            // Calculate fertile period (typically days 10-16 in a 28-day cycle)
            int fertileStart = cycleLength / 2 - 4;
            int fertileEnd = cycleLength / 2 + 2;
            String fertileRange = formatFertilePeriod(lastPeriodDate, fertileStart, fertileEnd);

            // Ovulation period (typically 6 days around ovulation)
            int ovulationDays = 6;

            return new AnalyticsData(currentDay, fertileRange, ovulationDays, cycleLength);

        } catch (Exception e) {
            // Return default data if error occurs
            return new AnalyticsData(5, "8 Mei - 14 Mei", 6, 28);
        }
    }

    // --- PERUBAHAN UTAMA ADA DI METODE INI ---
    public List<Entry> getGraphData() { // <<< UBAH TIPE RETURN DARI List<DataPoint> MENJADI List<Entry>
        List<Entry> dataPoints = new ArrayList<>(); // <<< UBAH TIPE LIST DARI DataPoint MENJADI Entry

        try {
            // Generate sample data for the last 7 days
            // In a real app, this would come from stored daily notes
            for (int i = 0; i < 7; i++) {
                // MPAndroidChart's Entry constructor takes float for x and y values
                float x = (float) (i + 1); // Cast double to float
                float y = (float) generateSampleIntensity(i); // Cast double to float
                dataPoints.add(new Entry(x, y)); // <<< GANTI new DataPoint() MENJADI new Entry()
            }

        } catch (Exception e) {
            // Return empty list if error occurs
            dataPoints.clear();
        }

        return dataPoints;
    }
    // --- AKHIR PERUBAHAN UTAMA ---

    public boolean saveDailyNote(DailyNote note) {
        try {
            // In a real app, you would save this to a database
            // For now, we'll just simulate saving by storing in SharedPreferences
            String key = "note_" + note.getDate().replace("/", "_");
            String noteData = note.getVolume() + "|" + note.getKeluhan() + "|" + note.getTimestamp();

            sharedPreferences.edit()
                    .putString(key, noteData)
                    .apply();

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public List<DailyNote> getDailyNotes() {
        List<DailyNote> notes = new ArrayList<>();

        try {
            // In a real app, you would retrieve from database
            // For now, we'll return sample data
            for (int i = 0; i < 7; i++) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -i);
                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.getTime());

                DailyNote note = new DailyNote();
                note.setDate(date);
                note.setVolume(getSampleVolume(i));
                note.setKeluhan(getSampleKeluhan(i));
                note.setTimestamp(cal.getTimeInMillis());

                notes.add(note);
            }

        } catch (Exception e) {
            // Return empty list if error occurs
        }

        return notes;
    }

    private String formatFertilePeriod(long lastPeriodDate, int startDay, int endDay) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(lastPeriodDate);

            // Add start day
            cal.add(Calendar.DAY_OF_MONTH, startDay - 1);
            String startDate = new SimpleDateFormat("dd MMM", new Locale("id", "ID")).format(cal.getTime());

            // Add end day
            cal.setTimeInMillis(lastPeriodDate);
            cal.add(Calendar.DAY_OF_MONTH, endDay - 1);
            String endDate = new SimpleDateFormat("dd MMM", new Locale("id", "ID")).format(cal.getTime());

            return startDate + " - " + endDate;

        } catch (Exception e) {
            return "8 Mei - 14 Mei"; // Default fallback
        }
    }

    private double generateSampleIntensity(int dayIndex) {
        // Generate realistic sample data based on typical menstrual cycle
        switch (dayIndex) {
            case 0: return 4.0; // Heavy flow
            case 1: return 3.5; // Medium-heavy
            case 2: return 2.5; // Medium
            case 3: return 1.5; // Light
            case 4: return 1.0; // Very light
            case 5: return 0.5; // Spotting
            case 6: return 0.0; // No flow
            default: return 0.0;
        }
    }

    private String getSampleVolume(int dayIndex) {
        switch (dayIndex % 3) {
            case 0: return "Banyak";
            case 1: return "Sedang";
            case 2: return "Sedikit";
            default: return "Sedang";
        }
    }

    private String getSampleKeluhan(int dayIndex) {
        String[] keluhans = {
                "Nyeri perut ringan",
                "Pusing",
                "Nyeri punggung",
                "Mual",
                "Kram perut",
                "Lelah",
                "Mood swing"
        };
        return keluhans[dayIndex % keluhans.length];
    }

    // Methods to update cycle settings
    public void updateCycleLength(int cycleLength) {
        sharedPreferences.edit()
                .putInt(KEY_CYCLE_LENGTH, cycleLength)
                .apply();
    }

    public void updatePeriodLength(int periodLength) {
        sharedPreferences.edit()
                .putInt(KEY_PERIOD_LENGTH, periodLength)
                .apply();
    }

    public void updateLastPeriodDate(long dateInMillis) {
        sharedPreferences.edit()
                .putLong(KEY_LAST_PERIOD_DATE, dateInMillis)
                .apply();
    }
}