package com.example.graduationproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class CapabilityFragment extends Fragment {
    private BarChart barChart;
    private ArrayList<Integer> dataList;
    private ArrayList<String> labelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capability, container, false);

        final TextView textView = (TextView)view.findViewById(R.id.recommendActivity);

        dataList = new ArrayList<>();
        labelList = new ArrayList<>();

        barChart = (BarChart) view.findViewById(R.id.chart);
        setGraphData();

        ArrayList act = new ArrayList();
        String tmp = "";

        //나중에 로드맵에서 스트링을 가져와서 add해서 출력하면 될듯요
        act.add("Java 프로그래밍 해보기");
        act.add("Django 연습하기");

        for(int i=0; i<act.size(); i++) {
            Object obj = act.get(i);
            String str = (String)obj;
            tmp += "- ";
            tmp += str;
            tmp += "\n";
        }

        textView.setText(tmp);

        Button myPageButton = view.findViewById(R.id.user_info_button);
        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myPageIntent = new Intent(getContext(), UserInfoActivity.class);
                startActivity(myPageIntent);
            }
        });

        return view;
    }

    private void setGraphData() {
        labelList.add("프론트엔드");
        labelList.add("안드로이드");
        labelList.add("게임 개발");
        labelList.add("백엔드");

        dataList.add(50);
        dataList.add(30);
        dataList.add(20);
        dataList.add(4);

        barChartGraph(labelList, dataList);
        barChart.setTouchEnabled(false);
        barChart.getAxisLeft().setAxisMaximum(100);
    }

    private void barChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < valList.size(); i++) {
            entries.add(new BarEntry(i, (Integer)valList.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "진행도");

        ArrayList<String> labels = new ArrayList<String>();
        for(int i = 0; i < labelList.size(); i++)
            labels.add((String) labelList.get(i));

        BarData data = new BarData();
        data.addDataSet(dataSet);
        dataSet.setColors(getResources().getIntArray(R.array.bar_chart_color_arr));
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setValueTextSize(10);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);

        YAxis yAxis = barChart.getAxisRight();
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);

        barChart.setData(data);
        barChart.animateXY(500, 500);
        barChart.invalidate();
    }
}