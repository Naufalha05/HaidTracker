package com.example.haidtracker.data.model.auth;

import com.google.gson.annotations.SerializedName;
import com.example.haidtracker.data.model.user.User;

public class RegisterResponse {
    
    @SerializedName("token")
    private String token;
    
    @SerializedName("user")
    private User user;
    
    @SerializedName("message")
    private String message;

    public RegisterResponse() {
    }

    public RegisterResponse(String token, User user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}