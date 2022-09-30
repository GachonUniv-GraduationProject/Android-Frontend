package com.example.graduationproject;

public class RoadMapStep {
    private String name;
    private RoadmapDrawer.Category category;
    private int posX;
    private int posY;

    private RoadMapStep baseStep;

    public RoadMapStep(String name, RoadmapDrawer.Category category, RoadMapStep baseStep) {
        this.name = name;
        this.category = category;
        this.baseStep = baseStep;
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
}
