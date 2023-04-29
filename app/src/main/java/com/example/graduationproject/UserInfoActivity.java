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

/**
 * Activity to check user's information
 * */
public class UserInfoActivity extends AppCompatActivity {

    /**
     * Button to select experienced project
     * */
    private Button projectButton;
    /**
     * Button to select favor field
     * */
    private Button favorButton;
    /**
     * Button to select unfavor(non-preferred) field
     * */
    private Button unfavorButton;

    /**
     * Project Experience Fragments to be shown in User Information Activity
     * */
    private ProjectExperienceFragment projectExperienceFragment;
    /**
     * Fragment indicating preferred field
     * */
    private FavorFieldFragment favorFieldFragment;
    /**
     * Fragments representing non-preferred areas
     * */
    private UnfavorFieldFragment unfavorFieldFragment;

    /**
     * List of projects experienced
     * */
    private List<Experience> experienceList = new ArrayList<>();
    /**
     * List of preference field
     * */
    private List<String> preferenceList = new ArrayList<>();
    /**
     * List of non-preferred field
     * */
    private List<String> unpreferenceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Load buttons from xml
        projectButton = findViewById(R.id.project_button);
        favorButton = findViewById(R.id.favor_button);
        unfavorButton = findViewById(R.id.unfavor_button);

        // Initialize fragments
        projectExperienceFragment = new ProjectExperienceFragment();
        favorFieldFragment = new FavorFieldFragment();
        unfavorFieldFragment = new UnfavorFieldFragment();

        // Set as project with default focus
        setButtonFocusOn(projectButton);
        getSupportFragmentManager().beginTransaction().replace(R.id.user_info_container, projectExperienceFragment).commit();

        // Experienced project fragment focus
        projectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonFocusOn(projectButton);
                setButtonFocusOff(new Button[]{favorButton, unfavorButton});
                getSupportFragmentManager().beginTransaction().replace(R.id.user_info_container, projectExperienceFragment).commit();
            }
        });

        // Preferred field fragment focus
        favorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonFocusOn(favorButton);
                setButtonFocusOff(new Button[]{projectButton, unfavorButton});
                getSupportFragmentManager().beginTransaction().replace(R.id.user_info_container, favorFieldFragment).commit();
            }
        });

        // Non-preferred field fragment focus
        unfavorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonFocusOn(unfavorButton);
                setButtonFocusOff(new Button[]{favorButton, projectButton});
                getSupportFragmentManager().beginTransaction().replace(R.id.user_info_container, unfavorFieldFragment).commit();
            }
        });

        // Set logout button listener
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesManager.clearPreferences(getApplicationContext());
                MainActivity.mainActivity.finish();
                finish();
            }
        });

        // Initialize user information by receiving data from previous activities
        String json = getIntent().getStringExtra("userInfoJson");
        setUserInfo(json);
    }

    /**
     * Get index of field in expereince list
     * */
    private int fieldCheck(String field) {
        for(int i = 0; i < experienceList.size(); i++) {
            if(experienceList.get(i).getField().equals(field))
                return i;
        }
        return -1;
    }

    /**
     * Set the user information with json data
     * */
    private void setUserInfo(String json) {
        // Parse root object
        JsonParser parser = new JsonParser();
        JsonObject rootObj = (JsonObject) parser.parse(json);
        JsonArray experienceArr = (JsonArray) rootObj.get("experience");
        // Add experienced project list
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
        // Forward the list of experiences
        projectExperienceFragment.setExperienceList(experienceList);

        // Add preference and non-preference area information
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
        // Forward preference/non-preference area information
        favorFieldFragment.setFields(preferenceList);
        unfavorFieldFragment.setFields(unpreferenceList);
    }

    /**
     * Apply focus effect of specific buttons
     * */
    private void setButtonFocusOn(Button button) {
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.rectangle_stroke);
        drawable.setColor(getResources().getColor(R.color.primary_color));
        button.setBackground(drawable);
        button.setTextColor(Color.WHITE);
    }
    /**
     * Release the focus effect of a particular button
     * */
    private void setButtonFocusOff(Button[] buttons) {
        for (Button button : buttons) {
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.rectangle_stroke);
            drawable.setColor(Color.WHITE);
            button.setBackground(drawable);
            button.setTextColor(getResources().getColor(R.color.primary_color));
        }
    }
}