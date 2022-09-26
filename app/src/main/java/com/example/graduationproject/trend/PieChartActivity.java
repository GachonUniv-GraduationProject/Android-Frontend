package com.example.graduationproject.trend;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.graduationproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        PieChart pieChart = findViewById(R.id.pieChart);
        // 동일한 분야? 에서 어떤 항목이 우세하고 많이 쓰였는지를 보여주는데 좋을 것 같다.
        ArrayList<PieEntry> trend = new ArrayList<>();
        trend.add(new PieEntry(508, "go"));
        trend.add(new PieEntry(620, "express.js"));
        trend.add(new PieEntry(750, "django"));
        trend.add(new PieEntry(450, "flask"));
        trend.add(new PieEntry(850, "spring"));

        PieDataSet pieDataSet = new PieDataSet(trend, "Trend");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Trend");
        pieChart.animate();

    }
}