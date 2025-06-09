package com.example.haidtracker.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.haidtracker.R;
import com.example.haidtracker.ui.siklus.SiklusActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private TextView daftarText;

    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String PREF_TOKEN = "auth_token";

    private SignInViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        rememberMeCheckBox = findViewById(R.id.checkbox);
        loginButton = findViewById(R.id.btnLogin);
        daftarText = findViewById(R.id.daftar);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString(PREF_TOKEN, null);
        if (token != null) {
            navigateToSiklusActivity();
            return;
        }

        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Email dan kata sandi wajib diisi.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.login(email, password);
            }
        });

        daftarText.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));

        // Observe token dari ViewModel
        viewModel.getToken().observe(this, loginToken -> {
            if (loginToken != null) {
                if (rememberMeCheckBox.isChecked()) {
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString(PREF_TOKEN, loginToken);
                    editor.apply();
                }
                navigateToSiklusActivity();
            }
        });

        // Observe error message
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToSiklusActivity() {
        Intent intent = new Intent(SignInActivity.this, SiklusActivity.class);
        startActivity(intent);
        finish();
    }
}
