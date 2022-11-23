package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class PersonalSurveyActivity extends AppCompatActivity {

    private boolean isFieldDecided = false;

    private LinearLayout surveyScrollContainer;
    private Animation questionEnable;

    private int currentQuestion = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_survey);

        questionEnable = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.question_enable);
        surveyScrollContainer = findViewById(R.id.survey_scroll_container);

        setFirstQuestion();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if(focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int)ev.getX(), y = (int)ev.getY();
            if(!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setFirstQuestion() {
        LinearLayout groupQuestion1 = findViewById(R.id.group_question_1);
        groupQuestion1.startAnimation(questionEnable);

        RadioGroup fieldRadioGroup = findViewById(R.id.field_radio_group);
        fieldRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.field_radio_button_true) {
                    isFieldDecided = true;
                }
                else if(i == R.id.field_radio_button_false) {
                    isFieldDecided = false;
                }
                groupQuestion1.clearAnimation();
                currentQuestion++;
                setDescriptiveQuestion(2);
            }
        });
    }

    private void setDescriptiveQuestion(int question) {
        LinearLayout groupQuestion;
        EditText answerEditText;
        if (question == 2) {
            groupQuestion = findViewById(R.id.group_question_2);
            answerEditText = findViewById(R.id.edittext_question_2);
        }
        else if(question == 3) {
            groupQuestion = findViewById(R.id.group_question_3);
            answerEditText = findViewById(R.id.edittext_question_3);
        }
        else
            return;
        groupQuestion.setVisibility(View.VISIBLE);
        groupQuestion.startAnimation(questionEnable);

        answerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    if(answerEditText.getText().toString().length() > 0) {
                        groupQuestion.clearAnimation();
                        currentQuestion++;
                        if (question == 2) {
                            setDescriptiveQuestion(3);
                        }
                        else {
                            setOtherQuestions();
                        }
                    }
                }
            }
        });
    }

    private void setOtherQuestions() {
        String[] sample = {"안드로이드", "IOS", "Kotlin", "Swift"};
        SurveyOtherQuestion otherQuestion = new SurveyOtherQuestion(this, sample);
        otherQuestion.setQuestion(currentQuestion, "앱 개발에서 경험한 것이나 자신있는 것을 골라주세요.");
        surveyScrollContainer.addView(otherQuestion);
        otherQuestion.startAnimation(questionEnable);
    }
}