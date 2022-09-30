package com.example.graduationproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class RoadmapDrawer extends View {
    private Canvas mainCanvas;

    private Path linePath;
    private Paint outerPaint;
    private Paint namePaint;
    private Paint innerPnt;

    private int[] roadmapColor;

    private List<RoadMapStep> roadMap;
    public enum Category {SMALL, MIDDLE, LARGE};
    private final int INTERVAL = 50;

    public RoadmapDrawer(Context context) {
        super(context);

        init();
    }

    private void init() {
        outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setColor(Color.BLACK);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(dpToPx(4));

        namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        namePaint.setTextSize(dpToPx(15));

        linePath = new Path();
        innerPnt = new Paint();
        roadmapColor = getResources().getIntArray(R.array.roadmap_step_color_arr);

        roadMap = new ArrayList<>();
        roadMap.add(new RoadMapStep("Test1", Category.LARGE, null));
        roadMap.add(new RoadMapStep("Test1-1", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-1-1", Category.SMALL, roadMap.get(1)));
        roadMap.add(new RoadMapStep("Test1-2", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-3", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-4", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-5", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-6", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-7", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-8", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-9", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-10", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-11", Category.MIDDLE, roadMap.get(0)));
        roadMap.add(new RoadMapStep("Test1-12", Category.MIDDLE, roadMap.get(0)));

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
        drawCircle(index, roadmapColor[cat]);
        drawName(index);
    }

    private void drawName(int index) {
        RoadMapStep step = roadMap.get(index);
        String name = step.getName();
        Category category = step.getCategory();

        mainCanvas.drawText(name, getCirclePosX(category) + getCircleSize(category) + dpToPx(10),
                getCirclePosY(index) + dpToPx(15) / 3, namePaint);
    }

    private void drawLine(int index) {
        RoadMapStep step = roadMap.get(index);
        RoadMapStep baseStep = step.getBaseStep();

        step.setPos(getCirclePosX(step.getCategory()), getCirclePosY(index));

        if (baseStep != null) {
            drawLine(baseStep.getPosX(), baseStep.getPosY(), step.getPosX(), step.getPosY(), getCircleSize(step.getCategory()));
        }
    }

    private void drawLine(int baseX, int baseY, int stepX, int stepY, int circleSize) {
        linePath.reset();
        linePath.moveTo(baseX, baseY + circleSize + dpToPx(2));
        linePath.lineTo(baseX, stepY);
        linePath.lineTo(stepX, stepY);
        mainCanvas.drawPath(linePath, outerPaint);
    }


    private void drawCircle(int index, int innerColor) {
        Category category = roadMap.get(index).getCategory();

        innerPnt.setColor(innerColor);
        mainCanvas.drawCircle(getCirclePosX(category), getCirclePosY(index), getCircleSize(category), innerPnt);
        mainCanvas.drawCircle(getCirclePosX(category), getCirclePosY(index), getCircleSize(category), outerPaint);
    }

    private int getCirclePosX(Category category) {
        if(category == Category.LARGE) {
            return dpToPx(50);
        }
        else if(category == Category.MIDDLE) {
            return dpToPx(100);
        }
        else {
            return dpToPx(150);
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

    private int getCirclePosY(int index) {
        return (index + 1) * dpToPx(INTERVAL);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
