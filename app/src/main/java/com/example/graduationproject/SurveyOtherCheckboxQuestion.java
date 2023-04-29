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

/**
 * Checkbox layout for other questions
 * */
public class SurveyOtherCheckboxQuestion extends LinearLayout {

    /**
     * Array of check boxes for multiple choices
     * */
    private CheckBox[] questionCheckboxArray;
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

    /**
     * Number of check boxes checked by the user
     * */
    private int checkedCount = 0;

    public SurveyOtherCheckboxQuestion(Context context, String[] items) {
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
        LinearLayout questionCheckboxContainer = findViewById(R.id.question_content_container);
        // Initialize checkbox array
        questionCheckboxArray = new CheckBox[items.length];

        // Load textview and button from xml
        questionNumberText = findViewById(R.id.question_number_text);
        questionSentenceText = findViewById(R.id.question_sentence_text);
        completeButton = findViewById(R.id.complete_button);

        // Setting the content and UI components for each option
        for(int i = 0; i < items.length; i++) {
            questionCheckboxArray[i] = new CheckBox(context);
            // Set the layout parameter
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            questionCheckboxArray[i].setLayoutParams(params);
            // Set the content and add to container
            questionCheckboxArray[i].setText(items[i]);
            questionCheckboxContainer.addView(questionCheckboxArray[i]);
            // Set the checkbox click listener
            questionCheckboxArray[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((CheckBox)view).isChecked();
                    // Increasing the number of checks
                    if(checked) {
                        checkedCount++;
                    }
                    // Decrease the number when unchecked
                    else {
                        checkedCount--;
                    }

                    // Activate the Done button when there is more than 1 checked item
                    if(checkedCount > 0 && completeButton.getVisibility() == INVISIBLE)
                        enableConfirmButton(context);
                    // Disable when there are fewer than 0 items
                    else if(checkedCount == 0 && completeButton.getVisibility() == VISIBLE)
                        disableConfirmButton(context);
                }
            });
        }

        // Set the survey complete listener
        completeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the contents that user checked
                List<String> contents = new ArrayList<>();
                for(int i = 0; i < questionCheckboxArray.length; i++) {
                    if(questionCheckboxArray[i].isChecked())
                        contents.add(questionCheckboxArray[i].getText().toString());
                }

                // Call onComplete callback method
                surveyListener.onComplete(contents);
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
