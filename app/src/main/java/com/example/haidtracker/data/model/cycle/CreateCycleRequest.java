package com.example.haidtracker.data.model.cycle;

import com.google.gson.annotations.SerializedName;

public class CreateCycleRequest {

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("note")
    private String note;

    @SerializedName("userId")
    private Integer userId;

    public CreateCycleRequest(String startDate, String endDate, String note) {
        // Pastikan format tanggal sesuai dengan yang diharapkan API
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "CreateCycleRequest{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", note='" + note + '\'' +
                ", userId=" + userId +
                '}';
    }
}
