package com.example.haidtracker.data.model.analytics;

public class DailyNote {
    private long id;
    private String date;
    private String volume;
    private String keluhan;
    private long timestamp;

    public DailyNote() {
        // Default constructor
    }

    public DailyNote(String date, String volume, String keluhan) {
        this.date = date;
        this.volume = volume;
        this.keluhan = keluhan;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getVolume() {
        return volume;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Helper method to get volume intensity for graph
    public int getVolumeIntensity() {
        if (volume == null) return 0;

        switch (volume.toLowerCase().trim()) {
            case "sedikit":
                return 1;
            case "sedang":
                return 3;
            case "banyak":
                return 5;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "DailyNote{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", volume='" + volume + '\'' +
                ", keluhan='" + keluhan + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}