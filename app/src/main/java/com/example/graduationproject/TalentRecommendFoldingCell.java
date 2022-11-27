package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TalentRecommendFoldingCell extends LinearLayout {

    private boolean detailedOpened = false;
    private LinearLayout detailedContainer;
    private Animation containerDisableAnim, containerEnableAnim, triangleRotationAnim;
    private ImageView foldNotifyImage;

    public TalentRecommendFoldingCell(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.talent_recommend_foldingcell, this, true);

        detailedContainer = findViewById(R.id.detailed_container);
        foldNotifyImage = findViewById(R.id.fold_notify_imageview);
        containerDisableAnim = AnimationUtils.loadAnimation(getContext(), R.anim.container_disable);
        containerEnableAnim = AnimationUtils.loadAnimation(getContext(), R.anim.container_enable);
        triangleRotationAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotation_180);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setDetailedInfo();
            }
        });

        Button detailButton = findViewById(R.id.detailed_info_button);
        detailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "홍길동", email = "abc@gmail.com";
                DetailedTargetInfoDialog detailedTargetInfoDialog = new DetailedTargetInfoDialog(getContext(), name, email);
                detailedTargetInfoDialog.show();
                detailedTargetInfoDialog.setFieldTextview("안드로이드 앱 개발");
                detailedTargetInfoDialog.setSkillTextview(new String[] {"Java", "Kotlin", "Retrofit2"});
                detailedTargetInfoDialog.setActivityTextview("Java, Retrofit을 활용한 AAA 프로젝트를 경험함.");
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
}
