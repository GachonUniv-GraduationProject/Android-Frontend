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
import android.widget.TextView;

public class SurveyOtherQuestion extends LinearLayout {

    private CheckBox[] questionCheckboxArray;
    private TextView questionNumberText;
    private TextView questionSentenceText;
    private Button completeButton;

    private int checkedCount = 0;

    public SurveyOtherQuestion(Context context, String[] items) {
        super(context);

        init(context, items);
    }

    private void init(Context context, String[] items) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_other_questions, this, true);

        LinearLayout questionCheckboxContainer = findViewById(R.id.question_checkbox_container);
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
    private void disableConfirmButton(Context context) {
        Animation buttonDisable = AnimationUtils.loadAnimation(context, R.anim.button_disable);
        completeButton.startAnimation(buttonDisable);
        completeButton.setVisibility(INVISIBLE);
    }
}
