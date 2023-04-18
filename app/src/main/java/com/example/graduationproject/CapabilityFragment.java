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

/**
 * Fragment of MainActivity to identify user competencies
 * */
public class CapabilityFragment extends Fragment {
    /**
     * Bar chart instance
     * */
    private BarChart barChart;
    /**
     * Bar List of data to be used as the y-axis of the chart
     * */
    private ArrayList<Integer> dataList;
    /**
     * Bar List of label to be used as the x-axis of the chart
     * */
    private ArrayList<String> labelList;

    /**
     * A list of fields that user prefer
     * */
    private List<String> preferenceList = new ArrayList<>();
    /**
     * User information json data received from the server
     * */
    private String userInfoJson;

    /**
     * TextView that recommends the following activities
     * */
    private TextView nextActivityTextview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capability, container, false);

        // Get next activity textview from xml
        nextActivityTextview = (TextView)view.findViewById(R.id.recommendActivity);

        // Get field textview from xml and set the user's current field.
        TextView fieldTextView = view.findViewById(R.id.roadmap_field_textview);
        String field = LoginData.currentLoginData.getField();
        fieldTextView.setText(field + "에요.");
        // Set rich text
        Spannable fieldSpan = (Spannable) fieldTextView.getText();
        fieldSpan.setSpan(new ForegroundColorSpan(Color.BLACK),
                0, field.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        // Initialize list instance
        dataList = new ArrayList<>();
        labelList = new ArrayList<>();

        // Get bar chart from xml
        barChart = (BarChart) view.findViewById(R.id.chart);

        // Set my page button's click listener
        Button myPageButton = view.findViewById(R.id.user_info_button);
        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myPageIntent = new Intent(getContext(), UserInfoActivity.class);
                myPageIntent.putExtra("userInfoJson", userInfoJson);
                startActivity(myPageIntent);
            }
        });

        // Load user information from server
        loadUserInfo();

        return view;
    }

    /**
     * Load user information from server
     * */
    private void loadUserInfo() {
        // Initialize preference list instance
        preferenceList = new ArrayList<>();
        // Get user info by userID
        int id = LoginData.currentLoginData.getUser().getId();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getMyPageInfo = service.getMyPageInfo(id);
        getMyPageInfo.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // Set the user info if data received successfully
                if(response.isSuccessful()) {
                    userInfoJson = new Gson().toJson(response.body());
                    setUserInfo(userInfoJson);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) { }
        });
    }

    /**
     * Parse json of user information
     * */
    private void setUserInfo(String json) {
        // Parse root json object
        JsonParser parser = new JsonParser();
        JsonObject rootObj = (JsonObject) parser.parse(json);

        // Get the preferences
        JsonArray preferenceArr = (JsonArray) rootObj.get("preference");
        for(int i = 0; i < preferenceArr.size(); i++) {
            JsonObject elementObj = (JsonObject) preferenceArr.get(i);
            if(elementObj.get("preference").getAsInt() >= 0) {
                preferenceList.add(elementObj.get("field").getAsString());
            }
        }

        // Load capability(competence) data from server
        loadCapability();
    }

    /**
     * Load capability(competence) data from server
     * */
    private void loadCapability() {
        //TODO: Loading dialog

        // Set the data to send to server
        int id = LoginData.currentLoginData.getUser().getId();
        String userField = LoginData.currentLoginData.getField();
        JsonArray fieldArray = new JsonArray();
        for(int i = 0; i < preferenceList.size(); i++) {
            if(!preferenceList.get(i).equals(userField))
                fieldArray.add(preferenceList.get(i));
        }
        JsonObject fieldObject = new JsonObject();
        fieldObject.add("fields", fieldArray);

        // Request capability data to server
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getCapability = service.getCapability(id, fieldObject);
        getCapability.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // If data received successfully, apply data to graph and next activity textview
                if(response.isSuccessful()) {
                    String capabilityJson = new Gson().toJson(response.body());
                    setGraphData(capabilityJson);
                    setNextActivity(capabilityJson);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) { }
        });
    }

    /**
     * Update the bar chart graph that shows progress in other areas
     * */
    private void setGraphData(String json) {
        // Parse the root json object
        JsonParser parser = new JsonParser();
        JsonObject rootObject = (JsonObject) parser.parse(json);
        JsonArray rootArray = (JsonArray) rootObject.get("capability");
        // Read the name of the field, calculate the percentage of progress,
        // and add it to the graph data list.
        for(int i = 0; i < rootArray.size(); i++) {
            JsonObject fieldObj = (JsonObject) rootArray.get(i);
            String name = fieldObj.get("name").getAsString();
            int totalCurriculum = fieldObj.get("total_curriculum").getAsInt();
            int completed = fieldObj.get("completed").getAsInt();
            int progress = 100 * completed / totalCurriculum;
            labelList.add(name);
            dataList.add(progress);
        }

        // Update the graph
        barChartGraph(labelList, dataList);
        barChart.setTouchEnabled(false);
        barChart.getAxisLeft().setAxisMaximum(100);
    }

    /**
     * Update the recommended activities with the following activities.
     * */
    private void setNextActivity(String json) {
        // Recommended next activity
        String nextActivities = "";

        // Parse root json object
        JsonParser parser = new JsonParser();
        JsonObject rootObject = (JsonObject) parser.parse(json);
        JsonArray recommendedSkills = rootObject.get("recommend_skills").getAsJsonArray();
        // Read the recommended skills to string
        for(JsonElement element : recommendedSkills) {
            nextActivities += "- ";
            nextActivities += element.getAsString();
            nextActivities += "\n";
        }

        // Update the Textview
        nextActivityTextview.setText(nextActivities);
    }

    /**
     * Update the bar chart graph with label and data list
     * */
    private void barChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
        // Initialize entry of bar chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < valList.size(); i++) {
            entries.add(new BarEntry(i, (Integer)valList.get(i)));
        }

        // Initialize the dataset of bar chart
        BarDataSet dataSet = new BarDataSet(entries, "진행도");

        // Initialize the label
        ArrayList<String> labels = new ArrayList<String>();
        for(int i = 0; i < labelList.size(); i++)
            labels.add((String) labelList.get(i));

        // Set the data and visual options of bar chart
        BarData data = new BarData();
        data.addDataSet(dataSet);
        dataSet.setColors(getResources().getIntArray(R.array.bar_chart_color_arr));
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setValueTextSize(10);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        // Set the x-axis of the chart
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        // Set the y-axis of the chart
        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);
        YAxis yAxis = barChart.getAxisRight();
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);

        // Update bar chart
        barChart.setData(data);
        barChart.animateXY(500, 500);
        barChart.invalidate();
    }
}