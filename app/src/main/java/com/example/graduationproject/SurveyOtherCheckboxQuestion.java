package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SurveyOtherCheckboxQuestion extends LinearLayout {

    private CheckBox[] questionCheckboxArray;
    private TextView questionNumberText;
    private TextView questionSentenceText;
    private Button completeButton;
    private SurveyListener surveyListener;

    private int checkedCount = 0;

    public SurveyOtherCheckboxQuestion(Context context, String[] items) {
        super(context);

        init(context, items);
    }

    public void setSurveyListener(SurveyListener surveyListener) {
        this.surveyListener = surveyListener;
    }

    private void init(Context context, String[] items) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_other_questions, this, true);

        LinearLayout questionCheckboxContainer = findViewById(R.id.question_content_container);
        questionCheckboxArray = new CheckBox[items.length];

        questionNumberText = findViewById(R.id.question_number_text);
        questionSentenceText = findViewById(R.id.question_sentence_text);
        completeButton = findViewById(R.id.complete_button);

        for(int i = 0; i < items.length; i++) {
            questionCheckboxArray[i] = new CheckBox(context);
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            questionCheckboxArray[i].setLayoutParams(params);
            questionCheckboxArray[i].setText(items[i]);
            questionCheckboxContainer.addView(questionCheckboxArray[i]);
            questionCheckboxArray[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((CheckBox)view).isChecked();
                    if(checked) {
                        checkedCount++;
                    }
                    else {
                        checkedCount--;
                    }

                    if(checkedCount > 0 && completeButton.getVisibility() == INVISIBLE)
                        enableConfirmButton(context);
                    else if(checkedCount == 0 && completeButton.getVisibility() == VISIBLE)
                        disableConfirmButton(context);
                }
            });
        }

        completeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> contents = new ArrayList<>();
                for(int i = 0; i < questionCheckboxArray.length; i++) {
                    if(questionCheckboxArray[i].isChecked())
                        contents.add(questionCheckboxArray[i].getText().toString());
                }

                surveyListener.onComplete(contents);
            }
        });
    }

    public void setQuestion(int questionNumber, String questionSentence) {
        questionNumberText.setText("Q" + questionNumber + ".");
        questionSentenceText.setText(questionSentence);
    }

    private void enableConfirmButton(Context context) {
        Animation buttonEnable = AnimationUtils.loadAnimation(context, R.anim.button_enable);
        completeButton.startAnimation(buttonEnable);
        completeButton.setVisibility(VISIBLE);
    }
    public void disableConfirmButton(Context context) {
        Animation buttonDisable = AnimationUtils.loadAnimation(context, R.anim.button_disable);
        completeButton.startAnimation(buttonDisable);
        completeButton.setVisibility(INVISIBLE);
    }
    public void hideConfirmButton() {
        completeButton.setVisibility(GONE);
    }
}
