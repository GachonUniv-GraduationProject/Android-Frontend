package com.example.graduationproject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragments showing technical trend information
 * */
public class TrendFragment extends Fragment {
    /**
     * Pie chart showing share by technology
     * */
    private PieChart pieChart;

    /**
     * Tech Sector Trend Map Edited to Form
     * */
    private Map<String, Double> editedTechTrendMap;
    /**
     * Framework Sector Trend Map Edited to Form
     * */
    private Map<String, Double> editedFrameworkTrendMap;
    /**
     * Other Sector Trend Map Edited to Form
     * */
    private Map<String, Double> editedEtcTrendMap;

    /**
     * Array of main keywords by field
     * */
    private String[] fields = {"Frontend", "Backend", "Android", "Game Client", "Game Server", "Machine Learning", "Deep Learning", "Data Science", "BigData Engineer", "Blockchain"};
    /**
     * Fragments' parent view
     * */
    private View thisView;
    /**
     * Text view that represents fields
     * */
    private TextView[] categoryTextviewArray;
    /**
     * The field index currently being shown
     * */
    private int currentCategory;
    /**
     * Trends in the form of json received from the server
     * */
    private String categoryTrendJson = null;

    /**
     * Containers containing top categories
     * */
    private LinearLayout categoryContainerTop;
    /**
     * Containers containing bottom categories
     * */
    private LinearLayout categoryContainerBottom;

