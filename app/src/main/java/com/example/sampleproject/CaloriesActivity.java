package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class CaloriesActivity extends AppCompatActivity {

    private CircularProgressIndicator progressBar;
    private TextView currentCountTextView;
    private TextView overallCountTextView;
    private EditText etCurrentCount;
    private EditText etTotalCount;
    private Button btnUpdateCurrent;
    private Button btnUpdateTotal;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        // initiallize progressbar information
        progressBar = findViewById(R.id.progress_bar);
        etCurrentCount = findViewById(R.id.etCurrentCount);
        etTotalCount = findViewById(R.id.etTotalCount);
        btnUpdateCurrent = findViewById(R.id.btnUpdateCurrent);
        btnUpdateTotal = findViewById(R.id.btnUpdateTotal);

        btnUpdateCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentCount();
            }

            private void updateCurrentCount() {
                String totalCountStr = etTotalCount.getText().toString();
                if (!totalCountStr.isEmpty()) {
                    overallCountTextView.setText(totalCountStr);
                    // Update progress bar to reflect new total count
                    String currentCountStr = currentCountTextView.getText().toString();
                    int currentCount = Integer.parseInt(currentCountStr);
                    int totalCount = Integer.parseInt(totalCountStr);
                    int progress = (int) ((currentCount / (float) totalCount) * 100);
                    progressBar.setProgress(progress);
                }
            }
        });

        btnUpdateTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTotalCount();
            }

            private void updateTotalCount() {
                String totalCountStr = etTotalCount.getText().toString();
                if (!totalCountStr.isEmpty()) {
                    overallCountTextView.setText(totalCountStr);
                    // Update progress bar to reflect new total count
                    String currentCountStr = currentCountTextView.getText().toString();
                    int currentCount = Integer.parseInt(currentCountStr);
                    int totalCount = Integer.parseInt(totalCountStr);
                    int progress = (int) ((currentCount / (float) totalCount) * 100);
                    progressBar.setProgress(progress);
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_calories);

        // swapping between windows
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_calories)
                return true;
            else if (item.getItemId() == R.id.bottom_weather) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_database) {
                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_training) {
                startActivity(new Intent(getApplicationContext(), TrainingActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }
}