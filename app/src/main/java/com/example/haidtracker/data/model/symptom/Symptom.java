package com.example.haidtracker.data.model.symptom;

public class Symptom {
    private int id;
    private String date;
    private String mood;
    private String symptoms;
    private int userId;

    public Symptom() {}

    public Symptom(int id, String date, String mood, String symptoms, int userId) {
        this.id = id;
        this.date = date;
        this.mood = mood;
        this.symptoms = symptoms;
        this.userId = userId;
    }

    // Getters
    public int getId() { return id; }
    public String getDate() { return date; }
    public String getMood() { return mood; }
    public String getSymptoms() { return symptoms; }
    public int getUserId() { return userId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setDate(String date) { this.date = date; }
    public void setMood(String mood) { this.mood = mood; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setUserId(int userId) { this.userId = userId; }
}