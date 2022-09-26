package com.example.graduationproject.trend;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.graduationproject.R;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;

import java.util.ArrayList;

public class BubbleChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_chart);
        BubbleChart bubbleChart = findViewById(R.id.bubbleChart);
        ArrayList<BubbleEntry> data = new ArrayList<>();

        data.add(new BubbleEntry(10, 10, 10));
        BubbleDataSet bubbleDataSet = new BubbleDataSet(data, "data");
        bubbleDataSet.setValueTextColor(Color.BLACK);
        bubbleDataSet.setColors(Color.RED);
        bubbleDataSet.setValueTextSize(16f);

        BubbleData bubbleData = new BubbleData(bubbleDataSet);
        bubbleChart.setData(bubbleData);
        bubbleChart.getDescription().setEnabled(false);
        bubbleChart.animate();
    }
}