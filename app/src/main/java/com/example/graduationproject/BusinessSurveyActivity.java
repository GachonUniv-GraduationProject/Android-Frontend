package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessSurveyActivity extends AppCompatActivity {
    private LinearLayout questionContainer;

    private String[] paths = {"Frontend_skill.json", "Backend_skill.json"};
    // TODO: 분야는 어디 한군데서 한번에 관리하기
    private String[] fields = {"Frontend", "Backend"};

    private List<FieldSkill> fieldSkillList = new ArrayList<>();
    private List<String> requiredList = new ArrayList<>();
    private String none = "없음";

    private String selectedField;
    private int currentId;
    
    private Animation questionEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_survey);

        questionContainer = findViewById(R.id.survey_scroll_container);

        questionEnable = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.question_enable);
        loadSkills();

        currentId = getIntent().getIntExtra("newUserId", 0);
        createFirstQuestion(currentId);
    }
    // TODO: 1번문항 분야 radio button 리스너 추가
    // TODO: 1번에서 고른 분야에 따라 기술, 프레임워크, 기타 순차적으로 SurveyOtherCheckboxQuestion 으로 추가

    private void createFirstQuestion(int id) {
        SurveyOtherRadioButtonQuestion radioButtonQuestion = new SurveyOtherRadioButtonQuestion(this, fields);
        radioButtonQuestion.setSurveyListener(new SurveyListener() {
            @Override
            public void onComplete(String content) {
                selectedField = content;
                for(int i = 0; i < fieldSkillList.size(); i++) {
                    if(content.equals(fields[i])) {
                        createQuestion(fieldSkillList.get(i), 1);
                        break;
                    }
                }
                radioButtonQuestion.hideConfirmButton();
                radioButtonQuestion.clearAnimation();
            }
            @Override
            public void onComplete(List<String> content) { }
        });
        radioButtonQuestion.setQuestion(1, "어떤 분야의 개발자가 필요하신가요?");
        questionContainer.addView(radioButtonQuestion);
        radioButtonQuestion.startAnimation(questionEnable);
    }

    private void createQuestion(FieldSkill skill, int sequence) {
        SurveyOtherCheckboxQuestion checkboxQuestion;
        if(sequence == 1) { // Skill
            checkboxQuestion = new SurveyOtherCheckboxQuestion(this, skill.getTechArray());
            checkboxQuestion.setSurveyListener(new SurveyListener() {
                @Override
                public void onComplete(String content) { }

                @Override
                public void onComplete(List<String> contents) {
                    if(!contents.contains(none))
                        requiredList.addAll(contents);
                    checkboxQuestion.clearAnimation();
                    checkboxQuestion.hideConfirmButton();
                    createQuestion(skill, sequence + 1);
                }
            });
            checkboxQuestion.setQuestion(2, "어떤 기술을 가진 개발자가 필요하신가요?");

        }
        else if(sequence == 2) { // Framework
            checkboxQuestion = new SurveyOtherCheckboxQuestion(this, skill.getFrameworkArray());
            checkboxQuestion.setSurveyListener(new SurveyListener() {
                @Override
                public void onComplete(String content) { }

                @Override
                public void onComplete(List<String> contents) {
                    if(!contents.contains(none))
                        requiredList.addAll(contents);
                    checkboxQuestion.clearAnimation();
                    checkboxQuestion.hideConfirmButton();
                    createQuestion(skill, sequence + 1);
                }
            });
            checkboxQuestion.setQuestion(3, "어떤 프레임워크를 할 줄 아는 개발자가 필요하신가요?");
        }
        else { // Others
            checkboxQuestion = new SurveyOtherCheckboxQuestion(this, skill.getOthersArray());
            checkboxQuestion.setSurveyListener(new SurveyListener() {
                @Override
                public void onComplete(String content) { }

                @Override
                public void onComplete(List<String> contents) {
                    if(!contents.contains(none))
                        requiredList.addAll(contents);
                    checkboxQuestion.clearAnimation();
                    sendSurveyToServer();
                }
            });
            checkboxQuestion.setQuestion(4, "어떤 사항에 부합하는 개발자가 필요하신가요?");
        }

        questionContainer.addView(checkboxQuestion);
        checkboxQuestion.startAnimation(questionEnable);
    }

    private void sendSurveyToServer() {
        JsonObject rootObj = new JsonObject();
        rootObj.addProperty("type", "USR_COM_MATCH");

        JsonObject companyDataObj = new JsonObject();
        companyDataObj.addProperty("company_id", currentId);
        companyDataObj.addProperty("field", selectedField);

        JsonArray skillArr = new JsonArray();
        for (String skill : requiredList)
            skillArr.add(skill);
        companyDataObj.add("skill", skillArr);

        JsonObject companyObj = new JsonObject();
        companyObj.add("company", companyDataObj);
        rootObj.add("data", companyObj);

        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> postCompanySurvey = service.postCompanySurvey(rootObj);
        postCompanySurvey.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginActivity);
                    Toast.makeText(BusinessSurveyActivity.this, "설문조사 및 회원가입에 성공하였습니다.\n로그인하여 개발자를 추천 받아보세요!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private String[] loadSkillData() {
        String[] jsonArr = new String[paths.length];
        try {
            for(int i = 0; i < paths.length; i++) {
                InputStream is = getAssets().open(paths[i]);

                int fileSize = is.available();

                byte[] buffer = new byte[fileSize];
                is.read(buffer);
                is.close();

                jsonArr[i] = new String(buffer, "UTF-8");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArr;
    }

    private void loadSkills() {
        String[] jsonArr = loadSkillData();
        for(int i = 0; i < jsonArr.length; i++) {
            fieldSkillList.add(new FieldSkill());
            JsonParser parser = new JsonParser();
            JsonObject rootObj = (JsonObject) parser.parse(jsonArr[i]);
            JsonObject fieldObj = (JsonObject) rootObj.get(fields[i]);
            JsonArray techObj = (JsonArray) fieldObj.get("기술");
            for(JsonElement elem : techObj) {
                fieldSkillList.get(i).addTech(elem.getAsString());
            }
            fieldSkillList.get(i).addTech(none);
            JsonArray frameworkObj = (JsonArray) fieldObj.get("프레임워크");
            for(JsonElement elem : frameworkObj) {
                fieldSkillList.get(i).addFramework(elem.getAsString());
            }
            fieldSkillList.get(i).addFramework(none);
            JsonArray othersObj = (JsonArray) fieldObj.get("기타");
            for(JsonElement elem : othersObj) {
                fieldSkillList.get(i).addOthers(elem.getAsString());
            }
            fieldSkillList.get(i).addOthers(none);
        }
    }

    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public class FieldSkill {
        private List<String> techList = new ArrayList<>();
        private List<String> frameworkList = new ArrayList<>();
        private List<String> othersList = new ArrayList<>();

        public void addTech(String skill) {
            techList.add(skill);
        }
        public void addFramework(String framework) {
            frameworkList.add(framework);
        }
        public void addOthers(String other) {
            othersList.add(other);
        }

        public List<String> getTechList() {
            return techList;
        }

        public List<String> getFrameworkList() {
            return frameworkList;
        }

        public List<String> getOthersList() {
            return othersList;
        }

        public String[] getTechArray() {
            return techList.toArray(new String[techList.size()]);
        }
        public String[] getFrameworkArray() {
            return frameworkList.toArray(new String[frameworkList.size()]);
        }
        public String[] getOthersArray() {
            return othersList.toArray(new String[othersList.size()]);
        }
    }
}