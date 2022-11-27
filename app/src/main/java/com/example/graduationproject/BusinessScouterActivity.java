package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BusinessScouterActivity extends AppCompatActivity {

    private LinearLayout recommendFieldContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_scouter);

        recommendFieldContainer = findViewById(R.id.recommend_field_container);

        TalentRecommendFoldingCell talentRecommendFoldingCell = new TalentRecommendFoldingCell(this);
        TalentRecommendFoldingCell talentRecommendFoldingCell1 = new TalentRecommendFoldingCell(this);
        recommendFieldContainer.addView(talentRecommendFoldingCell);
        recommendFieldContainer.addView(talentRecommendFoldingCell1);

        Button filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent businessSurveyIntent = new Intent(getApplicationContext(), BusinessSurveyActivity.class);
                startActivity(businessSurveyIntent);
            }
        });

    }
}