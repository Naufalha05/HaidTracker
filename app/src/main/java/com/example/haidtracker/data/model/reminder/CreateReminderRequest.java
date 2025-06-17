package com.example.haidtracker.data.model.reminder;

import com.google.gson.annotations.SerializedName;

public class CreateReminderRequest {
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("remindAt")
    private String remindAt;

    public CreateReminderRequest(String title, String description, String remindAt) {
        this.title = title;
        this.description = description;
        this.remindAt = remindAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(String remindAt) {
        this.remindAt = remindAt;
    }
}
