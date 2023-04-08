package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    static MainActivity mainActivity;

    TrendFragment trendFragment;
    RoadmapFragment roadmapFragment;
    CapabilityFragment capabilityFragment;

    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);

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
        mainActivity = this;
    }
    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }
}