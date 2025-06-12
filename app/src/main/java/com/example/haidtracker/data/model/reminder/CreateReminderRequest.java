package com.example.haidtracker.data.model.reminder;

public class CreateReminderRequest {
    private String title;
    private String description;
    private String remindAt;

    public CreateReminderRequest() {}

    public CreateReminderRequest(String title, String description, String remindAt) {
        this.title = title;
        this.description = description;
        this.remindAt = remindAt;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRemindAt() { return remindAt; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRemindAt(String remindAt) { this.remindAt = remindAt; }
}