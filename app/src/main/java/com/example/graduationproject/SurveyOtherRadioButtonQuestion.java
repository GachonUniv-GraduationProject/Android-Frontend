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

/**
 *
 * */
public class SurveyOtherRadioButtonQuestion extends LinearLayout {

    /**
     * Group of single-choice radio buttons
     * */
    private RadioGroup questionRadioGroup;
    /**
     * Array of single-choice radio buttons
     * */
    private RadioButton[] questionRadioButtonArray;
    /**
     * Textview to show question number
     * */
    private TextView questionNumberText;
    /**
     * Textview to show questionnaire
     * */
    private TextView questionSentenceText;
    /**
     * Survey complete button
     * */
    private Button completeButton;

    /**
     * Waiting Listener Interface for Response Complete
     * */
    private SurveyListener surveyListener;


    public SurveyOtherRadioButtonQuestion(Context context, String[] items) {
        super(context);

        init(context, items);
    }

    /**
     * Apply survey listener
     * */
    public void setSurveyListener(SurveyListener surveyListener) {
        this.surveyListener = surveyListener;
    }

    /**
     * Initialize UI components
     * */
    private void init(Context context, String[] items) {
        // Inflate this layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_other_questions, this, true);

        // Load container from xml
        LinearLayout questionContentContainer = findViewById(R.id.question_content_container);
        // Initialize radio button and radio group
        questionRadioGroup = new RadioGroup(context);
        questionRadioButtonArray = new RadioButton[items.length];
        questionContentContainer.addView(questionRadioGroup);

        // Load textview and button from xml
        questionNumberText = findViewById(R.id.question_number_text);
        questionSentenceText = findViewById(R.id.question_sentence_text);
        completeButton = findViewById(R.id.complete_button);

        // Setting the content and UI components for each option
        for(int i = 0; i < items.length; i++) {
            questionRadioButtonArray[i] = new RadioButton(context);
            // Set the layout parameter
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            questionRadioButtonArray[i].setLayoutParams(params);
            // Set the content and add to container
            questionRadioButtonArray[i].setText(items[i]);
            questionRadioGroup.addView(questionRadioButtonArray[i]);
            // Set the radio button click listener
            questionRadioButtonArray[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    enableConfirmButton(context);
                }
            });
        }

        // Set the survey complete listener
        completeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the content that user selected
                int selected = questionRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selected);
                String content = radioButton.getText().toString();
                // Call onComplete callback method
                surveyListener.onComplete(content);
            }
        });
    }

    /**
     * Set the question content
     * */
    public void setQuestion(int questionNumber, String questionSentence) {
        questionNumberText.setText("Q" + questionNumber + ".");
        questionSentenceText.setText(questionSentence);
    }

    /**
     * Enabling the complete button
     * */
    private void enableConfirmButton(Context context) {
        Animation buttonEnable = AnimationUtils.loadAnimation(context, R.anim.button_enable);
        completeButton.startAnimation(buttonEnable);
        completeButton.setVisibility(VISIBLE);
    }
    /**
     * Disabling the complete button
     * */
    public void disableConfirmButton(Context context) {
        Animation buttonDisable = AnimationUtils.loadAnimation(context, R.anim.button_disable);
        completeButton.startAnimation(buttonDisable);
        completeButton.setVisibility(INVISIBLE);
    }
    /**
     * Disable the Finish button to take up no space
     * */
    public void hideConfirmButton() {
        completeButton.setVisibility(GONE);
    }
}
