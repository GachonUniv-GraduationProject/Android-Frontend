package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 *
 * */
public class MainActivity extends AppCompatActivity {

    /**
     * Singleton instance for main activity
     * */
    static MainActivity mainActivity;

    /**
     * Partial screen for trend
     * */
    TrendFragment trendFragment;
    /**
     * Partial screen for roadmap
     * */
    RoadmapFragment roadmapFragment;
    /**
     * Partial screen for capability
     * */
    CapabilityFragment capabilityFragment;

    /**
     * Back key handler for shutting down app
     * */
    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the fragments
        trendFragment = new TrendFragment();
        roadmapFragment = new RoadmapFragment();
        capabilityFragment = new CapabilityFragment();

        // Show the default fragment (trend fragment)
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, trendFragment).commit();
        // Set the bottom bar for switching the fragment
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

        // Set singleton main activity instance
        mainActivity = this;
    }

    /**
     * Back key event process
     * */
    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }
}