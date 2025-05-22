package Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.haidtracker.R;

import java.io.RandomAccessFile;

public class SiklusActivity extends AppCompatActivity {

    ImageView iconCalendar, iconAnalisis, iconProfil, iconContent, IconPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siklus); // Ganti sesuai nama file XML kamu

       iconCalendar=findViewById(R.id.menu_calender);
       iconAnalisis=findViewById(R.id.analisis);
       iconContent=findViewById(R.id.content);
       iconProfil=findViewById(R.id.menu_icon);

        // Navigasi ke CalendarActivity
        iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SiklusActivity.this, CalenderActivity.class));
            }
        });

        // Navigasi ke AnalisisActivity
        iconAnalisis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SiklusActivity.this, AnalisisActivity.class));
            }
        });

        // Navigasi ke ProfilActivity
        iconProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SiklusActivity.this, ProfilActivity.class));
            }
        });
    }
}
