package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}