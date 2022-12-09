package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TalentRecommendFoldingCell extends LinearLayout {

    private boolean detailedOpened = false;
    private LinearLayout detailedContainer;
    private ImageView foldNotifyImage;

    private TextView nameTextview;
    private TextView fieldTextview;
    private TextView skillTextview;
    private TextView experienceTextview;

    private String name;
    private String email;
    private String field;
    private String[] skills;
    private String experience;

    public TalentRecommendFoldingCell(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.talent_recommend_foldingcell, this, true);

        detailedContainer = findViewById(R.id.detailed_container);
        foldNotifyImage = findViewById(R.id.fold_notify_imageview);
        nameTextview = findViewById(R.id.personal_name_textview);
        fieldTextview = findViewById(R.id.personal_field_textview);
        skillTextview = findViewById(R.id.personal_skill_textview);
        experienceTextview = findViewById(R.id.personal_experience_textview);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setDetailedInfo();
            }
        });

    }

    private void setDetailedInfo() {
        detailedOpened = !detailedOpened;

        if(detailedOpened) {
            detailedContainer.setVisibility(View.VISIBLE);
            foldNotifyImage.setRotation(180);
        }
        else {
            detailedContainer.setVisibility(View.GONE);
            foldNotifyImage.setRotation(0);
        }
    }

    private String getArrangedExperience(String sentence) {
        String result = sentence.replaceAll(".", ".\n");

        return result;
    }

    public void updateUserInfo(String name, String email, String field, String[] skill, String experience) {
        this.name = name;
        this.email = email;
        this.field = field;
        this.skills = skill;
        this.experience = experience;

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

    private void setDetailInfo() {
        Button detailButton = findViewById(R.id.detailed_info_button);
        detailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailedTargetInfoDialog detailedTargetInfoDialog = new DetailedTargetInfoDialog(getContext(), name, email);
                detailedTargetInfoDialog.show();
                detailedTargetInfoDialog.setFieldTextview(field);
                detailedTargetInfoDialog.setSkillTextview(skills);
                detailedTargetInfoDialog.setActivityTextview(experience);
            }
        });
    }
}
