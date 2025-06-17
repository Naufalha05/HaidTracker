package com.example.haidtracker.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.auth.LoginResponse;
import com.example.haidtracker.data.repository.AuthRepository;
import com.example.haidtracker.ui.siklus.SiklusActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private CheckBox checkbox;
    private ProgressBar progressBar;
    
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeViews();
        setupClickListeners();
        
        // Initialize AuthRepository
        authRepository = new AuthRepository(this);
        
        // Load saved credentials if remember me is enabled
        loadSavedCredentials();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        checkbox = findViewById(R.id.checkbox);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performSignIn());
        
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void loadSavedCredentials() {
        if (authRepository.isRememberMeEnabled()) {
            String savedEmail = authRepository.getSavedEmail();
            etEmail.setText(savedEmail);
            checkbox.setChecked(true);
        }
    }

    private void performSignIn() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        authRepository.login(email, password, new AuthRepository.AuthCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse result) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                
                // Save "Remember Me" preference
                if (checkbox.isChecked()) {
                    authRepository.saveRememberMe(email);
                } else {
                    authRepository.clearRememberMe();
                }
                
                Log.d(TAG, "Login successful for user: " + result.getUser().getEmail());
                Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                
                // Navigate to main activity
                Intent intent = new Intent(SignInActivity.this, SiklusActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Login failed: " + error);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // Check if user is already logged in
        if (authRepository.isLoggedIn()) {
            Intent intent = new Intent(this, SiklusActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
