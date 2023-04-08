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
/**
 * Activity for business members to scout talented user
 * */
public class BusinessScouterActivity extends AppCompatActivity {
    /**
     * Talented User Recommendation Information Container
     * */
    private LinearLayout recommendFieldContainer;
    /**
     * Recommended Talented User Data List
     * */
    private List<RecommendedUserData> recommendedUserDataList = new ArrayList<>();
    /**
     * Floating action button for logout
     * */
    private FloatingActionButton logoutFab;
    /**
     * Back key handler for shutting down app
     * */
    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_scouter);

        // UI initialization
        initUI();

        // Load talented user's data from server
        loadRecommendData();
    }

    /**
     * Initialize UI by connecting views from xml
     * */
    private void initUI()
    {
        // Load container from xml
        recommendFieldContainer = findViewById(R.id.recommend_field_container);

        // Filter Button Listener Registration
        Button filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent businessSurveyIntent = new Intent(getApplicationContext(), BusinessSurveyActivity.class);
                startActivity(businessSurveyIntent);
            }
        });

        // Logout Fab Click Listener Registration
        logoutFab = findViewById(R.id.logout_fab);
        logoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show alert dialog to check logout confirm
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
    }

    /**
     * Load talented user's data from server
     * */
    private void loadRecommendData() {

        // Current business members' IDs are handed over to the server and talented user's data is requested.
        int id = LoginData.currentLoginData.getUser().getId();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getRecommendedUser = service.getRecommendedUser(id);
        getRecommendedUser.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    String json = new Gson().toJson(response.body());
                    // Convert json to class
                    setRecommendedList(json);
                    // Apply to UI
                    updateRecommendedList();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) { }
        });
    }

    /**
     * Applies talented user recommendation lists(from server) to data structures
     * */
    private void setRecommendedList(String json) {

        // Parse json to RecommendedUserData class
        JsonParser parser = new JsonParser();
        JsonObject rootObj = (JsonObject) parser.parse(json);
        JsonArray usersArr = (JsonArray) rootObj.get("recommended_user");

        for(JsonElement element : usersArr) {
            // Read data for each element and initialize the RecommendedUserData class and add it to the list.
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

    /**
     * Update the recommended talent list on the UI
     * */
    private void updateRecommendedList() {
        // For each data, create a Folding Cell and add it to the container.
        for(int i = 0; i < recommendedUserDataList.size(); i++) {
            TalentRecommendFoldingCell talentRecommendFoldingCell = new TalentRecommendFoldingCell(this);
            RecommendedUserData data = recommendedUserDataList.get(i);
            talentRecommendFoldingCell.updateUserInfo(data.name, data.email, data.field, data.skills, data.experiences);
            recommendFieldContainer.addView(talentRecommendFoldingCell);
        }
    }

    /**
     * Back key event process
     * */
    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }

    /**
     * Talented user data
     * */
    public class RecommendedUserData {
        /**
         * Id of the user
         * */
        private int id;
        /**
         * Name of the user
         * */
        private String name;
        /**
         * E-mail of the user
         * */
        private String email;
        /**
         * Specialized field of the user
         * */
        private String field;
        /**
         * Skills held by the user
         * */
        private String[] skills;
        /**
         * Matching ratio with company
         * */
        private float matchRatio;
        /**
         * User's experiences
         * */
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