package com.example.haidtracker.data.model.analytic;

public class Analytic {

    private int id;
    private String periodStart;
    private String periodEnd;

    // Change from int to float or double
    private float averageCycle; // or private double averageCycle;

    private String symptomSummary;

    // Getters and setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }

    public float getAverageCycle() {
        return averageCycle;
    }

    public void setAverageCycle(float averageCycle) {
        this.averageCycle = averageCycle;
    }

    public String getSymptomSummary() {
        return symptomSummary;
    }

    public void setSymptomSummary(String symptomSummary) {
        this.symptomSummary = symptomSummary;
    }
}
