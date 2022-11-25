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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class CapabilityFragment extends Fragment {
    private BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capability, container, false);

        final TextView textView = (TextView)view.findViewById(R.id.recommendActivity);

        ArrayList<BarEntry> entry_chart = new ArrayList<>(); // 데이터를 담을 Arraylist
        ArrayList<String> Xname = new ArrayList<String>();
        ArrayList<Integer> Xvalue = new ArrayList<Integer>();

        Xname.add("예시1"); Xname.add("예시2"); Xname.add("예시3"); Xname.add("예시4");
        Xvalue.add(10); Xvalue.add(20); Xvalue.add(40); Xvalue.add(50);

        barChart = (BarChart)view.findViewById(R.id.chart);
        TextView Xlabels = (TextView)view.findViewById(R.id.x_labels);

        BarData barData = new BarData(); // 차트에 담길 데이터

        entry_chart.add(new BarEntry(1, (int)Xvalue.get(0))); //entry_chart1에 좌표 데이터를 담는다.
        entry_chart.add(new BarEntry(2, (int)Xvalue.get(1)));
        entry_chart.add(new BarEntry(3, (int)Xvalue.get(2)));
        entry_chart.add(new BarEntry(4, (int)Xvalue.get(3)));


        BarDataSet barDataSet = new BarDataSet(entry_chart, "Progress(%)"); // 데이터가 담긴 Arraylist 를 BarDataSet 으로 변환한다.

        barDataSet.setColor(Color.BLUE); // 해당 BarDataSet 색 설정 :: 각 막대 과 관련된 세팅은 여기서 설정한다.
        barData.addDataSet(barDataSet); // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
        barChart.setData(barData); // 차트에 위의 DataSet 을 넣는다.
        barChart.invalidate(); // 차트 업데이트
        barChart.setTouchEnabled(false); // 차트 터치 불가능하게
        barChart.animateY(1000);

        String tmp0 = "";
        for(int i=0; i<Xname.size(); i++) {
            Object obj = Xname.get(i);
            String str = (String)obj;
            tmp0 += String.format("%20s", str);
        }
        tmp0 += "\n";
        Xlabels.setText(tmp0);

        ArrayList act = new ArrayList();
        String tmp = "";

        //나중에 로드맵에서 스트링을 가져와서 add해서 출력하면 될듯요
        act.add("Java 프로그래밍 해보기");
        act.add("DJANGO 연습하기");

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
}