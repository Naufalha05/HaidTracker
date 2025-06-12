package com.example.haidtracker.data.model.reminder;

public class Reminder {
    private int id;
    private String title;
    private String description;
    private String remindAt;
    private boolean isActive;
    private int userId;

    public Reminder() {}

    public Reminder(int id, String title, String description, String remindAt, boolean isActive, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.remindAt = remindAt;
        this.isActive = isActive;
        this.userId = userId;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRemindAt() { return remindAt; }
    public boolean isActive() { return isActive; }
    public int getUserId() { return userId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRemindAt(String remindAt) { this.remindAt = remindAt; }
    public void setActive(boolean active) { isActive = active; }
    public void setUserId(int userId) { this.userId = userId; }
}