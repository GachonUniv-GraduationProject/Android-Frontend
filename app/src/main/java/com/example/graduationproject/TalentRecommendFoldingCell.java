package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Folding Cells representing talented user information recommended to business members
 * */
public class TalentRecommendFoldingCell extends LinearLayout {

    /**
     * Whether the folding cell is currently open
     * */
    private boolean detailedOpened = false;
    /**
     * Container for containing details
     * */
    private LinearLayout detailedContainer;
    /**
     * ImageView indicating folded state
     * */
    private ImageView foldNotifyImage;

    /**
     * Textview showing name of talented user
     * */
    private TextView nameTextview;
    /**
     * Textview showing field of talented user
     * */
    private TextView fieldTextview;
    /**
     * Textview showing skill information held by talented user
     * */
    private TextView skillTextview;
    /**
     * Textview showing experiences that talented user experienced
     * */
    private TextView experienceTextview;

    /**
     * Name of talented user
     * */
    private String name;
    /**
     * Email of talented user
     * */
    private String email;
    /**
     * Field of talented user
     * */
    private String field;
    /**
     * Skill list(array) of talented user
     * */
    private String[] skills;
    /**
     * Experiences that talented user experienced
     * */
    private String experience;

    public TalentRecommendFoldingCell(Context context) {
        super(context);

        // Inflate this layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.talent_recommend_foldingcell, this, true);

        // Load views and button from xml
        detailedContainer = findViewById(R.id.detailed_container);
        foldNotifyImage = findViewById(R.id.fold_notify_imageview);
        nameTextview = findViewById(R.id.personal_name_textview);
        fieldTextview = findViewById(R.id.personal_field_textview);
        skillTextview = findViewById(R.id.personal_skill_textview);
        experienceTextview = findViewById(R.id.personal_experience_textview);

        // Set the listener showing detailed information
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setDetailedInfo();
            }
        });

    }

    /**
     * Fold or open this folding cell
     * */
    private void setDetailedInfo() {
        // Switch open state
        detailedOpened = !detailedOpened;

        // Open the folding cell
        if(detailedOpened) {
            detailedContainer.setVisibility(View.VISIBLE);
            foldNotifyImage.setRotation(180);
        }
        // Close(Fold) the folding cell
        else {
            detailedContainer.setVisibility(View.GONE);
            foldNotifyImage.setRotation(0);
        }
    }

    /**
     * A sentence divided by . is divided into new lines.
     * */
    private String getArrangedExperience(String sentence) {
        String result = sentence.replaceAll(".", ".\n");

        return result;
    }

    /**
     * Update the talented user information
     * */
    public void updateUserInfo(String name, String email, String field, String[] skill, String experience) {
        // Set the user spec
        this.name = name;
        this.email = email;
        this.field = field;
        this.skills = skill;
        this.experience = experience;

        // Update UI contents
        nameTextview.setText(name);
        fieldTextview.setText(field);
        String skillText = "";
        for(int i = 0; i < skill.length; i++) {
            skillText += skill[i];
            if(i < skill.length - 1)
                skillText += ", ";
        }
        skillTextview.setText(skillText);
        experienceTextview.setText(experience);

        setDetailInfo();
    }

    /**
     * Set up to view talent details
     * */
    private void setDetailInfo() {
        // Set the detail button listener
        Button detailButton = findViewById(R.id.detailed_info_button);
        detailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the detailed talented target information dialog
                DetailedTargetInfoDialog detailedTargetInfoDialog = new DetailedTargetInfoDialog(getContext(), name, email);
                detailedTargetInfoDialog.show();
                detailedTargetInfoDialog.setFieldTextview(field);
                detailedTargetInfoDialog.setSkillTextview(skills);
                detailedTargetInfoDialog.setActivityTextview(experience);
            }
        });
    }
}
