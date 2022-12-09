package com.example.graduationproject;

import java.util.List;

public interface SurveyListener {
    public void onComplete(String content);

    public void onComplete(List<String> contents);
}
