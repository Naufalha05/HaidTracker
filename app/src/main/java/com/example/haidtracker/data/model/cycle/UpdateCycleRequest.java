package com.example.haidtracker.data.model.cycle;

public class UpdateCycleRequest {
    private String startDate;
    private String endDate;
    private String note;

    public UpdateCycleRequest() {}

    public UpdateCycleRequest(String startDate, String endDate, String note) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    // Getters
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getNote() { return note; }

    // Setters
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setNote(String note) { this.note = note; }
}