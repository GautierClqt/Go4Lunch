package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

        enablingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSwitchPosition();
            }
        });
    }

    private void checkSwitchPosition() {
        if(enablingSwitch.isChecked()) {
            enablingCheckboxes();
        }
        else {
            disablingCheckboxes();
        }
    }

    private void enablingCheckboxes() {
        mondayCheckbox.setClickable(true);
        mondayCheckbox.setEnabled(true);
        tuesdayCheckBox.setClickable(true);
        tuesdayCheckBox.setEnabled(true);
        wednesdayCheckBox.setClickable(true);
        wednesdayCheckBox.setEnabled(true);
        thursdayCheckBox.setClickable(true);
        thursdayCheckBox.setEnabled(true);
        fridayCheckBox.setClickable(true);
        fridayCheckBox.setEnabled(true);
        saturdayCheckBox.setClickable(true);
        saturdayCheckBox.setEnabled(true);
        sundayCheckBox.setClickable(true);
        sundayCheckBox.setEnabled(true);
    }

    private void disablingCheckboxes() {
        mondayCheckbox.setClickable(false);
        mondayCheckbox.setEnabled(false);
        tuesdayCheckBox.setClickable(false);
        tuesdayCheckBox.setEnabled(false);
        wednesdayCheckBox.setClickable(false);
        wednesdayCheckBox.setEnabled(false);
        thursdayCheckBox.setClickable(false);
        thursdayCheckBox.setEnabled(false);
        fridayCheckBox.setClickable(false);
        fridayCheckBox.setEnabled(false);
        saturdayCheckBox.setClickable(false);
        saturdayCheckBox.setEnabled(false);
        sundayCheckBox.setClickable(false);
        sundayCheckBox.setEnabled(false);
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
