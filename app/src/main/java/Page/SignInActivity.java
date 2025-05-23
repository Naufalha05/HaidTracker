package Page;

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

import com.example.haidtracker.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Home.SiklusActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private TextView daftarText;

    private final OkHttpClient client = new OkHttpClient();
    private static final String LOGIN_URL = "https://haidtracker.up.railway.app/auth/login";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final String PREFS_NAME = "HaidTrackerPrefs";
    private static final String PREF_TOKEN = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in); // pastikan ini sesuai nama file XML

        // Inisialisasi view
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        rememberMeCheckBox = findViewById(R.id.checkbox);
        loginButton = findViewById(R.id.btnLogin);
        daftarText = findViewById(R.id.daftar);

        // Cek apakah token sudah ada
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString(PREF_TOKEN, null);
        if (token != null) {
            navigateToSiklusActivity();
        }

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Email dan kata sandi wajib diisi.", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        daftarText.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
    }

    private void loginUser(String email, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            Toast.makeText(this, "Gagal membuat data login.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(SignInActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        if (response.isSuccessful()) {
                            String token = jsonResponse.optString("token", null);
                            if (token != null && !token.isEmpty()) {
                                if (rememberMeCheckBox.isChecked()) {
                                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString(PREF_TOKEN, token);
                                    editor.apply();
                                }
                                navigateToSiklusActivity();
                            } else {
                                Toast.makeText(SignInActivity.this, "Token tidak ditemukan di respons.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String error = jsonResponse.optString("message", "Login gagal.");
                            Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(SignInActivity.this, "Kesalahan membaca data dari server.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void navigateToSiklusActivity() {
        Intent intent = new Intent(SignInActivity.this, SiklusActivity.class);
        startActivity(intent);
        finish();
    }
}
