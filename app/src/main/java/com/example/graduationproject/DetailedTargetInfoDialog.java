package com.example.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailedTargetInfoDialog extends Dialog {
    private Context context;
    private TextView titleTextview;
    private TextView fieldTextview;
    private TextView skillTextview;
    private TextView activityTitleTextview;
    private TextView activityTextview;

    private final String title = "님에 대한 자세한 정보에요.";
    private final String activityTitle = "님은 이런 활동을 해왔어요.";
    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detailed_target_info);

        titleTextview = findViewById(R.id.detailed_title_textview);
        fieldTextview = findViewById(R.id.personal_field_textview);
        skillTextview = findViewById(R.id.personal_skill_textview);
        activityTitleTextview = findViewById(R.id.detailed_activity_title_textview);
        activityTextview = findViewById(R.id.detailed_activity_textview);

        titleTextview.setText(name + title);
        activityTitleTextview.setText(name + activityTitle);

        Button emailButton = findViewById(R.id.email_button);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public DetailedTargetInfoDialog(Context context, String name, String email) {
        super(context);

        this.context = context;
        this.name = name;
        this.email = email;
    }

    public void setFieldTextview(String field) {
        fieldTextview.setText(field);
    }

    public void setSkillTextview(String[] skills) {
        StringBuilder skillsStr = new StringBuilder();
        for (int i = 0; i < skills.length; i++) {
            skillsStr.append(skills[i]);

            if(i < skills.length - 1)
                skillsStr.append(", ");
        }
        skillTextview.setText(skillsStr);
    }

    public void setActivityTextview(String activity) {
        activityTextview.setText(activity);
    }
}
