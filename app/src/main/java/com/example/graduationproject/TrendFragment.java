package com.example.graduationproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendFragment extends Fragment {
    private PieChart pieChart;

    private FieldCategory currentTrendCategory = FieldCategory.WEB_FRONTEND;
    private List<FieldCategory> bookmarkedCategory = null;
    /* Trend Data를 어떤 자료구조로 보관할지 */

    private String[] fields = {"Frontend", "Backend", "Android", "Game Client", "Game Server", "Machine Learning", "Deep Learning", "Data Science", "BigData Engineer", "Blockchain"};
    private View thisView;
    private TextView[] categoryTextviewArray;
    private int currentCategory;
    private String categoryTrendJson = null;

    private LinearLayout categoryContainerTop, categoryContainerBottom;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_trend, container, false);

        pieChart = thisView.findViewById(R.id.pieChart);


        categoryContainerTop = thisView.findViewById(R.id.category_container_top);
        categoryContainerBottom = thisView.findViewById(R.id.category_container_bottom);

        init();

        setCategoryListener();
        loadTrends(currentCategory, false);

        return thisView;
    }

    private void init() {
        categoryTextviewArray = new TextView[fields.length];
        for(int i = 0; i < fields.length; i++){
            categoryTextviewArray[i] = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(150), dpToPx(30));
            params.setMargins(dpToPx(5), 0, dpToPx(5), 0);
            categoryTextviewArray[i].setLayoutParams(params);
            categoryTextviewArray[i].setGravity(Gravity.CENTER);
            categoryTextviewArray[i].setText(fields[i]);
            categoryTextviewArray[i].setTextColor(Color.BLACK);
            if(i == 0)
                categoryTextviewArray[i].setBackground(getResources().getDrawable(R.drawable.current_category_background));

            if(i < fields.length  / 2) {
                categoryContainerTop.addView(categoryTextviewArray[i]);
                if(i < fields.length / 2 - 1)
                    createDivider(categoryContainerTop);
            }
            else {
                categoryContainerBottom.addView(categoryTextviewArray[i]);
                if(i < fields.length - 1)
                    createDivider(categoryContainerBottom);
            }
        }
    }

    private void createDivider(LinearLayout container) {
        View view = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(1), ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.GRAY);
        container.addView(view);
    }

    private void setCategoryListener() {
        for(int i = 0; i < categoryTextviewArray.length; i++) {
            final int c = i;
            categoryTextviewArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(c < 2) {
                        categoryTextviewArray[currentCategory].setBackground(null);
                        currentCategory = c;
                        categoryTextviewArray[currentCategory].setBackgroundResource(R.drawable.current_category_background);
                        loadTrends(c, true);
                    }
                    else {
                        Toast.makeText(getContext(), "해당 분야의 트렌드는 준비 중입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loadTrends(int index, boolean forceLoad) {
        if(categoryTrendJson == null || forceLoad) {
            loadingDialog = new LoadingDialog(getContext());
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
            loadingDialog.show();

            RetrofitService service = RetrofitClient.getRetrofitService();
            String field = fields[index].replaceAll(" ", "");
            Call<Object> trend = service.getTrend(field);
            trend.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        categoryTrendJson = new Gson().toJson(response.body());
                        applyTrend(categoryTrendJson, fields[currentCategory]);
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }
        else {
            categoryTextviewArray[0].setBackground(null);
            categoryTextviewArray[currentCategory].setBackgroundResource(R.drawable.current_category_background);
            applyTrend(categoryTrendJson, fields[currentCategory]);
        }
    }

    private void applyTrend(String trendJson, String key) {
        Gson gson = new Gson();
        Map<String, Map<String, Double>> trendMap = gson.fromJson(trendJson, Map.class);
        Map<String, Double> editedTrendMap = new HashMap<>();
        editedTrendMap = trendMap.get(key);

        ArrayList<PieEntry> trend = new ArrayList<>();
        int othersCount = 0, otherCriteria = 5;
        for(Map.Entry<String, Double> entryDetail : editedTrendMap.entrySet()) {
            int val = (int)Math.round(entryDetail.getValue());
            if(val <= otherCriteria)
                othersCount += val;
            else
                trend.add(new PieEntry(val, entryDetail.getKey()));
        }
        if(othersCount > 0)
            trend.add(new PieEntry(othersCount, "기타"));

        PieDataSet pieDataSet = new PieDataSet(trend, "Trend");
        pieDataSet.setValueFormatter(new PercentFormatter(pieChart));
        drawPieChart(pieDataSet);
        /*for(Map.Entry<String, Map<String, Integer>> entry : trendMap.entrySet()) {
            String entryJson = gson.toJson(entry.getValue());
            Map<String, Integer> entryMap = gson.fromJson(entryJson, Map.class);

        }*/
    }

    private void drawPieChart(PieDataSet pieDataSet) {
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Trend");
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setUsePercentValues(true);
        pieChart.animate();
        pieChart.invalidate();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}