package com.example.graduationproject;

import java.util.ArrayList;
import java.util.List;

/**
 * User experience data class
 * */
public class Experience {
    /**
     * Field of experience
     * */
    private String field;
    /**
     * Project content string list
     * */
    private List<String> projects = new ArrayList<>();

    public Experience(String field, String detail) {
        this.field = field;
        projects.add(detail);
    }

    /**
     * Get the field of experience
     * */
    public String getField() {
        return field;
    }

    /**
     * Get the list of projects that user experienced
     * */
    public List<String> getProjects() {
        return projects;
    }

    /**
     * Add the project content
     * */
    public void addProject(String detail) {
        projects.add(detail);
    }
}
