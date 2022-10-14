package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class CapacityActivity extends AppCompatActivity {
    private BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacity_activity);
        final TextView textView = (TextView)findViewById(R.id.recommendActivity);

        ArrayList<BarEntry> entry_chart = new ArrayList<>(); // 데이터를 담을 Arraylist
        ArrayList<String> Xname = new ArrayList<String>();
        ArrayList<Integer> Xvalue = new ArrayList<Integer>();

        Xname.add("예시1"); Xname.add("예시2"); Xname.add("예시3"); Xname.add("예시4");
        Xvalue.add(10); Xvalue.add(20); Xvalue.add(40); Xvalue.add(50);

        barChart = (BarChart)findViewById(R.id.chart);
        TextView Xlabels = (TextView)findViewById(R.id.x_labels);

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
        barChart.animateY(2000);

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
    }
}