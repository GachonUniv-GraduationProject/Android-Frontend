package com.example.graduationproject;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class RoadmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap);

        LinearLayout roadmapContainer = findViewById(R.id.roadmap_container);
        //RoadmapDrawer drawer = new RoadmapDrawer(this);
        //roadmapContainer.addView(drawer);
    }
}