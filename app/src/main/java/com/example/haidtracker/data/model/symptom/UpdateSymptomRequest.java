package com.example.haidtracker.data.model.symptom;

public class UpdateSymptomRequest {
    private String date;
    private String mood;
    private String symptoms;

    public UpdateSymptomRequest() {}

    public UpdateSymptomRequest(String date, String mood, String symptoms) {
        this.date = date;
        this.mood = mood;
        this.symptoms = symptoms;
    }

    // Getters
    public String getDate() { return date; }
    public String getMood() { return mood; }
    public String getSymptoms() { return symptoms; }

    // Setters
    public void setDate(String date) { this.date = date; }
    public void setMood(String mood) { this.mood = mood; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
}