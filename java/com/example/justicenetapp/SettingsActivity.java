package com.example.justicenetapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar brightnessSeekBar;
    private Switch notificationsSwitch;
    private RadioButton darkThemeRadioButton;
    private RadioButton lightThemeRadioButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        darkThemeRadioButton = findViewById(R.id.darkThemeRadioButton);
        lightThemeRadioButton = findViewById(R.id.lightThemeRadioButton);
        saveButton = findViewById(R.id.saveButton);

        // Set up listeners
        setUpBrightnessControl();
        setUpNotificationsControl();
        setUpThemeControl();
        setUpSaveButton();
    }

    private void setUpBrightnessControl() {
        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Adjust brightness here
                float brightnessValue = progress / 255f; // Normalize to a float between 0 and 1
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = brightnessValue;
                getWindow().setAttributes(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle when the user starts adjusting brightness
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle when the user stops adjusting brightness
            }
        });
    }

    private void setUpNotificationsControl() {
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle notifications switch change
            showNotificationStatus(isChecked);
        });
    }

    private void showNotificationStatus(boolean notificationsEnabled) {
        String message = notificationsEnabled ? "Notifications turned ON" : "Notifications turned OFF";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setUpThemeControl() {
        darkThemeRadioButton.setOnClickListener(v -> {
            // Handle dark theme selection
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            recreate(); // Recreate the activity to apply the theme immediately
        });

        lightThemeRadioButton.setOnClickListener(v -> {
            // Handle light theme selection
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate(); // Recreate the activity to apply the theme immediately
        });
    }

    private void setUpSaveButton() {
        saveButton.setOnClickListener(v -> {
            // Save the user's preferences
            savePreferences();
            finish(); // Close the SettingsActivity
        });
    }

    private void savePreferences() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Save brightness preference
        int brightness = brightnessSeekBar.getProgress();
        editor.putInt("brightness", brightness);

        // Save notifications preference
        boolean notificationsEnabled = notificationsSwitch.isChecked();
        editor.putBoolean("notifications", notificationsEnabled);

        // Save theme preference
        int themeMode = AppCompatDelegate.getDefaultNightMode();
        editor.putInt("theme", themeMode);

        editor.apply();
    }
}
