package com.example.haidtracker.data.model.cycle;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Cycle {

    @SerializedName("id")
    private int id;

    @SerializedName("startDate")
    private Date startDate;

    @SerializedName("endDate")
    private Date endDate;

    @SerializedName("note")
    private String note;

    @SerializedName("userId")
    private int userId;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Cycle{" +
                "id=" + id +
                ", startDate=" + (startDate != null ? startDate.toString() : "null") +
                ", endDate=" + (endDate != null ? endDate.toString() : "null") +
                ", note='" + note + '\'' +
                ", userId=" + userId +
                '}';
    }
}
