package com.example.graduationproject.trend;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.graduationproject.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        BarChart barChart = findViewById(R.id.barChart);
        // x 축 값으로 숫자만 할당 가능해서, 해당 기술?의 시간 별 변화량을 보여줄때 사용할 수 있을 것 같음
        ArrayList<BarEntry> trend = new ArrayList<>();
        trend.add(new BarEntry(2014, 420));
        trend.add(new BarEntry(2015, 450));
        trend.add(new BarEntry(2016, 520));
        trend.add(new BarEntry(2017, 620));
        trend.add(new BarEntry(2018, 660));
        trend.add(new BarEntry(2019, 720));

        BarDataSet barDataSet = new BarDataSet(trend, "Trend");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart Example");
        barChart.animateY(2000);

    }
}