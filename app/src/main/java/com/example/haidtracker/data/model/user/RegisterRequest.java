package com.example.haidtracker.data.model.user;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("password")
    private String password;

    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    // ... rest of the class
}