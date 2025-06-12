package com.example.haidtracker.data.model.reminder;

public class UpdateReminderRequest {
    private String title;
    private String description;
    private String remindAt;
    private Boolean isActive;

    public UpdateReminderRequest() {}

    public UpdateReminderRequest(String title, String description, String remindAt, Boolean isActive) {
        this.title = title;
        this.description = description;
        this.remindAt = remindAt;
        this.isActive = isActive;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRemindAt() { return remindAt; }
    public Boolean getIsActive() { return isActive; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRemindAt(String remindAt) { this.remindAt = remindAt; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}