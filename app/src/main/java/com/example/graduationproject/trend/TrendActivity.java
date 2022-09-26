package com.example.graduationproject.trend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.graduationproject.R;

public class TrendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);

        Button btn_barChart = findViewById(R.id.btn_barChart);
        Button btn_pieChart = findViewById(R.id.btn_pieChart);
        Button btn_radarChart = findViewById(R.id.btn_radarChart);
        Button btn_bubbleChart = findViewById(R.id.btn_bubbleChart);
        btn_barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), BarChartActivity.class));

            }
        });
        btn_pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PieChartActivity.class));

            }
        });
        btn_radarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RadarChartActivity.class));
            }
        });
        btn_bubbleChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BubbleChartActivity.class));
            }
        });


    }
}