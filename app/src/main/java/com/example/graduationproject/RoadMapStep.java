package com.example.graduationproject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Data classes at each roadmap step
 * */
public class RoadMapStep {
    /**
     * Name of step
     * */
    private String name;
    /**
     * Whether the step is locked
     * */
    private boolean locked;

    /**
     * Whether the step is already completed
     * */
    private boolean completed;
    /**
     * Size category of this step
     * */
    private RoadmapDrawer.Category category;
    /**
     * Position X of circle node
     * */
    private int posX;
    /**
     * Position Y of circle node
     * */
    private int posY;

    /**
     * Base skill step
     * */
    private RoadMapStep baseStep;
    /**
     * Children skill step list
     * */
    private ArrayList<RoadMapStep> childrenSteps = null;
    /**
     * Completed children skill amount
     * */
    private int childCompletedCount = 0;
    /**
     * Roadmap drawer instance
     * */
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

    /**
     * Set position x and y
     * */
    public void setPos(int x, int y) {
        posX = x;
        posY = y;
    }
    /**
     * Get the position X
     * */
    public int getPosX() {
        return posX;
    }
    /**
     * Get the position Y
     * */
    public int getPosY() {
        return posY;
    }

    /**
     * Get the name of step
     * */
    public String getName() {
        return name;
    }

    /**
     * Get the size category of this step
     * */
    public RoadmapDrawer.Category getCategory() {
        return category;
    }

    /**
     * Get the base skill step
     * */
    public RoadMapStep getBaseStep() {
        return baseStep;
    }

    /**
     * Get whether this step is locked
     * */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Set its lock state
     * */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Get whether this step is already completed
     * */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Set its completion state
     * */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Add children skill step
     * */
    private void registerBaseStep(RoadMapStep step) {
        if(childrenSteps == null)
            childrenSteps = new ArrayList<>();

        childrenSteps.add(step);
    }
    /**
     * Check completed children step amount
     * */
    public void checkSubSteps() {
        childCompletedCount = 0;
        for(RoadMapStep childStep : childrenSteps) {
            childCompletedCount += childStep.isCompleted() ? 1 : 0;
        }

        if(childCompletedCount > 0 && childCompletedCount == childrenSteps.size()) {
            openNextLevel();
        }
    }

    /**
     * Get children skill steps list
     * */
    public ArrayList<RoadMapStep> getChildrenSteps() {
        return childrenSteps;
    }

    /**
     * Unlock next level
     * */
    private void openNextLevel() {
        // Create json object
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "next_level");
        // Set the user and field info and send to server
        int id = LoginData.currentLoginData.getUser().getId();
        String field = LoginData.currentLoginData.getField();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> callNextLevel = service.putRoadmap(id, field, jsonObject);
        callNextLevel.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    // Get the result and apply to UI
                    String json = new Gson().toJson(response.body());
                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = (JsonObject) parser.parse(json);
                    String name = rootObj.get("name").getAsString();
                    drawer.openNextLevel(name);                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) { }
        });
    }
}
