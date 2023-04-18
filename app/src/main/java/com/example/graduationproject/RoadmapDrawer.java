package com.example.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.core.content.ContextCompat;

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
 * View class to draw roadmap UI
 * */
public class RoadmapDrawer extends View {
    /**
     * Main canvas of this drawer
     * */
    private Canvas mainCanvas;
    /**
     * Frame Container of roadmap
     * */
    private FrameLayout roadmapFrameContainer;
    /**
     * Context of this view
     * */
    private static Context context;

    /**
     * Path for drawing line of roadmap
     * */
    private Path linePath;
    /**
     * Paint for drawing stroke
     * */
    private Paint outerPaint;
    /**
     * Paint for writing name of skill
     * */
    private Paint namePaint;

    /**
     * Color array of roadmap by depth
     * */
    private int[] roadmapColor;

    /**
     * Field of this roadmap
     * */
    private String field;
    /**
     * List of Roadmap step
     * */
    private List<RoadMapStep> roadMap;
    /**
     * List of circular image for each step
     * */
    private List<ImageView> imageViewList;
    /**
     * List of Textview that shows the name of step
     * */
    private List<TextView> textViewList;
    /**
     * Step size category enumeration
     * */
    public enum Category {
        LARGE(0), MIDDLE(1), SMALL(2);

        /**
         * Value of this category
         * */
        private final int value;
        /**
         * Set value in integer type
         * */
        private Category(int value) {
            this.value = value;
        }

        /**
         * Get value in integer type
         * */
        public int getValue() {
            return value;
        }
    }
    /**
     * Interval size for each step
     * */
    private final int INTERVAL = 50;

    /**
     * Reference link dialog
     * */
    private LinkDialog linkDialog;
    /**
     * Loading dialog to indicate that loading in in progress
     * */
    private LoadingDialog loadingDialog;

    public RoadmapDrawer(Context context, FrameLayout roadmapContainer) {
        super(context);

        roadMap = new ArrayList<>();

        loadRoadmapData(roadmapContainer);
    }

