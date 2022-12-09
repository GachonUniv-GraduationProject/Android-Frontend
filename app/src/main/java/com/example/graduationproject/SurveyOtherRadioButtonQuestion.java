package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyOtherRadioButtonQuestion extends LinearLayout {

    private RadioGroup questionRadioGroup;
    private RadioButton[] questionRadioButtonArray;
    private TextView questionNumberText;
    private TextView questionSentenceText;
    private Button completeButton;

    private SurveyListener surveyListener;


    public SurveyOtherRadioButtonQuestion(Context context, String[] items) {
        super(context);

        init(context, items);
    }

    public void setSurveyListener(SurveyListener surveyListener) {
        this.surveyListener = surveyListener;
    }

    private void init(Context context, String[] items) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_other_questions, this, true);

        LinearLayout questionContentContainer = findViewById(R.id.question_content_container);
        questionRadioGroup = new RadioGroup(context);
        questionRadioButtonArray = new RadioButton[items.length];
        questionContentContainer.addView(questionRadioGroup);

        questionNumberText = findViewById(R.id.question_number_text);
        questionSentenceText = findViewById(R.id.question_sentence_text);
        completeButton = findViewById(R.id.complete_button);

        for(int i = 0; i < items.length; i++) {
            questionRadioButtonArray[i] = new RadioButton(context);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            questionRadioButtonArray[i].setLayoutParams(params);
            questionRadioButtonArray[i].setText(items[i]);
            questionRadioGroup.addView(questionRadioButtonArray[i]);
            questionRadioButtonArray[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    enableConfirmButton(context);
                }
            });
        }

        completeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = questionRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selected);
                String content = radioButton.getText().toString();
                surveyListener.onComplete(content);
                /**/
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
