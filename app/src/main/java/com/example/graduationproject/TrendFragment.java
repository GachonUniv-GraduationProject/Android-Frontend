package com.example.graduationproject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class TrendFragment extends Fragment {

    private FieldCategory currentTrendCategory = FieldCategory.WEB_FRONTEND;
    private List<FieldCategory> bookmarkedCategory = null;
    /* Trend Data를 어떤 자료구조로 보관할지 */

    private View thisView;
    private TextView[] categoryTextviewArray;
    private int currentCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_trend, container, false);

        PieChart pieChart = thisView.findViewById(R.id.pieChart);
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

        init();

        setCategoryListener();

        return thisView;
    }

    private void init() {
        int[] categoryArr = {R.id.trend_category_0, R.id.trend_category_1, R.id.trend_category_2,
                R.id.trend_category_3, R.id.trend_category_4, R.id.trend_category_5};
        categoryTextviewArray = new TextView[categoryArr.length];
        for(int i = 0; i < categoryArr.length; i++){
            categoryTextviewArray[i] = thisView.findViewById(categoryArr[i]);
        }
    }

    private void setCategoryListener() {
        for(int i = 0; i < categoryTextviewArray.length; i++) {
            final int c = i;
            categoryTextviewArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    categoryTextviewArray[currentCategory].setBackground(null);
                    currentCategory = c;
                    categoryTextviewArray[currentCategory].setBackgroundResource(R.drawable.current_category_background);
                }
            });
        }
    }

    private void loadTrends(/* Trend Data parameter */) {

    }

}