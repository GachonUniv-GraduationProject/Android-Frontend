package com.example.graduationproject;

import java.util.ArrayList;
import java.util.List;

public class Experience {
    private String field;
    private List<String> projects = new ArrayList<>();

    public Experience(String field, String detail) {
        this.field = field;
        projects.add(detail);
    }

    public String getField() {
        return field;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void addProject(String detail) {
        projects.add(detail);
    }
}
