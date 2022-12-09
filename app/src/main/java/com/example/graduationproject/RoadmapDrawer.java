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

public class RoadmapDrawer extends View {
    private Canvas mainCanvas;
    private FrameLayout roadmapFrameContainer;
    private static Context context;

    private Path linePath;
    private Paint outerPaint;
    private Paint namePaint;
    private Paint innerPnt;

    private int[] roadmapColor;

    private String field;
    private List<RoadMapStep> roadMap;
    private List<ImageView> imageViewList;
    private List<TextView> textViewList;
    public enum Category {
        LARGE(0), MIDDLE(1), SMALL(2);

        private final int value;
        private Category(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private final int INTERVAL = 50;

    private LinkDialog linkDialog;
    private LoadingDialog loadingDialog;

    public RoadmapDrawer(Context context, FrameLayout roadmapContainer) {
        super(context);

        roadMap = new ArrayList<>();

        loadRoadmapData(roadmapContainer);
    }

    private void loadRoadmapData(FrameLayout roadmapContainer) {
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        int id = LoginData.currentLoginData.getUser().getId();
        field = LoginData.currentLoginData.getField();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getRoadmap = service.getRoadmap(id, field);
        getRoadmap.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
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
    private RoadMapStep getRoadmapStepByName(String name) {
        for(RoadMapStep step : roadMap) {
            if(step.getName().equals(name)) {
                return step;
            }
        }

        return null;
    }

    private void initRoadmapData(String json) {
        JsonParser parser = new JsonParser();
        JsonObject rootObject = (JsonObject) parser.parse(json);
        JsonArray rootArray = (JsonArray) rootObject.get("skill");
        for(int i = 0; i < rootArray.size(); i++) {
            JsonObject stepObj = (JsonObject) rootArray.get(i);
            String name = stepObj.get("name").getAsString();
            int level = stepObj.get("level").getAsInt();
            String base = stepObj.get("base").getAsString();
            RoadMapStep baseStep = getRoadmapStepByName(base);
            boolean locked = stepObj.get("locked").getAsBoolean();
            boolean completed = stepObj.get("completed").getAsBoolean();
            if(level > 2) // TODO: 임시방편, 추후에 다단계 로드맵을 어떻게 구성할지 구조를 고민해봐야 함
                level = 2;

            roadMap.add(new RoadMapStep(name, Category.values()[level], baseStep, locked, completed, this));
        }
    }

    private void initView(FrameLayout roadmapFrameContainer) {
        this.context = getContext();
        this.roadmapFrameContainer = roadmapFrameContainer;
        imageViewList = new ArrayList<>();
        textViewList = new ArrayList<>();
        for(int i = 0; i < roadMap.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageViewList.add(imageView);
            roadmapFrameContainer.addView(imageView);

            TextView textView = new TextView(context);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(Dimension.SP, 15);
            textViewList.add(textView);
            roadmapFrameContainer.addView(textView);
        }
    }

    private void initCanvas() {
        outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setColor(Color.BLACK);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(dpToPx(4));

        namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        namePaint.setTextSize(dpToPx(15));

        linePath = new Path();
        innerPnt = new Paint();
        roadmapColor = getResources().getIntArray(R.array.roadmap_step_color_arr);

        setLayoutHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mainCanvas = canvas;

        for(int i = 0; i < roadMap.size(); i++)
            addPoint(i);
    }

    private void setLayoutHeight() {
        int bottomY = getCirclePosY(roadMap.size() - 1) + dpToPx(30);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bottomY);
        setLayoutParams(params);
    }

    // 지점 추가 (원+선 그리기)
    private void addPoint(int index) {
        int cat = roadMap.get(index).getCategory().ordinal();

        drawLine(index);
        if(roadMap.get(index).isLocked())
            drawCircle(index, Color.WHITE);
        else
            drawCircle(index, roadmapColor[cat]);
        drawName(index);
    }

    private void drawName(int index) {
        RoadMapStep step = roadMap.get(index);
        String name = step.getName();
        Category category = step.getCategory();

        int circleSize = getCircleSize(category);
        int x = getCirclePosX(category) + circleSize + dpToPx(10);
        int y = getCirclePosY(index) + dpToPx(15) / 3;
        int lockBackgroundWidth = 140;
        int lockWidth = 20;

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
        else {
            //namePaint.setColor(Color.BLACK);
            //mainCanvas.drawText(name, x, y, namePaint);
            setNodeName(index, name, category);

        }
    }
    private void setNodeName(int index, String name, Category category) {
        TextView textView = textViewList.get(index);

        textView.setText(name);

        int circleSize = getCircleSize(category) + dpToPx(2);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setX(getCirclePosX(category) + circleSize + dpToPx(10));
        textView.setY(getCirclePosY(index) - dpToPx(10));

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                linkDialog = new LinkDialog(context);
                linkDialog.setSkillName(textView.getText().toString());
                linkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                linkDialog.setContentView(R.layout.dialog_link_to_reference);

                showLinkDialog();
            }
        });
    }

    private void showLinkDialog() {
        linkDialog.loadUrls();
        linkDialog.show();
    }

    private void drawLine(int index) {
        RoadMapStep step = roadMap.get(index);
        RoadMapStep priorStep = null;
        if (index > 0)
            priorStep = roadMap.get(index - 1);
        RoadMapStep baseStep = step.getBaseStep();

        step.setPos(getCirclePosX(step.getCategory()), getCirclePosY(index));

        if (baseStep != null) {
            int circleSize = getCircleSize(step.getCategory());
            int baseY = priorStep.getPosY();
            if(priorStep.getCategory() != Category.SMALL)
                baseY += circleSize + dpToPx(2);
            drawLine(baseStep.getPosX(), baseY, step.getPosX(), step.getPosY());
        }
    }

    private void drawLine(int baseX, int baseY, int stepX, int stepY) {
        linePath.reset();
        linePath.moveTo(baseX, baseY);
        linePath.lineTo(baseX, stepY);
        linePath.lineTo(stepX, stepY);
        mainCanvas.drawPath(linePath, outerPaint);
    }


    private void drawCircle(int index, int innerColor) {
        Category category = roadMap.get(index).getCategory();

        //innerPnt.setColor(innerColor);
        //mainCanvas.drawCircle(getCirclePosX(category), getCirclePosY(index), getCircleSize(category), innerPnt);
        //mainCanvas.drawCircle(getCirclePosX(category), getCirclePosY(index), getCircleSize(category), outerPaint);
        setNodeCircle(index, category);
    }

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

    private void applyCompleteOnServer(String field, String skill, RoadMapStep step, boolean isCompleted, int index) {
        if(isCompleted) {
            loadingDialog = new LoadingDialog(getContext());
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
            loadingDialog.show();

            int id = LoginData.currentLoginData.getUser().getId();
            JsonObject skillObj = new JsonObject();
            skillObj.addProperty("name", skill);
            RetrofitService service = RetrofitClient.getRetrofitService();
            Call<Object> getRoadmap = service.putRoadmap(id, field, skillObj);
            getRoadmap.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        GradientDrawable circleDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.circle);
                        if (isCompleted) {
                            circleDrawable.setColor(getResources().getColor(R.color.roadmap_color_complete));
                            imageViewList.get(index).setImageDrawable(circleDrawable);
                            step.setCompleted(true);
                            step.getBaseStep().checkSubSteps();
                        } else {
                            //circleDrawable.setColor(getColorBySize(category));
                            //step.setCompleted(false);
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

    private void setNodeCircle(int index, Category category) {
        ImageView imageView = imageViewList.get(index);
        int circleSize = getCircleSize(category) + dpToPx(2);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(circleSize * 2, circleSize * 2);
        imageView.setLayoutParams(params);
        imageView.setX(getCirclePosX(category) - circleSize);
        imageView.setY(getCirclePosY(index) - circleSize);

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

    public void openNextLevel(String name) {
        RoadMapStep step = null;
        for(int i = 0; i < roadMap.size(); i++) {
            if(roadMap.get(i).getName().equals(name))
                step = roadMap.get(i);
        }

        if(step == null)
            return;

        step.setLocked(false);
        for(RoadMapStep childStep : step.getChildrenSteps()) {
            childStep.setLocked(false);
        }

        invalidate();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int getCirclePosY(int index) {
        return (index + 1) * dpToPx(INTERVAL);
    }
}