    /**
     * Loading dialog during login process
     * */
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_trend, container, false);

        // Load pie chart from xml
        pieChart = thisView.findViewById(R.id.pieChart);

        // Load containers from xml
        categoryContainerTop = thisView.findViewById(R.id.category_container_top);
        categoryContainerBottom = thisView.findViewById(R.id.category_container_bottom);

        // Initialize categories
        init();

        // Set category listener
        setCategoryListener();
        setItemListener();
        loadTrends(currentCategory, false);

        return thisView;
    }

    /**
     * Initialize category textview
     * */
    private void init() {
        categoryTextviewArray = new TextView[fields.length];
        for(int i = 0; i < fields.length; i++){
            // Create textview and apply custom layout parameter
            categoryTextviewArray[i] = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(150), dpToPx(30));
            params.setMargins(dpToPx(5), 0, dpToPx(5), 0);
            categoryTextviewArray[i].setLayoutParams(params);
            // Set gravity to center
            categoryTextviewArray[i].setGravity(Gravity.CENTER);
            // Set contents and color
            categoryTextviewArray[i].setText(fields[i]);
            categoryTextviewArray[i].setTextColor(Color.BLACK);
            // Set background image
            if(i == 0)
                categoryTextviewArray[i].setBackground(getResources().getDrawable(R.drawable.current_category_background));

            // Add a text view to the first(top) or second(bottom) line container
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

    /**
     * Create a divider that separates categories
     * */
    private void createDivider(LinearLayout container) {
        View view = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(1), ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.GRAY);
        container.addView(view);
    }

    /**
     * Set category click listener
     * */
    private void setCategoryListener() {
        for(int i = 0; i < categoryTextviewArray.length; i++) {
            final int c = i;
            categoryTextviewArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: TEMPORARY LIMIT FOR DATA COLLECTION
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

    /**
     * Set detailed item click listener
     * */
    private void setItemListener() {
        // Load buttons from xml
        TextView techButton = thisView.findViewById(R.id.tech_item_textview);
        TextView frameworkButton = thisView.findViewById(R.id.framework_item_textview);
        TextView etcButton = thisView.findViewById(R.id.etc_item_textview);

        // Set technical trend
        techButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyTrend(0);
                techButton.setBackgroundResource(R.drawable.current_category_background);
                frameworkButton.setBackground(null);
                etcButton.setBackground(null);
            }
        });

        // Set framework trend
        frameworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyTrend(1);
                frameworkButton.setBackgroundResource(R.drawable.current_category_background);
                techButton.setBackground(null);
                etcButton.setBackground(null);
            }
        });

        // Set other trend
        etcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyTrend(2);
                etcButton.setBackgroundResource(R.drawable.current_category_background);
                frameworkButton.setBackground(null);
                techButton.setBackground(null);
            }
        });
    }

    /**
     * Load trend data from server
     * */
    private void loadTrends(int index, boolean forceLoad) {
        // Make sure no data is loaded first
        if(categoryTrendJson == null || forceLoad) {
            // Enable the loading dialog
            loadingDialog = new LoadingDialog(getContext());
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
            loadingDialog.show();

            // Request trend data for user chosen field
            RetrofitService service = RetrofitClient.getRetrofitService();
            String field = fields[index].replaceAll(" ", "");
            Call<Object> trend = service.getTrend(field);
            trend.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        // Convert trend json to Map instance
                        categoryTrendJson = new Gson().toJson(response.body());
                        convertTrendJson(categoryTrendJson);
                        // Apply to pie chart
                        applyTrend(0);
                        // Disable loading dialog
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) { }
            });
        }
        else {
            // Expose existing data
            categoryTextviewArray[0].setBackground(null);
            categoryTextviewArray[currentCategory].setBackgroundResource(R.drawable.current_category_background);
            convertTrendJson(categoryTrendJson);
            applyTrend(0);
        }
    }

    /**
     * Extract from json according to the form and organize in map form
     * */
    private Map<String, Double> getEditedTrendMap(JsonObject rootObj) {
        Gson gson = new Gson();
        Map<String, Map<String, Double>> trendMap = gson.fromJson(rootObj, Map.class);
        Map<String, Double> editedTrendMap = new HashMap<>();
        String[] techNames = trendMap.keySet().toArray(new String[trendMap.size()]);
        for(int i = 0; i < trendMap.size(); i++) {
            double value = trendMap.get(techNames[i]).get("count");
            if(value > 0)
                editedTrendMap.put(techNames[i], value);
        }
        return editedTrendMap;
    }

    /**
     * Parse the json data divided into tech, framework, and others
     * */
    private void convertTrendJson(String trendJson) {
        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(trendJson).getAsJsonObject();

        JsonObject techObj = rootObj.get("기술").getAsJsonObject();
        JsonObject frameworkObj = rootObj.get("프레임워크").getAsJsonObject();
        JsonObject etcObj = rootObj.get("기타").getAsJsonObject();

        editedTechTrendMap = getEditedTrendMap(techObj);
        editedFrameworkTrendMap = getEditedTrendMap(frameworkObj);
        editedEtcTrendMap = getEditedTrendMap(etcObj);
    }

    /**
     * Get a trend map by index.
     * */
    private Map<String, Double> findEditedMapByIndex(int index) {
        switch (index) {
            case 0:
                return editedTechTrendMap;
            case 1:
                return editedFrameworkTrendMap;
            case 2:
                return editedEtcTrendMap;
        }
        return null;
    }

    /**
     * Apply the desired kind of data to the pie chart
     * */
    private void applyTrend(int item) {

        ArrayList<PieEntry> trend = new ArrayList<>();
        int othersCount = 0, otherCriteria = 5;
        for(Map.Entry<String, Double> entryDetail : findEditedMapByIndex(item).entrySet()) {
            // Get the frequency of appearance of the keyword and add it as a pie chart entry
            int val = (int)Math.round(entryDetail.getValue());
            if(val <= otherCriteria)
                othersCount += val;
            else {
                /* // Temporary code for modify Java to javascript
                if(entryDetail.getKey().equals("Java"))
                    trend.add(new PieEntry(val, "JavaScript"));
                else*/
                trend.add(new PieEntry(val, entryDetail.getKey()));
            }
        }
        if(othersCount > 0)
            trend.add(new PieEntry(othersCount, "기타"));

        // Set the pie chart data set
        PieDataSet pieDataSet = new PieDataSet(trend, "Trend");
        pieDataSet.setValueFormatter(new PercentFormatter(pieChart));
        // Set the font
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "d2coding.ttf");
        pieDataSet.setValueTypeface(tf);
        // Draw pie chart
        drawPieChart(pieDataSet);
    }

    /**
     * Draw a pie chart that fits the given data.
     * */
    private void drawPieChart(PieDataSet pieDataSet) {
        // Set the color and text appearance
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        // Apply the data to pie chart and show it
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Trend");
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setUsePercentValues(true);
        pieChart.animate();
        pieChart.invalidate();
    }

    /**
     * Convert dp to pixel size
     * */
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}