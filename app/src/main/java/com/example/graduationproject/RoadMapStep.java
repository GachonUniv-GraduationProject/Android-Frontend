package com.example.graduationproject;

public class RoadMapStep {
    private String name;
    private boolean locked;

    private boolean completed;
    private RoadmapDrawer.Category category;
    private int posX;
    private int posY;

    private RoadMapStep baseStep;

    public RoadMapStep(String name, RoadmapDrawer.Category category, RoadMapStep baseStep, boolean locked) {
        this.name = name;
        this.category = category;
        this.baseStep = baseStep;
        this.locked = locked;
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
}
