package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessScouterActivity extends AppCompatActivity {

    private LinearLayout recommendFieldContainer;

    private List<RecommendedUserData> recommendedUserDataList = new ArrayList<>();
    private FloatingActionButton logoutFab;

    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_scouter);

        recommendFieldContainer = findViewById(R.id.recommend_field_container);

        //TalentRecommendFoldingCell talentRecommendFoldingCell = new TalentRecommendFoldingCell(this);
        //TalentRecommendFoldingCell talentRecommendFoldingCell1 = new TalentRecommendFoldingCell(this);
        //recommendFieldContainer.addView(talentRecommendFoldingCell);
        //recommendFieldContainer.addView(talentRecommendFoldingCell1);

        Button filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent businessSurveyIntent = new Intent(getApplicationContext(), BusinessSurveyActivity.class);
                startActivity(businessSurveyIntent);
            }
        });

        logoutFab = findViewById(R.id.logout_fab);
        logoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BusinessScouterActivity.this);
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠어요?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferencesManager.clearPreferences(BusinessScouterActivity.this);
                        finish();
                    }
                });
                builder.setNegativeButton("아니오", null);
                builder.create().show();
            }
        });

        loadRecommendData();
    }

    private void loadRecommendData() {
        int id = LoginData.currentLoginData.getUser().getId();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getRecommendedUser = service.getRecommendedUser(id);
        getRecommendedUser.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    String json = new Gson().toJson(response.body());
                    setRecommendedList(json);
                    updateRecommendedList();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setRecommendedList(String json) {
        JsonParser parser = new JsonParser();
        JsonObject rootObj = (JsonObject) parser.parse(json);
        JsonArray usersArr = (JsonArray) rootObj.get("recommended_user");

        for(JsonElement element : usersArr) {
            JsonObject elementObj = element.getAsJsonObject();
            int id = elementObj.get("user_id").getAsInt();
            String name = elementObj.get("name").getAsString();
            String email = elementObj.get("user_email").getAsString();
            String field = elementObj.get("field").getAsString();

            JsonArray skillArr = elementObj.get("skill").getAsJsonArray();
            String[] skills = new String[skillArr.size()];
            for(int i = 0; i < skillArr.size(); i++) {
                skills[i] = skillArr.get(i).getAsString();
            }

            float matchRatio = elementObj.get("match_ratio").getAsFloat();
            String experiences = elementObj.get("experience").getAsString();

            recommendedUserDataList.add(new RecommendedUserData(id, name, email, field, skills, matchRatio, experiences));
        }
    }

    private void updateRecommendedList() {
        for(int i = 0; i < recommendedUserDataList.size(); i++) {
            TalentRecommendFoldingCell talentRecommendFoldingCell = new TalentRecommendFoldingCell(this);
            RecommendedUserData data = recommendedUserDataList.get(i);
            talentRecommendFoldingCell.updateUserInfo(data.name, data.email, data.field, data.skills, data.experiences);
            recommendFieldContainer.addView(talentRecommendFoldingCell);
        }
    }

    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }

    public class RecommendedUserData {
        private int id;
        private String name;
        private String email;
        private String field;
        private String[] skills;
        private float matchRatio;
        private String experiences;

        public RecommendedUserData(int id, String name, String email, String field, String[] skills,
                                   float matchRatio, String experiences) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.field = field;
            this.skills = skills;
            this.matchRatio = matchRatio;
            this.experiences = experiences;
        }
    }
}