    /**
     * Get roadmap data from server
     * */
    private void loadRoadmapData(FrameLayout roadmapContainer) {
        // Show loading dialog
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        // Set the user and field data and request roadmap data
        int id = LoginData.currentLoginData.getUser().getId();
        field = LoginData.currentLoginData.getField();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getRoadmap = service.getRoadmap(id, field);
        getRoadmap.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    // Convert received json data to road map class data, and initialize UI
                    String roadmapJson = new Gson().toJson(response.body());
                    initRoadmapData(roadmapJson);
                    initView(roadmapContainer);
                    initCanvas();
                    loadingDialog.dismiss();
                }
                else {
                    //TODO : 로딩 실패 시 오류메시지 출력 후 다른 fragment로 이동
                    Log.d("Apply Complete Roadmap", "Failed to apply " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                //TODO : 로딩 실패 시 오류메시지 출력 후 다른 fragment로 이동
                Log.d("Apply Complete Roadmap", "Failed to apply " + t.getStackTrace());
            }
        });
    }
    /**
     * Find the roadmap step object by the name of step
     * */
    private RoadMapStep getRoadmapStepByName(String name) {
        for(RoadMapStep step : roadMap) {
            if(step.getName().equals(name)) {
                return step;
            }
        }

        return null;
    }

    /**
     * Initialize the roadmap data with json
     * */
    private void initRoadmapData(String json) {
        // Parse json root object
        JsonParser parser = new JsonParser();
        JsonObject rootObject = (JsonObject) parser.parse(json);
        JsonArray rootArray = (JsonArray) rootObject.get("skill");
        // For each element of the Json array, obtain the name, level, base skill, lock, and completion.
        for(int i = 0; i < rootArray.size(); i++) {
            JsonObject stepObj = (JsonObject) rootArray.get(i);
            String name = stepObj.get("name").getAsString();
            int level = stepObj.get("level").getAsInt();
            String base = stepObj.get("base").getAsString();
            RoadMapStep baseStep = getRoadmapStepByName(base);
            boolean locked = stepObj.get("locked").getAsBoolean();
            boolean completed = stepObj.get("completed").getAsBoolean();
            if(level > 2)
                level = 2;

            // Add roadmap data to list of roadmap
            roadMap.add(new RoadMapStep(name, Category.values()[level], baseStep, locked, completed, this));
        }
    }

    /**
     * Initialize the view component constituting this drawer.
     * */
    private void initView(FrameLayout roadmapFrameContainer) {
        // Set context and container
        this.context = getContext();
        this.roadmapFrameContainer = roadmapFrameContainer;
        // Initialize list
        imageViewList = new ArrayList<>();
        textViewList = new ArrayList<>();

        // Initialize node images and names as much as the size of the roadmap.
        for(int i = 0; i < roadMap.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageViewList.add(imageView);
            roadmapFrameContainer.addView(imageView);

            // Set the textview of step
            TextView textView = new TextView(context);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(Dimension.SP, 15);
            textViewList.add(textView);
            roadmapFrameContainer.addView(textView);
        }
    }

    /**
     * Initialize canvas setting
     * */
    private void initCanvas() {
        // Initialize stroke paint
        outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setColor(Color.BLACK);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(dpToPx(4));

        // Initialize naming paint
        namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        namePaint.setTextSize(dpToPx(15));

        // Initialize path of roadmap line
        linePath = new Path();
        // Get color of roadmap node
        roadmapColor = getResources().getIntArray(R.array.roadmap_step_color_arr);

        setLayoutHeight();
    }

    /**
     * Draw on Canvas
     * */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mainCanvas = canvas;

        for(int i = 0; i < roadMap.size(); i++)
            addPoint(i);
    }

    /**
     * Set layout height
     * */
    private void setLayoutHeight() {
        int bottomY = getCirclePosY(roadMap.size() - 1) + dpToPx(30);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bottomY);
        setLayoutParams(params);
    }

    /**
     * Add a node (draw a cicle + line)
     * */
    private void addPoint(int index) {
        int cat = roadMap.get(index).getCategory().ordinal();

        drawLine(index);
        if(roadMap.get(index).isLocked())
            drawCircle(index, Color.WHITE);
        else
            drawCircle(index, roadmapColor[cat]);
        drawName(index);
    }

    /**
     * Draw name of the step
     * */
    private void drawName(int index) {
        // Get the name and category of step
        RoadMapStep step = roadMap.get(index);
        String name = step.getName();
        Category category = step.getCategory();

        // Set the circle specification
        int circleSize = getCircleSize(category);
        int x = getCirclePosX(category) + circleSize + dpToPx(10);
        int y = getCirclePosY(index) + dpToPx(15) / 3;
        int lockBackgroundWidth = 140;
        int lockWidth = 20;

        // If the step is locked, draw UI to indicate lock status
        if(step.isLocked()) {
            String lockedMessage = "다음 활동 잠금";

            Drawable lockBackgroundDrawable = getResources().getDrawable(R.drawable.round_button);
            lockBackgroundDrawable.setBounds(x, y - circleSize - dpToPx(10), x + dpToPx(lockBackgroundWidth), y + circleSize);
            lockBackgroundDrawable.draw(mainCanvas);
            Drawable lockImage=  getResources().getDrawable(R.drawable.lock);
            lockImage.setBounds(x + dpToPx(10), y - dpToPx(15), x + dpToPx(10 + lockWidth), y + dpToPx(5));
            lockImage.draw(mainCanvas);
            namePaint.setColor(Color.WHITE);
            mainCanvas.drawText(lockedMessage, x + dpToPx(lockWidth + 15), y, namePaint);
        }
        // If unlocked, write the name of step
        else {
            setNodeName(index, name, category);

        }
    }

    /**
     * Write the name of step on textview
     * */
    private void setNodeName(int index, String name, Category category) {
        // Set the textview content
        TextView textView = textViewList.get(index);
        textView.setText(name);

        // Set the position of textview
        int circleSize = getCircleSize(category) + dpToPx(2);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setX(getCirclePosX(category) + circleSize + dpToPx(10));
        textView.setY(getCirclePosY(index) - dpToPx(10));

        // Set the click listener for textview
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and show the reference link dialog
                linkDialog = new LinkDialog(context);
                linkDialog.setSkillName(textView.getText().toString());
                linkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                linkDialog.setContentView(R.layout.dialog_link_to_reference);

                showLinkDialog();
            }
        });
    }

    /**
     * Show the reference link dialog
     * */
    private void showLinkDialog() {
        linkDialog.loadUrls();
        linkDialog.show();
    }

    /**
     * Draw line of roadmap
     * */
    private void drawLine(int index) {
        // Get the current step
        RoadMapStep step = roadMap.get(index);
        // Get the prior step
        RoadMapStep priorStep = null;
        if (index > 0)
            priorStep = roadMap.get(index - 1);
        // Get the step of base skill
        RoadMapStep baseStep = step.getBaseStep();

        // Set the position of the step
        step.setPos(getCirclePosX(step.getCategory()), getCirclePosY(index));

        // Draw a line for the indented step.
        if (baseStep != null) {
            int circleSize = getCircleSize(step.getCategory());
            int baseY = priorStep.getPosY();
            if(priorStep.getCategory() != Category.SMALL)
                baseY += circleSize + dpToPx(2);
            drawLine(baseStep.getPosX(), baseY, step.getPosX(), step.getPosY());
        }
    }

    /**
     * Draw line of roadmap tree
     * */
    private void drawLine(int baseX, int baseY, int stepX, int stepY) {
        linePath.reset();
        linePath.moveTo(baseX, baseY);
        linePath.lineTo(baseX, stepY);
        linePath.lineTo(stepX, stepY);
        mainCanvas.drawPath(linePath, outerPaint);
    }

    /**
     * Draw node circle
     * */
    private void drawCircle(int index, int innerColor) {
        Category category = roadMap.get(index).getCategory();

        setNodeCircle(index, category);
    }

    /**
     * Get position X of circle by size category
     * */
    private int getCirclePosX(Category category) {
        if(category == Category.LARGE) {
            return dpToPx(50);
        }
        else if(category == Category.MIDDLE) {
            return dpToPx(50);
        }
        else {
            return dpToPx(75);
        }
    }

    /**
     * Get circle size in pixel by size category
     * */
    private int getCircleSize(Category category) {
        if(category == Category.LARGE) {
            return dpToPx(15);
        }
        else if(category == Category.MIDDLE) {
            return dpToPx(12);
        }
        else {
            return dpToPx(8);
        }
    }

    /**
     * Get circle color by size category
     * */
    private int getColorBySize(Category category) {
        if(category == Category.LARGE) {
            return getResources().getColor(R.color.roadmap_color_large);
        }
        else if(category == Category.MIDDLE) {
            return getResources().getColor(R.color.roadmap_color_middle);
        }
        else {
            return getResources().getColor(R.color.roadmap_color_small);
        }
    }

    /**
     * Request the selected step to complete to the server
     * */
    private void applyCompleteOnServer(String field, String skill, RoadMapStep step, boolean isCompleted, int index) {
        // Check if the user is treating it as complete
        if(isCompleted) {
            // Show loading dialog
            loadingDialog = new LoadingDialog(getContext());
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
            loadingDialog.show();

            // Set the user id and skill that will be checked to complete
            int id = LoginData.currentLoginData.getUser().getId();
            JsonObject skillObj = new JsonObject();
            skillObj.addProperty("name", skill);
            RetrofitService service = RetrofitClient.getRetrofitService();
            Call<Object> getRoadmap = service.putRoadmap(id, field, skillObj);
            getRoadmap.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        // Load the circular drawable picture
                        GradientDrawable circleDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.circle);
                        if (isCompleted) {
                            // If it is complete, change it to the appropriate color and add the completed substep to the base skill
                            circleDrawable.setColor(getResources().getColor(R.color.roadmap_color_complete));
                            imageViewList.get(index).setImageDrawable(circleDrawable);
                            step.setCompleted(true);
                            step.getBaseStep().checkSubSteps();
                        } else {
                            // Notify that cancellation of completion is not possible
                            Toast.makeText(getContext(), "취소는 불가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    } else {
                        //TODO : 로딩 실패 시 오류메시지 출력 후 다른 fragment로 이동
                        Log.d("Apply Complete Roadmap", "Failed to apply " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    //TODO : 로딩 실패 시 오류메시지 출력 후 다른 fragment로 이동
                    Log.d("Apply Complete Roadmap", "Failed to apply " + t.getStackTrace());
                }
            });
        }
        else {
            Toast.makeText(getContext(), "취소는 불가능합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Location and Event Listener Settings for Node Circle
     * */
    private void setNodeCircle(int index, Category category) {
        // Set the position of node circle
        ImageView imageView = imageViewList.get(index);
        int circleSize = getCircleSize(category) + dpToPx(2);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(circleSize * 2, circleSize * 2);
        imageView.setLayoutParams(params);
        imageView.setX(getCirclePosX(category) - circleSize);
        imageView.setY(getCirclePosY(index) - circleSize);

        // Set the color of circle
        GradientDrawable circleDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.circle);
        RoadMapStep step = roadMap.get(index);
        if(!step.isLocked()) {
            circleDrawable.setColor(getColorBySize(category));
            if(step.isCompleted() && step.getCategory() == Category.SMALL)
                circleDrawable.setColor(getResources().getColor(R.color.roadmap_color_complete));
        }
        else
            circleDrawable.setColor(Color.WHITE);
        imageView.setImageDrawable(circleDrawable);

        // Set the click event listener if it is small node
        if (category == Category.SMALL) {
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    RoadMapStep step = roadMap.get(index);
                    applyCompleteOnServer(field, step.getName(), step, !step.isCompleted(), index);
                }
            });
        }
    }

    /**
     * Activate next step
     * */
    public void openNextLevel(String name) {
        // Get the roadmap step instance
        RoadMapStep step = null;
        for(int i = 0; i < roadMap.size(); i++) {
            if(roadMap.get(i).getName().equals(name))
                step = roadMap.get(i);
        }

        // Null check
        if(step == null)
            return;

        // Set the step to be unlocked with its children
        step.setLocked(false);
        for(RoadMapStep childStep : step.getChildrenSteps()) {
            childStep.setLocked(false);
        }

        // Redraw the roadmap
        invalidate();
    }

    /**
     * Convert dp to pixel size
     * */
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * Get the position Y of circle
     * */
    private int getCirclePosY(int index) {
        return (index + 1) * dpToPx(INTERVAL);
    }
}
