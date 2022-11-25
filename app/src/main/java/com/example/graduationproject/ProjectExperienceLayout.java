package com.example.graduationproject;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;

public class ProjectExperienceLayout extends LinearLayout {

    private Context context;

    private TextView fieldTextview;
    private LinearLayout projectContainer;

    public ProjectExperienceLayout(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_project_experience, this, true);
        this.context = context;

        fieldTextview = findViewById(R.id.field_title_Text);
        projectContainer = findViewById(R.id.project_container);
    }

    public void setField(String field) {
        fieldTextview.setText(field);
    }

    public void addProject(String[] projects) {
        for(int i = 0; i < projects.length; i++) {
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, dpToPx(5), 0, 0);
            textView.setLayoutParams(params);
            textView.setText(projects[i]);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(Dimension.SP, 14);
            projectContainer.addView(textView);
        }
    }
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
