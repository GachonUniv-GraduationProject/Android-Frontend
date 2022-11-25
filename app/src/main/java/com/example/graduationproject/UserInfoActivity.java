package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserInfoActivity extends AppCompatActivity {

    private Button projectButton;
    private Button favorButton;
    private Button unfavorButton;

    private ProjectExperienceFragment projectExperienceFragment;
    private FavorFieldFragment favorFieldFragment;
    private UnfavorFieldFragment unfavorFieldFragment;

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