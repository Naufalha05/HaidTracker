package com.example.haidtracker.data.model.user;

public class UpdateUserRequest {
    private String email;
    private String name;
    private String password;
    private String role;

    public UpdateUserRequest() {}

    public UpdateUserRequest(String email, String name, String password, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Setters
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}