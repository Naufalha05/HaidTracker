package login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.haidtracker.R;

import Home.SiklusActivity;
import login.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private TextView daftarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Pastikan ini nama file layout kamu

        // Inisialisasi view
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        rememberMeCheckBox = findViewById(R.id.checkbox);
        loginButton = findViewById(R.id.btnLogin);
        daftarText = findViewById(R.id.daftar);

        // Aksi klik tombol login
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan kata sandi harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                // Sementara, tampilkan toast. Ganti dengan autentikasi sebenarnya.
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show();

                // Contoh: pindah ke halaman utama
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Aksi klik teks daftar
        daftarText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SiklusActivity.class);
            startActivity(intent);
        });
    }
}
