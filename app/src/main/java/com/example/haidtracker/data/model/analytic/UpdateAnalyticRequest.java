package com.example.haidtracker.data.model.analytic;

public class UpdateAnalyticRequest {
    private String periodStart;
    private String periodEnd;
    private Integer averageCycle;
    private String symptomSummary;

    public UpdateAnalyticRequest() {}

    public UpdateAnalyticRequest(String periodStart, String periodEnd, Integer averageCycle, String symptomSummary) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.averageCycle = averageCycle;
        this.symptomSummary = symptomSummary;
    }

    // Getters
    public String getPeriodStart() { return periodStart; }
    public String getPeriodEnd() { return periodEnd; }
    public Integer getAverageCycle() { return averageCycle; }
    public String getSymptomSummary() { return symptomSummary; }

    // Setters
    public void setPeriodStart(String periodStart) { this.periodStart = periodStart; }
    public void setPeriodEnd(String periodEnd) { this.periodEnd = periodEnd; }
    public void setAverageCycle(Integer averageCycle) { this.averageCycle = averageCycle; }
    public void setSymptomSummary(String symptomSummary) { this.symptomSummary = symptomSummary; }
}