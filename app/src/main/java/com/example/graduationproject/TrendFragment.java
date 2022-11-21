package com.example.graduationproject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrendFragment extends Fragment {

    private FieldCategory currentTrendCategory = FieldCategory.WEB_FRONTEND;
    private List<FieldCategory> bookmarkedCategory = null;
    /* Trend Data를 어떤 자료구조로 보관할지 */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trend, container, false);

        PieChart pieChart = view.findViewById(R.id.pieChart);
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

        return view;
    }

    private void setCategoryListener() {

    }

    private void loadTrends(/* Trend Data parameter */) {

    }

}