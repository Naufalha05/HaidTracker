package com.example.haidtracker.data.model.cycle;

public class CreateCycleRequest {
    private String startDate;
    private String endDate;
    private String note;
    private Integer userId; // Optional, only for admin

    public CreateCycleRequest() {}

    public CreateCycleRequest(String startDate, String endDate, String note) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    public CreateCycleRequest(String startDate, String endDate, String note, Integer userId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.userId = userId;
    }

    // Getters
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getNote() { return note; }
    public Integer getUserId() { return userId; }

    // Setters
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setNote(String note) { this.note = note; }
    public void setUserId(Integer userId) { this.userId = userId; }
}