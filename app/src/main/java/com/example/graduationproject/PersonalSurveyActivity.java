package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that survey normal users during the signup
 * */
public class PersonalSurveyActivity extends AppCompatActivity {

    /**
     * If the user decided the specific field
     * */
    private boolean isFieldDecided = false;

    /**
     * Container for questionnaires
     * */
    private LinearLayout surveyScrollContainer;
    /**
     * Animation effects that activate questions
     * */
    private Animation questionEnable;

    /**
     * Number of the current question
     * */
    private int currentQuestion = 1;
    /**
     * ID number of this user
     * */
    private int currentUserId;
    /**
     *
     * */
    private int loadingState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_survey);

        //
        questionEnable = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.question_enable);
        surveyScrollContainer = findViewById(R.id.survey_scroll_container);

        //
        currentUserId = getIntent().getIntExtra("newUserId", 0);

        //
        setFirstQuestion();
    }

    /**
     *
     * */
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

    /**
     *
     * */
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
                setDescriptiveQuestion(2, "");
            }
        });
    }

    /**
     *
     * */
    private void setDescriptiveQuestion(int question, String priorAnswer) {
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
                            setDescriptiveQuestion(3, answerEditText.getText().toString());
                        }
                        else {
                            //setOtherQuestions();
                            analyzeAnswers(priorAnswer, answerEditText.getText().toString());
                        }
                    }
                }
            }
        });
    }

    /**
     *
     * */
    private JsonObject getJsonContent(String type, String sentence) {
        JsonObject content = new JsonObject();
        content.addProperty("type", type);
        content.addProperty("data", sentence);
        return content;
    }

    /**
     * Analyze the answers that users typed in
     * */
    private void analyzeAnswers(String sentenceQ1, String sentenceQ2) {
        // Activate loading dialog during analyzing the answers
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        // Remove spaces before/after characters entered by the user
        sentenceQ1 = sentenceQ1.trim();
        sentenceQ2 = sentenceQ2.trim();

        // Analyze the positive or negative sentences
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> postPosNegSurvey = service.postPersonalSurvey(currentUserId, getJsonContent("NLP_POS_NEG", sentenceQ1));
        postPosNegSurvey.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    // Get the positive fields
                    String posNegJson = new Gson().toJson(response.body());
                    getPositiveFields(posNegJson);
                    // If both analysis are completed, dismiss the laoding dialog
                    loadingState += 1;
                    if(loadingState == 3)
                        loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) { }
        });

        // Analyze the field in the sentence described in the experience
        Call<Object> postFieldSurvey = service.postPersonalSurvey(currentUserId, getJsonContent("NLP_FIELD", sentenceQ2));
        postFieldSurvey.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    // If both analysis are completed, dismiss the laoding dialog
                    loadingState += 2;
                    if(loadingState == 3)
                        loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) { }
        });
    }

    /**
     * Find the positively described areas and add them to the field selection list.
     * */
    private void getPositiveFields(String json) {
        List<String> positiveFields = new ArrayList<>();
        // Parse root json object
        JsonParser parser = new JsonParser();
        JsonObject rootObj = (JsonObject) parser.parse(json);
        JsonArray classifyArray = (JsonArray) rootObj.get("classify");

        // Read the favorable fields
        for(int i = 0; i < classifyArray.size(); i++) {
            JsonObject elementObj = classifyArray.get(i).getAsJsonObject();
            if(elementObj.get("value").getAsInt() >= 0) {
                positiveFields.add(elementObj.get("field").getAsString());
            }
        }

        // Show the field selection question with the positively described fields
        setOtherQuestions("가고자 하는 분야를 골라주세요.", positiveFields.toArray(new String[positiveFields.size()]));
    }

    /**
     * Show the radio button selection question
     * */
    private void setOtherQuestions(String question, String[] sample) {
        // Initialize the radio button selection question
        SurveyOtherRadioButtonQuestion otherQuestion = new SurveyOtherRadioButtonQuestion(this, sample);

        otherQuestion.setSurveyListener(new SurveyListener() {
            @Override
            public void onComplete(String content) {
                // Create a json object that includes a user-selected field
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", content);

                // Send the field to server
                RetrofitService service = RetrofitClient.getRetrofitService();
                Call<Object> postPersonalField = service.postPersonalField(currentUserId, content, jsonObject);
                postPersonalField.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if(response.isSuccessful()) {
                            // Successful membership registration and go to login screen
                            Toast.makeText(PersonalSurveyActivity.this, "설문조사 및 회원가입에 성공하였습니다.\n로그인하여 개발자의 길을 준비해보세요!", Toast.LENGTH_LONG).show();
                            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(loginActivity);
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) { }
                });
            }
            @Override
            public void onComplete(List<String> content) { }
        });

        // Show the question
        otherQuestion.setQuestion(currentQuestion, question);
        surveyScrollContainer.addView(otherQuestion);
        otherQuestion.startAnimation(questionEnable);
    }
}