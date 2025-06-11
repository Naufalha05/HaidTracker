package com.example.haidtracker.data.model.auth;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role;

    public RegisterRequest(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
