package com.example.graduationproject;

import java.util.List;

/**
 * Interface for survey completion processing callback
 * */
public interface SurveyListener {
    /**
     * Complete with single content
     * */
    public void onComplete(String content);

    /**
     * Complete with multiple contents
     * */
    public void onComplete(List<String> contents);
}
