package com.example.haidtracker.data.model.cycle;

public class Cycle {
    private int id;
    private String startDate;
    private String endDate;
    private String note;
    private int userId;

    public Cycle() {}

    public Cycle(int id, String startDate, String endDate, String note, int userId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.userId = userId;
    }

    // Getters
    public int getId() { return id; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getNote() { return note; }
    public int getUserId() { return userId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setNote(String note) { this.note = note; }
    public void setUserId(int userId) { this.userId = userId; }
}