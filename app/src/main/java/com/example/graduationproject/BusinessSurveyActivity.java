package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class BusinessSurveyActivity extends AppCompatActivity {

    private RadioGroup businessFieldRadioGroup;
    private LinearLayout businessSkillCheckboxContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_survey);

        businessFieldRadioGroup = findViewById(R.id.business_field_radio_group);
        businessSkillCheckboxContainer = findViewById(R.id.business_skill_checkbox_container);
    }

    private void loadFields() {

    }

    private void loadSkills() {

    }
}