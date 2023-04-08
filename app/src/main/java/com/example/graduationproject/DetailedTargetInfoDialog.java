package com.example.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * A dialog showing the details of the talented user
 * */
public class DetailedTargetInfoDialog extends Dialog {
    /**
     * Context of dialog
     * */
    private Context context;
    /**
     * Title textview of dialog
     * */
    private TextView titleTextview;
    /**
     * Field textview of user
     * */
    private TextView fieldTextview;
    /**
     * Skill textview of user
     * */
    private TextView skillTextview;
    /**
     * Textview that shows user experienced activity title
     * */
    private TextView activityTitleTextview;
    /**
     * Textview that shows user experienced activity
     * */
    private TextView activityTextview;

    /**
     * Const string of dialog title
     * */
    private final String title = "님에 대한 자세한 정보에요.";
    /**
     * Const string of activity title
     * */
    private final String activityTitle = "님은 이런 활동을 해왔어요.";
    /**
     * Name of user
     * */
    private String name;
    /**
     * E-mail of user
     * */
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detailed_target_info);

        initUI();
    }

    /**
     * Initialize UI by connecting views from xml
     * */
    private void initUI()
    {
        // Load Textview from xml
        titleTextview = findViewById(R.id.detailed_title_textview);
        fieldTextview = findViewById(R.id.personal_field_textview);
        skillTextview = findViewById(R.id.personal_skill_textview);
        activityTitleTextview = findViewById(R.id.detailed_activity_title_textview);
        activityTextview = findViewById(R.id.detailed_activity_textview);

        // Update the title textview
        titleTextview.setText(name + title);
        activityTitleTextview.setText(name + activityTitle);

        // Set email button click listener
        Button emailButton = findViewById(R.id.email_button);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the title of email
                String mailName = "[Devigation] ";
                mailName += LoginData.currentLoginData.getUser().getDisplayName() + "에서 관심이 있어 연락드립니다.";

                // Target email address
                String[] address = {email};

                // Start Email intent
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("plain/text");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, address);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, mailName);
                getContext().startActivity(mailIntent);
                dismiss();
            }
        });

        // Set closing button click listener
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

    /**
     * Update the field of user
     * */
    public void setFieldTextview(String field) {
        fieldTextview.setText(field);
    }

    /**
     * Update the skills of user
     * */
    public void setSkillTextview(String[] skills) {
        StringBuilder skillsStr = new StringBuilder();
        for (int i = 0; i < skills.length; i++) {
            skillsStr.append(skills[i]);

            if(i < skills.length - 1)
                skillsStr.append(", ");
        }
        skillTextview.setText(skillsStr);
    }

    /**
     * Update the activity of user
     * */
    public void setActivityTextview(String activity) {
        activityTextview.setText(activity);
    }
}
