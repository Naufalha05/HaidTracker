package com.example.haidtracker.ui.intro;
public class IntroSlide {
    private int imageResId;
    private String title;
    private String description;

    public IntroSlide(int imageResId, String title, String description) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}