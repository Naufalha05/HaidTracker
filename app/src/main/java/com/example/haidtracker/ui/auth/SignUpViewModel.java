package com.example.haidtracker.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.haidtracker.data.model.auth.RegisterResponse;
import com.example.haidtracker.data.repository.AuthRepository;

public class SignUpViewModel extends AndroidViewModel {
    
    private AuthRepository authRepository;
    private MutableLiveData<RegisterResponse> registerResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        // Pass application context to AuthRepository
        authRepository = new AuthRepository(application.getApplicationContext());
    }

    public void register(String name, String email, String password, String confirmPassword) {
        isLoading.setValue(true);
        
        authRepository.register(name, email, password, confirmPassword, new AuthRepository.AuthCallback<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse result) {
                isLoading.setValue(false);
                registerResult.setValue(result);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
            }
        });
    }

    // Getters for LiveData
    public MutableLiveData<RegisterResponse> getRegisterResult() {
        return registerResult;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
