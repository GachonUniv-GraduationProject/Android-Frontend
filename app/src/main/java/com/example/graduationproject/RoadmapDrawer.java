package com.example.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class RoadmapDrawer extends View {
    private Canvas mainCanvas;
    private FrameLayout roadmapFrameContainer;
    private static Context context;

    private Path linePath;
    private Paint outerPaint;
    private Paint namePaint;
    private Paint innerPnt;

    private int[] roadmapColor;

    private List<RoadMapStep> roadMap;
    private List<ImageView> imageViewList;
    private List<TextView> textViewList;
    public enum Category {SMALL, MIDDLE, LARGE};
    private final int INTERVAL = 50;

    private LinkDialog linkDialog;

    public RoadmapDrawer(Context context, FrameLayout roadmapContainer) {
        super(context);

        roadMap = new ArrayList<>();
        roadMap.add(new RoadMapStep("Test1", Category.LARGE, null, false));
        roadMap.add(new RoadMapStep("Test1-1", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-1-1", Category.SMALL, roadMap.get(1), false));
        roadMap.add(new RoadMapStep("Test1-2", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-3", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-4", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-5", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-6", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-7", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-8", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-9", Category.MIDDLE, roadMap.get(0), false));
        roadMap.add(new RoadMapStep("Test1-10", Category.MIDDLE, roadMap.get(0), true));
        roadMap.add(new RoadMapStep("Test1-11", Category.MIDDLE, roadMap.get(0), true));
        roadMap.add(new RoadMapStep("Test1-12", Category.MIDDLE, roadMap.get(0), true));

        initView(context, roadmapContainer);
        initCanvas();
    }

    private void initView(Context context, FrameLayout roadmapFrameContainer) {
        this.context = context;
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
                linkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                linkDialog.setContentView(R.layout.dialog_link_to_reference);

                showLinkDialog();
            }
        });
    }

    private void showLinkDialog() {
        String[] titles = {"교육자료 1", "교육자료 2"};
        String[] urls = {"https://www.youtube.com/", "https://www.youtube.com/"};
        linkDialog.setLinkSets(titles, urls);

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

    private void setNodeCircle(int index, Category category) {
        ImageView imageView = imageViewList.get(index);
        int circleSize = getCircleSize(category) + dpToPx(2);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(circleSize * 2, circleSize * 2);
        imageView.setLayoutParams(params);
        imageView.setX(getCirclePosX(category) - circleSize);
        imageView.setY(getCirclePosY(index) - circleSize);

        GradientDrawable circleDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.circle);
        if(!roadMap.get(index).isLocked())
            circleDrawable.setColor(getColorBySize(category));
        else
            circleDrawable.setColor(Color.WHITE);
        imageView.setImageDrawable(circleDrawable);

        if (category == Category.SMALL) {
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    RoadMapStep step = roadMap.get(index);
                    if(!step.isCompleted()) {
                        circleDrawable.setColor(getResources().getColor(R.color.roadmap_color_complete));
                        step.setCompleted(true);
                    }
                    else {
                        circleDrawable.setColor(getColorBySize(category));
                        step.setCompleted(false);
                    }
                    imageView.setImageDrawable(circleDrawable);
                }
            });
        }
    }
    private void checkMiddleLevelStep() {
        // TODO: 하위 단계들이 모두 complete 되면 middle level을 complete하고 색상 변경 처리
    }

    private int getCirclePosY(int index) {
        return (index + 1) * dpToPx(INTERVAL);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
