package com.example.graduationproject;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;

/**
 * Layout preset for showing experience record
 * */
public class ProjectExperienceLayout extends LinearLayout {

    /**
     * Context of this layout
     * */
    private Context context;

    /**
     * Textview showing that the field to which this experience belongs
     * */
    private TextView fieldTextview;
    /**
     * Container of project data
     * */
    private LinearLayout projectContainer;

    public ProjectExperienceLayout(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_project_experience, this, true);
        this.context = context;

        // Load views from xml
        fieldTextview = findViewById(R.id.field_title_Text);
        projectContainer = findViewById(R.id.project_container);
    }

    /**
     * Set the field of this experience
     * */
    public void setField(String field) {
        fieldTextview.setText(field);
    }

    /**
     * Add project to container
     * */
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
    /**
     * Convert dp to pixel size
     * */
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
