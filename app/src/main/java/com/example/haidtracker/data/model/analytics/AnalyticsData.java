package com.example.haidtracker.data.model.analytics;

public class AnalyticsData {
    private int hariKe;
    private String masaSubur;
    private int masaOvulasi;
    private int siklusTerakhir;
    private long timestamp;

    public AnalyticsData() {
        // Default constructor
    }

    public AnalyticsData(int hariKe, String masaSubur, int masaOvulasi, int siklusTerakhir) {
        this.hariKe = hariKe;
        this.masaSubur = masaSubur;
        this.masaOvulasi = masaOvulasi;
        this.siklusTerakhir = siklusTerakhir;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public int getHariKe() {
        return hariKe;
    }

    public String getMasaSubur() {
        return masaSubur;
    }

    public int getMasaOvulasi() {
        return masaOvulasi;
    }

    public int getSiklusTerakhir() {
        return siklusTerakhir;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setHariKe(int hariKe) {
        this.hariKe = hariKe;
    }

    public void setMasaSubur(String masaSubur) {
        this.masaSubur = masaSubur;
    }

    public void setMasaOvulasi(int masaOvulasi) {
        this.masaOvulasi = masaOvulasi;
    }

    public void setSiklusTerakhir(int siklusTerakhir) {
        this.siklusTerakhir = siklusTerakhir;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AnalyticsData{" +
                "hariKe=" + hariKe +
                ", masaSubur='" + masaSubur + '\'' +
                ", masaOvulasi=" + masaOvulasi +
                ", siklusTerakhir=" + siklusTerakhir +
                ", timestamp=" + timestamp +
                '}';
    }
}