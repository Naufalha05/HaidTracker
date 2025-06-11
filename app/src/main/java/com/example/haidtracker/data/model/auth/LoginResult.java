package com.example.haidtracker.data.model.auth;

public class LoginResult {
    public boolean success;
    public String token;
    public String errorMessage;

    public LoginResult(boolean success, String token, String errorMessage) {
        this.success = success;
        this.token = token;
        this.errorMessage = errorMessage;
    }
}
