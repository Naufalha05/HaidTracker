package login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.haidtracker.R;

import org.json.JSONException;
import org.json.JSONObject;

import Home.SiklusActivity;
import login.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private TextView daftarText;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        rememberMeCheckBox = findViewById(R.id.checkbox);
        loginButton = findViewById(R.id.btnLogin);
        daftarText = findViewById(R.id.daftar);

        // Inisialisasi Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan kata sandi harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                doLogin(email, password);
            }
        });

        daftarText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void doLogin(String email, String password) {
        String url = "https://haidtracker-backend-production.up.railway.app/auth/login";

        try {
            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("password", password);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    params,
                    response -> {
                        // Cek response backend, misal ada field token atau status sukses
                        Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                        // Kalau backend mengembalikan token misal, bisa simpan di SharedPreferences di sini
                        // Contoh pindah halaman:
                        Intent intent = new Intent(LoginActivity.this, SiklusActivity.class);
                        startActivity(intent);
                        finish();
                    },
                    error -> {
                        // Tangani error, bisa karena jaringan atau autentikasi gagal
                        Toast.makeText(LoginActivity.this, "Login gagal: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error membuat request login", Toast.LENGTH_SHORT).show();
        }
    }
}
