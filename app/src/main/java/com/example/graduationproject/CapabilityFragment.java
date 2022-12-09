package com.example.graduationproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CapabilityFragment extends Fragment {
    private BarChart barChart;
    private ArrayList<Integer> dataList;
    private ArrayList<String> labelList;

    private List<String> preferenceList = new ArrayList<>();
    private String userInfoJson;

    private TextView nextActivityTextview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capability, container, false);

        nextActivityTextview = (TextView)view.findViewById(R.id.recommendActivity);

        TextView fieldTextView = view.findViewById(R.id.roadmap_field_textview);
        String field = LoginData.currentLoginData.getField();
        fieldTextView.setText(field + "에요.");
        Spannable fieldSpan = (Spannable) fieldTextView.getText();
        fieldSpan.setSpan(new ForegroundColorSpan(Color.BLACK),
                0, field.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        dataList = new ArrayList<>();
        labelList = new ArrayList<>();

        barChart = (BarChart) view.findViewById(R.id.chart);

        Button myPageButton = view.findViewById(R.id.user_info_button);
        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myPageIntent = new Intent(getContext(), UserInfoActivity.class);
                myPageIntent.putExtra("userInfoJson", userInfoJson);
                startActivity(myPageIntent);
            }
        });

        loadUserInfo();

        return view;
    }

    private void loadUserInfo() {
        preferenceList = new ArrayList<>();
        int id = LoginData.currentLoginData.getUser().getId();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getMyPageInfo = service.getMyPageInfo(id);
        getMyPageInfo.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    userInfoJson = new Gson().toJson(response.body());
                    setUserInfo(userInfoJson);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setUserInfo(String json) {
        JsonParser parser = new JsonParser();
        JsonObject rootObj = (JsonObject) parser.parse(json);

        JsonArray preferenceArr = (JsonArray) rootObj.get("preference");
        for(int i = 0; i < preferenceArr.size(); i++) {
            JsonObject elementObj = (JsonObject) preferenceArr.get(i);
            if(elementObj.get("preference").getAsInt() >= 0) {
                preferenceList.add(elementObj.get("field").getAsString());
            }
        }

        loadCapability();
    }

    private void loadCapability() {
        //TODO: Loading dialog

        int id = LoginData.currentLoginData.getUser().getId();
        String userField = LoginData.currentLoginData.getField();
        JsonArray fieldArray = new JsonArray();
        for(int i = 0; i < preferenceList.size(); i++) {
            if(!preferenceList.get(i).equals(userField))
                fieldArray.add(preferenceList.get(i));
        }

        JsonObject fieldObject = new JsonObject();
        fieldObject.add("fields", fieldArray);

        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getCapability = service.getCapability(id, fieldObject);
        getCapability.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    String capabilityJson = new Gson().toJson(response.body());
                    setGraphData(capabilityJson);
                    setNextActivity(capabilityJson);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setGraphData(String json) {
        JsonParser parser = new JsonParser();
        JsonObject rootObject = (JsonObject) parser.parse(json);
        JsonArray rootArray = (JsonArray) rootObject.get("capability");
        for(int i = 0; i < rootArray.size(); i++) {
            JsonObject fieldObj = (JsonObject) rootArray.get(i);
            String name = fieldObj.get("name").getAsString();
            int totalCurriculum = fieldObj.get("total_curriculum").getAsInt();
            int completed = fieldObj.get("completed").getAsInt();
            int progress = 100 * completed / totalCurriculum;
            labelList.add(name);
            dataList.add(progress);
        }

        barChartGraph(labelList, dataList);
        barChart.setTouchEnabled(false);
        barChart.getAxisLeft().setAxisMaximum(100);
    }

    private void setNextActivity(String json) {
        String nextActivities = "";

        JsonParser parser = new JsonParser();
        JsonObject rootObject = (JsonObject) parser.parse(json);
        JsonArray recommendedSkills = rootObject.get("recommend_skills").getAsJsonArray();
        for(JsonElement element : recommendedSkills) {
            nextActivities += "- ";
            nextActivities += element.getAsString();
            nextActivities += "\n";
        }

        nextActivityTextview.setText(nextActivities);
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