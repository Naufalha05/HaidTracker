package com.example.haidtracker.data.model.reminder;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Reminder {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("date")
    private Date date;

    @SerializedName("time")
    private String time;

    @SerializedName("type")
    private String type; // e.g., "medication", "appointment", "general"

    @SerializedName("is_active")
    private boolean isActive;

    @SerializedName("repeat_type")
    private String repeatType; // e.g., "none", "daily", "weekly", "monthly"

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    // Default constructor
    public Reminder() {}

    // Constructor with parameters
    public Reminder(int userId, String title, String description, Date date, String time) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.isActive = true;
        this.repeatType = "none";
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", isActive=" + isActive +
                ", repeatType='" + repeatType + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}