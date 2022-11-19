package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.graduationproject.trend.BarChartActivity;
import com.example.graduationproject.trend.BubbleChartActivity;
import com.example.graduationproject.trend.PieChartActivity;
import com.example.graduationproject.trend.RadarChartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    TrendFragment trendFragment;
    RoadmapFragment roadmapFragment;
    CapabilityFragment capabilityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trendFragment = new TrendFragment();
        roadmapFragment = new RoadmapFragment();
        capabilityFragment = new CapabilityFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, trendFragment).commit();
        BottomNavigationView bottomMenu = findViewById(R.id.bottom_menu);
        bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.trend_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, trendFragment).commit();
                        break;
                    case R.id.roadmap_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, roadmapFragment).commit();
                        break;
                    case R.id.capability_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, capabilityFragment).commit();
                        break;
                }
                return false;
            }
        });
    }
}