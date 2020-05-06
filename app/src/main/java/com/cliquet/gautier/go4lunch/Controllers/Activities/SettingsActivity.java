package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

import com.cliquet.gautier.go4lunch.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences preferences;

    ArrayList<Integer> mCheckboxIdList = new ArrayList<>();

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

        preferences = getSharedPreferences("Go4Lunch_Settings", MODE_PRIVATE);
        boolean switchPosition = preferences.getBoolean("switch_position", false);

        bindView();
        setClickOnViewListener();
        setSwitchPosition(switchPosition);

        enablingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSwitchPosition();
            }
        });
    }

    private void setSwitchPosition(boolean switchPosition) {
        enablingSwitch.setChecked(switchPosition);
        checkSwitchPosition();
    }

    private void checkSwitchPosition() {
        if(enablingSwitch.isChecked()) {
            enablingCheckboxes();
        }
        else {
            disablingCheckboxes();
        }
        preferences.edit().putBoolean("switch_position", enablingSwitch.isChecked()).apply();
    }

    @Override
    public void onClick(View view) {
        int idView = view.getId();

        if(view instanceof CheckBox) {
            CheckBox checkbox = findViewById(idView);
            if(checkbox.isChecked()) {
                mCheckboxIdList.add(idView);
            }
            else {
                for(int i = 0; i <= mCheckboxIdList.size(); i++) {
                    if(mCheckboxIdList.get(i) == idView) {
                        mCheckboxIdList.remove(i);
                        break;
                    }
                }
            }
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

    private void setClickOnViewListener() {
        mondayCheckbox.setOnClickListener(this);
        tuesdayCheckBox.setOnClickListener(this);
        wednesdayCheckBox.setOnClickListener(this);
        thursdayCheckBox.setOnClickListener(this);
        fridayCheckBox.setOnClickListener(this);
        saturdayCheckBox.setOnClickListener(this);
        sundayCheckBox.setOnClickListener(this);
    }
}
