package com.example.haidtracker.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.data.repository.AuthRepository;

public class SignInViewModel extends AndroidViewModel {
    
    private AuthRepository authRepository;
    private MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public SignInViewModel(@NonNull Application application) {
        super(application);
        // Pass application context to AuthRepository
        authRepository = new AuthRepository(application.getApplicationContext());
    }

    public void login(String email, String password) {
        isLoading.setValue(true);
        
        authRepository.login(email, password, new AuthRepository.AuthCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse result) {
                isLoading.setValue(false);
                loginResult.setValue(result);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
            }
        });
    }

    public void saveRememberMe(String email) {
        authRepository.saveRememberMe(email);
    }

    public void clearRememberMe() {
        authRepository.clearRememberMe();
    }

    public boolean isRememberMeEnabled() {
        return authRepository.isRememberMeEnabled();
    }

    public String getSavedEmail() {
        return authRepository.getSavedEmail();
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    // Getters for LiveData
    public MutableLiveData<LoginResponse> getLoginResult() {
        return loginResult;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
