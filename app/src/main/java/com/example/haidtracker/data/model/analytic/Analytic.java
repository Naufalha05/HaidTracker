package com.example.haidtracker.data.model.analytic;

public class Analytic {
    private int id;
    private String periodStart;
    private String periodEnd;
    private int averageCycle;
    private String symptomSummary;
    private int userId;

    public Analytic() {}

    public Analytic(int id, String periodStart, String periodEnd, int averageCycle, String symptomSummary, int userId) {
        this.id = id;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.averageCycle = averageCycle;
        this.symptomSummary = symptomSummary;
        this.userId = userId;
    }

    // Getters
    public int getId() { return id; }
    public String getPeriodStart() { return periodStart; }
    public String getPeriodEnd() { return periodEnd; }
    public int getAverageCycle() { return averageCycle; }
    public String getSymptomSummary() { return symptomSummary; }
    public int getUserId() { return userId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setPeriodStart(String periodStart) { this.periodStart = periodStart; }
    public void setPeriodEnd(String periodEnd) { this.periodEnd = periodEnd; }
    public void setAverageCycle(int averageCycle) { this.averageCycle = averageCycle; }
    public void setSymptomSummary(String symptomSummary) { this.symptomSummary = symptomSummary; }
    public void setUserId(int userId) { this.userId = userId; }
}
