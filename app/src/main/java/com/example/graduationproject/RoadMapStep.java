package com.example.graduationproject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadMapStep {
    private String name;
    private boolean locked;

    private boolean completed;
    private RoadmapDrawer.Category category;
    private int posX;
    private int posY;

    private RoadMapStep baseStep;
    private ArrayList<RoadMapStep> childrenSteps = null;
    private int childCompletedCount = 0;
    private RoadmapDrawer drawer;

    public RoadMapStep(String name, RoadmapDrawer.Category category, RoadMapStep baseStep,
                       boolean locked, boolean completed, RoadmapDrawer drawer) {
        this.name = name;
        this.category = category;
        this.baseStep = baseStep;
        this.locked = locked;
        this.completed = completed;
        this.drawer = drawer;
        if(baseStep != null)
            baseStep.registerBaseStep(this);
    }

    public void setPos(int x, int y) {
        posX = x;
        posY = y;
    }
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }

    public String getName() {
        return name;
    }

    public RoadmapDrawer.Category getCategory() {
        return category;
    }

    public RoadMapStep getBaseStep() {
        return baseStep;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    private void registerBaseStep(RoadMapStep step) {
        if(childrenSteps == null)
            childrenSteps = new ArrayList<>();

        childrenSteps.add(step);
    }
    public void checkSubSteps() {
        childCompletedCount = 0;
        for(RoadMapStep childStep : childrenSteps) {
            childCompletedCount += childStep.isCompleted() ? 1 : 0;
        }

        if(childCompletedCount > 0 && childCompletedCount == childrenSteps.size()) {
            openNextLevel();
        }
    }

    public ArrayList<RoadMapStep> getChildrenSteps() {
        return childrenSteps;
    }

    private void openNextLevel() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "next_level");
        int id = LoginData.currentLoginData.getUser().getId();
        String field = LoginData.currentLoginData.getField();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> callNextLevel = service.putRoadmap(id, field, jsonObject);
        callNextLevel.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    String json = new Gson().toJson(response.body());
                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = (JsonObject) parser.parse(json);
                    String name = rootObj.get("name").getAsString();
                    drawer.openNextLevel(name);                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
}
