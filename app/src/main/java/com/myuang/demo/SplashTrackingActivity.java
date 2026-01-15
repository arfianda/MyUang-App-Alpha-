package com.myuang.demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashTrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_tracking);

        findViewById(R.id.nextButton).setOnClickListener(v -> {
            startActivity(new Intent(SplashTrackingActivity.this, MainActivity.class));
            finish();
        });
    }
}
