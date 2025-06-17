package com.example.haidtracker.data.model.auth;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("password")
    private String password;
    
    @SerializedName("password_confirmation")
    private String passwordConfirmation;

    // Constructor dengan 3 parameter (tanpa confirm password)
    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = password; // Set sama dengan password
    }

    // Constructor dengan 4 parameter (dengan confirm password)
    public RegisterRequest(String name, String email, String password, String passwordConfirmation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='[HIDDEN]'" +
                ", passwordConfirmation='[HIDDEN]'" +
                '}';
    }
}
