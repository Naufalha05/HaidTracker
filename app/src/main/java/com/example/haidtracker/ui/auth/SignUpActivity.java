package com.example.haidtracker.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.haidtracker.R;
import com.google.android.material.textfield.TextInputEditText;
import com.example.haidtracker.data.model.auth.LoginResponse;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText nameEditText, emailEditText, passwordEditText;
    private Button btnRegister;
    private ImageButton btnBack;
    private TextView loginLink;

    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        loginLink = findViewById(R.id.login);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        btnBack.setOnClickListener(v -> finish());

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Misal role di-set "user" default
            String role = "user";

            viewModel.register(name, email, password, role);

            viewModel.getRegisterResult().observe(this, loginResponse -> {
                if (loginResponse != null) {
                    Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                    // Misal pindah ke halaman login
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Pendaftaran gagal", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
