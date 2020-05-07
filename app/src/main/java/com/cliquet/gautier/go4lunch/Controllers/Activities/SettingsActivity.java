package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

import com.cliquet.gautier.go4lunch.R;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences preferences;

    ArrayList<Integer> mCheckboxIdList = new ArrayList<>();
    ArrayList<Integer> mCheckBoxPositionList = new ArrayList<>();

    Switch enablingSwitch;
    CheckBox mondayCheckbox;
    CheckBox tuesdayCheckBox;
    CheckBox wednesdayCheckBox;
    CheckBox thursdayCheckBox;
    CheckBox fridayCheckBox;
    CheckBox saturdayCheckBox;
    CheckBox sundayCheckBox;

    String jsonCheckBoxId;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("Go4Lunch_Settings", MODE_PRIVATE);
        boolean switchPosition = preferences.getBoolean("switch_position", false);

        bindView();
        setClickOnViewListener();
        initCheckboxesLists();
        setSwitchPosition(switchPosition);
        setCheckboxesStates();

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

    private void setCheckboxesStates() {
        if(mCheckboxIdList == null) {
            for (int i = 0; i < mCheckBoxPositionList.size(); i++) {
                mCheckboxIdList.add(null);
            }
        }
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

    private void initCheckboxesLists() {
        //init mCheckBoxPositionList
        mCheckBoxPositionList.add(mondayCheckbox.getId());
        mCheckBoxPositionList.add(tuesdayCheckBox.getId());
        mCheckBoxPositionList.add(wednesdayCheckBox.getId());
        mCheckBoxPositionList.add(thursdayCheckBox.getId());
        mCheckBoxPositionList.add(fridayCheckBox.getId());
        mCheckBoxPositionList.add(saturdayCheckBox.getId());
        mCheckBoxPositionList.add(sundayCheckBox.getId());

        //init mCheckBoxIdList
        String jsonCheckBoxId = preferences.getString("checkboxes_id_list", null);
        mCheckboxIdList = gson.fromJson(jsonCheckBoxId, new TypeToken<ArrayList<Integer>>(){}.getType());
    }

    private void putCheckboxesAtRightPosition(int idView, Boolean check) {
        for(int i = 0; i < mCheckBoxPositionList.size(); i++) {
            for(int j = 0; j < mCheckBoxPositionList.size(); j++) {
                if(mCheckBoxPositionList.get(i) == idView) {
                    if(check) {
                        mCheckboxIdList.set(i, idView);
                    }
                    else{
                        mCheckboxIdList.set(i, null);
                    }
                }
            }
        }
        saveCheckboxesIdList();
    }

    private void saveCheckboxesIdList() {
        jsonCheckBoxId = gson.toJson(mCheckboxIdList);
        preferences.edit().putString("checkboxes_id_list", jsonCheckBoxId).apply();
    }

    @Override
    public void onClick(View view) {
        int idView = view.getId();

        if(view instanceof CheckBox) {
            CheckBox checkbox = findViewById(idView);
            Boolean check = checkbox.isChecked();

            putCheckboxesAtRightPosition(idView, check);
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
