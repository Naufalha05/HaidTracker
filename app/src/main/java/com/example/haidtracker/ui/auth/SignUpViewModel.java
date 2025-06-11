package com.example.haidtracker.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.data.repository.AuthRepository;

public class SignUpViewModel extends ViewModel {
    private final AuthRepository repository;
    private LiveData<LoginResponse> registerResult;

    public SignUpViewModel() {
        repository = new AuthRepository();
    }

    public void register(String name, String email, String password, String role) {
        registerResult = repository.register(name, email, password, role);
    }

    public LiveData<LoginResponse> getRegisterResult() {
        return registerResult;
    }
}
