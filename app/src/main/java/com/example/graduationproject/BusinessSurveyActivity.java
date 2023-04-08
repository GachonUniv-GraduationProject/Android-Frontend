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

/**
 * Activity for survey of human resources required by business members
 * */
public class BusinessSurveyActivity extends AppCompatActivity {
    /**
     * Survey Container
     * */
    private LinearLayout questionContainer;

    /**
     * Array of skill information file path
     * */
    private String[] paths = {"Frontend_skill.json", "Backend_skill.json"};
    // TODO: 분야는 어디 한군데서 한번에 관리하기
    /**
     * Fields to be selected by business member
     * */
    private String[] fields = {"Frontend", "Backend"};

    /**
     * Skill data list by fields
     * */
    private List<FieldSkill> fieldSkillList = new ArrayList<>();
    /**
     * Required skill list
     * */
    private List<String> requiredList = new ArrayList<>();
    /**
     * String indicating 'None(없음)'
     * */
    private String none;

    /**
     * Field that business member selected
     * */
    private String selectedField;
    /**
     * Business user id
     * */
    private int currentId;

    /**
     * Animated effect to show question
     * */
    private Animation questionEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_survey);

        // Load container from xml
        questionContainer = findViewById(R.id.survey_scroll_container);
        // Load 'None' string from string.xml
        none = getResources().getString(R.string.none);

        // Load animation
        questionEnable = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.question_enable);
        // Load skill data
        loadSkills();

        // Get business user's id
        currentId = getIntent().getIntExtra("newUserId", 0);
        // Start first question
        createFirstQuestion();
    }

    /**
     * Create the first question
     * */
    private void createFirstQuestion() {
        // Show radio button question
        SurveyOtherRadioButtonQuestion radioButtonQuestion = new SurveyOtherRadioButtonQuestion(this, fields);
        // Register the survey answer listener
        radioButtonQuestion.setSurveyListener(new SurveyListener() {
            @Override
            public void onComplete(String content) {
                // Apply recruitment field selection results
                selectedField = content;
                for(int i = 0; i < fieldSkillList.size(); i++) {
                    // Generate a questionnaire for the selected area.
                    if(content.equals(fields[i])) {
                        createQuestion(fieldSkillList.get(i), 1);
                        break;
                    }
                }
                // Hide the confirm button, remove animation to prevent replay of animation effects
                radioButtonQuestion.hideConfirmButton();
                radioButtonQuestion.clearAnimation();
            }
            @Override
            public void onComplete(List<String> content) { }
        });
        // Activate questions by adding question statements
        radioButtonQuestion.setQuestion(1, "어떤 분야의 개발자가 필요하신가요?");
        questionContainer.addView(radioButtonQuestion);
        radioButtonQuestion.startAnimation(questionEnable);
    }

    /**
     * Create general questions
     * */
    private void createQuestion(FieldSkill skill, int sequence) {
        // Survey Checkbox UI Set
        SurveyOtherCheckboxQuestion checkboxQuestion;

        if(sequence == 1) { // Registration of required 'Skill' competency questionnaire
            checkboxQuestion = new SurveyOtherCheckboxQuestion(this, skill.getTechArray());
            checkboxQuestion.setSurveyListener(new SurveyListener() {
                @Override
                public void onComplete(String content) { }

                @Override
                public void onComplete(List<String> contents) {
                    // Apply selection result list for multiple selections
                    if(!contents.contains(none))
                        requiredList.addAll(contents);
                    // Hide the confirm button, remove animation to prevent replay of animation effects
                    checkboxQuestion.clearAnimation();
                    checkboxQuestion.hideConfirmButton();
                    // Create next question
                    createQuestion(skill, sequence + 1);
                }
            });
            checkboxQuestion.setQuestion(2, "어떤 기술을 가진 개발자가 필요하신가요?");

        }
        else if(sequence == 2) { // Registration of required 'Framework' competency questionnaire
            checkboxQuestion = new SurveyOtherCheckboxQuestion(this, skill.getFrameworkArray());
            checkboxQuestion.setSurveyListener(new SurveyListener() {
                @Override
                public void onComplete(String content) { }

                @Override
                public void onComplete(List<String> contents) {
                    // Apply selection result list for multiple selections
                    if(!contents.contains(none))
                        requiredList.addAll(contents);
                    // Hide the confirm button, remove animation to prevent replay of animation effects
                    checkboxQuestion.clearAnimation();
                    checkboxQuestion.hideConfirmButton();
                    // Create next question
                    createQuestion(skill, sequence + 1);
                }
            });
            checkboxQuestion.setQuestion(3, "어떤 프레임워크를 할 줄 아는 개발자가 필요하신가요?");
        }
        else { // Registration of required 'Others' competency questionnaire
            checkboxQuestion = new SurveyOtherCheckboxQuestion(this, skill.getOthersArray());
            checkboxQuestion.setSurveyListener(new SurveyListener() {
                @Override
                public void onComplete(String content) { }

                @Override
                public void onComplete(List<String> contents) {
                    // Apply selection result list for multiple selections
                    if(!contents.contains(none))
                        requiredList.addAll(contents);
                    // Remove animation to prevent replay of animation effects
                    checkboxQuestion.clearAnimation();
                    // Send survey data to server
                    sendSurveyToServer();
                }
            });
            checkboxQuestion.setQuestion(4, "어떤 사항에 부합하는 개발자가 필요하신가요?");
        }

        // Activate the question with animated effect
        questionContainer.addView(checkboxQuestion);
        checkboxQuestion.startAnimation(questionEnable);
    }

    /**
     * Send survey data to server
     * */
    private void sendSurveyToServer() {
        // Create root json object
        JsonObject rootObj = new JsonObject();
        rootObj.addProperty("type", "USR_COM_MATCH");

        // Add company's information and field
        JsonObject companyDataObj = new JsonObject();
        companyDataObj.addProperty("company_id", currentId);
        companyDataObj.addProperty("field", selectedField);

        // Add required skills
        JsonArray skillArr = new JsonArray();
        for (String skill : requiredList)
            skillArr.add(skill);
        companyDataObj.add("skill", skillArr);

        // Add company's information to root
        JsonObject companyObj = new JsonObject();
        companyObj.add("company", companyDataObj);
        rootObj.add("data", companyObj);

        // Send generated json data to server
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> postCompanySurvey = service.postCompanySurvey(rootObj);
        postCompanySurvey.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // If it has been successfully processed, turn it over to the login screen.
                if(response.isSuccessful()) {
                    Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginActivity);
                    Toast.makeText(BusinessSurveyActivity.this, "설문조사 및 회원가입에 성공하였습니다.\n로그인하여 개발자를 추천 받아보세요!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) { }
        });
    }

    /**
     * All skill data is read from the file and returned in the form of a json string
     * */
    private String[] loadSkillData() {
        String[] jsonArr = new String[paths.length];
        try {
            // Text data is retrieved to InputStream for each file.
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

    /**
     * The json data read from the file is converted into a list form of class.
     * */
    private void loadSkills() {
        String[] jsonArr = loadSkillData();
        // Add the skills of each field to the list by category
        for(int i = 0; i < jsonArr.length; i++) {
            // Parse json
            fieldSkillList.add(new FieldSkill());
            JsonParser parser = new JsonParser();
            JsonObject rootObj = (JsonObject) parser.parse(jsonArr[i]);
            JsonObject fieldObj = (JsonObject) rootObj.get(fields[i]);

            // Get "기술(Tech)" category
            JsonArray techObj = (JsonArray) fieldObj.get("기술");
            for(JsonElement elem : techObj) {
                fieldSkillList.get(i).addTech(elem.getAsString());
            }
            // Add "None" for question
            fieldSkillList.get(i).addTech(none);

            // Get "프레임워크(Framework)" category
            JsonArray frameworkObj = (JsonArray) fieldObj.get("프레임워크");
            for(JsonElement elem : frameworkObj) {
                fieldSkillList.get(i).addFramework(elem.getAsString());
            }
            // Add "None" for question
            fieldSkillList.get(i).addFramework(none);

            // Get "기타(Others)" category
            JsonArray othersObj = (JsonArray) fieldObj.get("기타");
            for(JsonElement elem : othersObj) {
                fieldSkillList.get(i).addOthers(elem.getAsString());
            }
            // Add "None" for question
            fieldSkillList.get(i).addOthers(none);
        }
    }

    /**
     * A data class that holds skill data for a particular field by category
     * */
    public class FieldSkill {
        /**
         * "기술(Tech)" category skill list
         * */
        private List<String> techList = new ArrayList<>();
        /**
         * "프레임워크(Framework)" category skill list
         * */
        private List<String> frameworkList = new ArrayList<>();
        /**
         * "기타(Others)" category skill list
         * */
        private List<String> othersList = new ArrayList<>();

        /**
         * Add technical skill to list
         * */
        public void addTech(String skill) {
            techList.add(skill);
        }
        /**
         * Add framework skill to list
         * */
        public void addFramework(String framework) {
            frameworkList.add(framework);
        }
        /**
         * Add other skill to list
         * */
        public void addOthers(String other) {
            othersList.add(other);
        }

        /**
         * Gets the technical skill list in the form of an array
         * */
        public String[] getTechArray() {
            return techList.toArray(new String[techList.size()]);
        }
        /**
         * Gets the framework skill list in the form of an array
         * */
        public String[] getFrameworkArray() {
            return frameworkList.toArray(new String[frameworkList.size()]);
        }
        /**
         * Gets the other skill list in the form of an array
         * */
        public String[] getOthersArray() {
            return othersList.toArray(new String[othersList.size()]);
        }
    }
}