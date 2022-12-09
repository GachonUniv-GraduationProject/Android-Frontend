package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private Button projectButton;
    private Button favorButton;
    private Button unfavorButton;

    private ProjectExperienceFragment projectExperienceFragment;
    private FavorFieldFragment favorFieldFragment;
    private UnfavorFieldFragment unfavorFieldFragment;

    private List<Experience> experienceList = new ArrayList<>();
    private List<String> preferenceList = new ArrayList<>();
    private List<String> unpreferenceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        projectButton = findViewById(R.id.project_button);
        favorButton = findViewById(R.id.favor_button);
        unfavorButton = findViewById(R.id.unfavor_button);


        projectExperienceFragment = new ProjectExperienceFragment();
        favorFieldFragment = new FavorFieldFragment();
        unfavorFieldFragment = new UnfavorFieldFragment();

        setButtonFocusOn(projectButton);
        getSupportFragmentManager().beginTransaction().replace(R.id.user_info_container, projectExperienceFragment).commit();

        projectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonFocusOn(projectButton);
                setButtonFocusOff(new Button[]{favorButton, unfavorButton});
                getSupportFragmentManager().beginTransaction().replace(R.id.user_info_container, projectExperienceFragment).commit();
            }
        });

        favorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonFocusOn(favorButton);
                setButtonFocusOff(new Button[]{projectButton, unfavorButton});
                getSupportFragmentManager().beginTransaction().replace(R.id.user_info_container, favorFieldFragment).commit();
            }
        });

        unfavorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonFocusOn(unfavorButton);
                setButtonFocusOff(new Button[]{favorButton, projectButton});
                getSupportFragmentManager().beginTransaction().replace(R.id.user_info_container, unfavorFieldFragment).commit();
            }
        });

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesManager.clearPreferences(getApplicationContext());
                MainActivity.mainActivity.finish();
                finish();
            }
        });

        String json = getIntent().getStringExtra("userInfoJson");
        setUserInfo(json);
    }

    private int fieldCheck(String field) {
        for(int i = 0; i < experienceList.size(); i++) {
            if(experienceList.get(i).getField().equals(field))
                return i;
        }
        return -1;
    }

    private void setUserInfo(String json) {
        JsonParser parser = new JsonParser();
        JsonObject rootObj = (JsonObject) parser.parse(json);
        JsonArray experienceArr = (JsonArray) rootObj.get("experience");
        for(int i = 0; i < experienceArr.size(); i++) {
            JsonObject elementObj = (JsonObject)experienceArr.get(i);
            String field = elementObj.get("field").getAsString();
            String detail = elementObj.get("detail").getAsString();

            int fieldIndex = fieldCheck(field);
            if(fieldIndex > 0) {
                experienceList.get(fieldIndex).addProject(detail);
            }
            else
                experienceList.add(new Experience(field, detail));
        }
        projectExperienceFragment.setExperienceList(experienceList);

        JsonArray preferenceArr = (JsonArray) rootObj.get("preference");
        for(int i = 0; i < preferenceArr.size(); i++) {
            JsonObject elementObj = (JsonObject) preferenceArr.get(i);
            if(elementObj.get("preference").getAsInt() >= 0) {
                preferenceList.add(elementObj.get("field").getAsString());
            }
            else {
                unpreferenceList.add(elementObj.get("field").getAsString());
            }
        }
        favorFieldFragment.setFields(preferenceList);
        unfavorFieldFragment.setFields(unpreferenceList);
    }

    private void setButtonFocusOn(Button button) {
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.rectangle_stroke);
        drawable.setColor(getResources().getColor(R.color.primary_color));
        button.setBackground(drawable);
        button.setTextColor(Color.WHITE);
    }
    private void setButtonFocusOff(Button[] buttons) {
        for (Button button : buttons) {
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.rectangle_stroke);
            drawable.setColor(Color.WHITE);
            button.setBackground(drawable);
            button.setTextColor(getResources().getColor(R.color.primary_color));
        }
    }
}