package com.example.haidtracker.data.model.symptom;

import com.google.gson.annotations.SerializedName;

public class UpdateSymptomRequest {

    @SerializedName("date")
    private String date;

    @SerializedName("mood")
    private String mood;

    @SerializedName("symptoms")
    private String symptoms;

    public UpdateSymptomRequest(String date, String mood, String symptoms) {
        this.date = date;
        this.mood = mood;
        this.symptoms = symptoms;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
