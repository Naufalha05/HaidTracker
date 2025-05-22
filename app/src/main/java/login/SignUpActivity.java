package login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.haidtracker.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button btnRegister;
    private ImageButton btnBack;
    private TextView loginLink;

    private final OkHttpClient client = new OkHttpClient();
    private static final String REGISTER_URL = "https://haidtracker-backend-production.up.railway.app/auth/register";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Inisialisasi view
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        loginLink = findViewById(R.id.login);

        // Tombol kembali
        btnBack.setOnClickListener(v -> finish());

        // Tombol "Daftar"
        btnRegister.setOnClickListener(v -> daftarPengguna());

        // Tautan ke halaman Login
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void daftarPengguna() {
        String nama = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String konfirmasi = confirmPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(konfirmasi)) {
            Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(konfirmasi)) {
            Toast.makeText(this, "Konfirmasi kata sandi tidak cocok!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat JSON untuk dikirim ke server
        JSONObject json = new JSONObject();
        try {
            json.put("name", nama);
            json.put("email", email);
            json.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Terjadi kesalahan saat membuat data pendaftaran.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kirim permintaan POST menggunakan OkHttp
        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Tangani kegagalan koneksi
                runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Tangani respons sukses
                    String responseData = response.body().string();
                    runOnUiThread(() -> {
                        Toast.makeText(SignUpActivity.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                        // Lanjutkan ke aktivitas berikutnya atau simpan token jika diperlukan
                    });
                } else {
                    // Tangani respons gagal
                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Pendaftaran gagal", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
