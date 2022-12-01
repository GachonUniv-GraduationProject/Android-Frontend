package com.example.graduationproject;

import java.util.ArrayList;

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

    public RoadMapStep(String name, RoadmapDrawer.Category category, RoadMapStep baseStep, boolean locked) {
        this.name = name;
        this.category = category;
        this.baseStep = baseStep;
        this.locked = locked;
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
        baseStep.checkSubSteps(completed);
        this.completed = completed;
    }

    private void registerBaseStep(RoadMapStep step) {
        if(childrenSteps == null)
            childrenSteps = new ArrayList<>();

        childrenSteps.add(step);
    }
    private void checkSubSteps(boolean completed) {
        if(completed) {
            childCompletedCount++;
        }
        else {
            childCompletedCount--;
        }

        if(childCompletedCount == childrenSteps.size()) {
            // TODO: 다음 중간 노드 unlock인데 어떻게 접근/연결할지는 고민해봐야됨..
        }
    }
}
