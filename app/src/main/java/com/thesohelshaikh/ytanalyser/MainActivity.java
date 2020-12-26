package com.thesohelshaikh.ytanalyser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final String API_KEY = "AIzaSyDR6pW44r1itzKNBJg3U0mXOkbZCoXhkhE";
    private final String code = "-QMg39gK624";
    EditText edURL;
    Button btnAnalyse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edURL = findViewById(R.id.ed_url);
        btnAnalyse = findViewById(R.id.btn_analyse);

        btnAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), edURL.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}