package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Switch;

import com.cliquet.gautier.go4lunch.R;

public class SettingsActivity extends AppCompatActivity {

    Switch enablingSwitch;
    CheckBox mondayCheckbox;
    CheckBox tuesdayCheckBox;
    CheckBox wednesdayCheckBox;
    CheckBox thursdayCheckBox;
    CheckBox fridayCheckBox;
    CheckBox saturdayCheckBox;
    CheckBox sundayCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bindView();
        checkSwitchPosition();
    }

    private void checkSwitchPosition() {
    }

    private void bindView() {
        enablingSwitch = findViewById(R.id.activity_settings_switch);
        mondayCheckbox = findViewById(R.id.activity_settings_monday_checkbox);
        tuesdayCheckBox = findViewById(R.id.activity_settings_tuesday_checkbox);
        wednesdayCheckBox = findViewById(R.id.activity_settings_wednesday_checkbox);
        thursdayCheckBox = findViewById(R.id.activity_settings_thursday_checkbox);
        fridayCheckBox = findViewById(R.id.activity_settings_friday_checkbox);
        saturdayCheckBox = findViewById(R.id.activity_settings_saturday_checkbox);
        sundayCheckBox = findViewById(R.id.activity_settings_sunday_checkbox);
    }
}
