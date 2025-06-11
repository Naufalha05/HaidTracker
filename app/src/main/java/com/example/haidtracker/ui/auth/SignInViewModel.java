package com.example.haidtracker.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.data.repository.AuthRepository;

public class SignInViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<String> token = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SignInViewModel() {
        authRepository = new AuthRepository();
    }

    public LiveData<String> getToken() {
        return token;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        authRepository.login(email, password).observeForever(response -> {
            if (response != null && response.getToken() != null) {
                token.postValue(response.getToken());
                errorMessage.postValue(null);
            } else {
                token.postValue(null);
                errorMessage.postValue("Login gagal: token tidak ditemukan");
            }
        });
    }
}
