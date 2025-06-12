package com.example.haidtracker.data.model.analytic;

public class CreateAnalyticRequest {
    private String periodStart;
    private String periodEnd;
    private int averageCycle;
    private String symptomSummary;

    public CreateAnalyticRequest() {}

    public CreateAnalyticRequest(String periodStart, String periodEnd, int averageCycle, String symptomSummary) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.averageCycle = averageCycle;
        this.symptomSummary = symptomSummary;
    }

    // Getters
    public String getPeriodStart() { return periodStart; }
    public String getPeriodEnd() { return periodEnd; }
    public int getAverageCycle() { return averageCycle; }
    public String getSymptomSummary() { return symptomSummary; }

    // Setters
    public void setPeriodStart(String periodStart) { this.periodStart = periodStart; }
    public void setPeriodEnd(String periodEnd) { this.periodEnd = periodEnd; }
    public void setAverageCycle(int averageCycle) { this.averageCycle = averageCycle; }
    public void setSymptomSummary(String symptomSummary) { this.symptomSummary = symptomSummary; }
}
