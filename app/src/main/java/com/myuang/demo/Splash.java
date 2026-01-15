package com.myuang.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.os.LocaleListCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.util.Locale;

public class Splash extends AppCompatActivity {

    private static final String TAG = "LocationDebug";
    private static final String EXTRA_SHOW_FINAL_STATE = "SHOW_FINAL_STATE";
    private static final String EXTRA_COUNTRY_NAME = "COUNTRY_NAME";

    private int currentPhase = 1;
    private static final int LOGO_SPLASH_DELAY = 2000;
    private static final int LOCATION_TIMEOUT_MS = 10000; // 10 seconds
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private RecyclerView flagsRecyclerView;

    private final Handler timeoutHandler = new Handler(Looper.getMainLooper());
    private boolean isLocationHandled = false;

    private final int[] flagDrawables = {R.drawable.indonesia_flag, R.drawable.usa_flag, R.drawable.china_flag, R.drawable.japan_flag};
    private final String[] countryNames = {"Indonesia", "United States", "China", "Japan"};
    private final String[] languageTags = {"in", "en", "zh", "ja"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra(EXTRA_SHOW_FINAL_STATE, false)) {
            String countryName = getIntent().getStringExtra(EXTRA_COUNTRY_NAME);
            displayFinalScreen(countryName);
        } else {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            showPhase(currentPhase);
        }
    }

    private void showPhase(int phase) {
        this.currentPhase = phase;
        switch (phase) {
            case 1:
                setContentView(R.layout.activity_splash_logo);
                new Handler().postDelayed(() -> showPhase(2), LOGO_SPLASH_DELAY);
                break;
            case 2:
                setContentView(R.layout.activity_splash);
                findViewById(R.id.nextButton).setOnClickListener(v -> showPhase(3));
                break;
            case 3:
                setContentView(R.layout.activity_splash_location);
                setupRecyclerView();
                startFlagAnimation();
                detectLocation();
                break;
        }
    }

    private void setupRecyclerView() {
        flagsRecyclerView = findViewById(R.id.flags_recyclerview);
        if (flagsRecyclerView != null) {
            flagsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            flagsRecyclerView.setAdapter(new FlagAdapter(flagDrawables));
            flagsRecyclerView.setOnTouchListener((v, event) -> true); // Disable user interaction
        }
    }

    private void startFlagAnimation() {
        final Handler animationHandler = new Handler(Looper.getMainLooper());
        animationHandler.post(new Runnable() {
            @Override
            public void run() {
                if (flagsRecyclerView != null && !isLocationHandled) {
                    flagsRecyclerView.scrollBy(15, 0);
                    animationHandler.postDelayed(this, 16); // ~60fps
                }
            }
        });
    }

    private void detectLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        Log.d(TAG, "Starting location detection with a " + LOCATION_TIMEOUT_MS + "ms timeout.");
        isLocationHandled = false;

        // Start a timeout timer
        timeoutHandler.postDelayed(() -> {
            if (!isLocationHandled) {
                Log.w(TAG, "Location detection timed out! Proceeding with default.");
                handleLocationResult(null);
            }
        }, LOCATION_TIMEOUT_MS);

        mFusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener(this, this::handleLocationResult)
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "FusedLocationProvider failed", e);
                    handleLocationResult(null);
                });
    }

    private void handleLocationResult(android.location.Location location) {
        if (isLocationHandled) {
            return;
        }
        isLocationHandled = true;
        timeoutHandler.removeCallbacksAndMessages(null);

        String detectedCountry = "Indonesia"; // Default country

        if (location != null) {
            Log.d(TAG, "Location object: " + location);
            try {
                Geocoder geocoder = new Geocoder(Splash.this, Locale.getDefault());
                detectedCountry = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getCountryName();
                Log.d(TAG, "Geocoder returned country: " + detectedCountry);
            } catch (IOException | IndexOutOfBoundsException e) {
                Log.e(TAG, "Geocoder failed, falling back to default.", e);
            }
        } else {
            Log.w(TAG, "Location object was null, falling back to default.");
        }

        restartActivityForLocale(detectedCountry);
    }


    private void restartActivityForLocale(String detectedCountryName) {
        String languageTag = "in"; // Default to Indonesian
        for (int i = 0; i < countryNames.length; i++) {
            if (countryNames[i].equalsIgnoreCase(detectedCountryName)) {
                languageTag = languageTags[i];
                break;
            }
        }

        setAppLocale(languageTag);

        Intent intent = new Intent(this, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_SHOW_FINAL_STATE, true);
        intent.putExtra(EXTRA_COUNTRY_NAME, detectedCountryName);
        startActivity(intent);
        finish();
    }

    private void displayFinalScreen(String detectedCountryName) {
        setContentView(R.layout.activity_splash_location);

        // Hide animation views and show final content
        View flagsRecyclerView = findViewById(R.id.flags_recyclerview);
        if(flagsRecyclerView != null) flagsRecyclerView.setVisibility(View.GONE);
        View subtitle = findViewById(R.id.detecting_location_subtitle);
        if(subtitle != null) subtitle.setVisibility(View.GONE);
        View title = findViewById(R.id.detecting_location_title);
        if(title != null) title.setVisibility(View.GONE);
        View underline = findViewById(R.id.title_underline);
        if(underline != null) underline.setVisibility(View.GONE);

        ImageView finalFlag = findViewById(R.id.final_flag_view);
        TextView greetingTextView = findViewById(R.id.greeting_text);
        TextView locationDetectedTextView = findViewById(R.id.location_detected_text);
        ImageView nextButton = findViewById(R.id.nextButton);

        int finalFlagResId = R.drawable.indonesia_flag;
        String countryNameForDisplay = "Indonesia";

        for (int i = 0; i < countryNames.length; i++) {
            if (countryNames[i].equalsIgnoreCase(detectedCountryName)) {
                finalFlagResId = flagDrawables[i];
                countryNameForDisplay = countryNames[i];
                break;
            }
        }

        finalFlag.setImageResource(finalFlagResId);

        greetingTextView.setText(getString(R.string.greeting));
        String locationText = getString(R.string.location_detected, countryNameForDisplay, getString(R.string.language_name));
        locationDetectedTextView.setText(locationText);

        finalFlag.setVisibility(View.VISIBLE);
        greetingTextView.setVisibility(View.VISIBLE);
        locationDetectedTextView.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);

        nextButton.setOnClickListener(v -> {
            startActivity(new Intent(Splash.this, SplashTrackingActivity.class));
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                detectLocation();
            } else {
                Log.w(TAG, "Location permission denied.");
                handleLocationResult(null);
            }
        }
    }

    private void setAppLocale(String languageTag) {
        Log.d(TAG, "Setting app locale to: " + languageTag);
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(languageTag);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeoutHandler.removeCallbacksAndMessages(null);
    }
}
