package com.example.graduationproject.trend;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.graduationproject.R;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class RadarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);


        RadarChart radarChart = findViewById(R.id.radarChart);
        ArrayList<RadarEntry> roadMap = new ArrayList<>();
        roadMap.add(new RadarEntry(420));
        roadMap.add(new RadarEntry(347));
        roadMap.add(new RadarEntry(333));
        roadMap.add(new RadarEntry(458));
        roadMap.add(new RadarEntry(584));
        roadMap.add(new RadarEntry(693));

        RadarDataSet radarDataSet = new RadarDataSet(roadMap, "Roadmap");
        radarDataSet.setColor(Color.RED);
        radarDataSet.setLineWidth(2f);
        radarDataSet.setValueTextColor(Color.RED);
        radarDataSet.setValueTextSize(14f);

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);

        String[] labels = {"2014", "2015", "2016", "2017", "2018", "2019", "2020"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        radarChart.setData(radarData);
    }
}