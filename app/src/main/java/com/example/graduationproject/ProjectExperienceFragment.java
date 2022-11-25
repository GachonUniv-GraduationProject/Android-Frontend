package com.example.graduationproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ProjectExperienceFragment extends Fragment {

    private LinearLayout projectExperienceContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_experience, container, false);

        projectExperienceContainer = view.findViewById(R.id.project_experience_container);

        String[] fields = {"분야 1", "분야 2"};
        String[] projects1 = {"프로젝트 1", "프로젝트 2"};
        String[] projects2 = {"프로젝트 1", "프로젝트 3"};

        ProjectExperienceLayout experienceLayout1 = new ProjectExperienceLayout(getContext());
        experienceLayout1.setField(fields[0]);
        experienceLayout1.addProject(projects1);
        projectExperienceContainer.addView(experienceLayout1);

        ProjectExperienceLayout experienceLayout2 = new ProjectExperienceLayout(getContext());
        experienceLayout2.setField(fields[1]);
        experienceLayout2.addProject(projects2);
        projectExperienceContainer.addView(experienceLayout2);

        return view;
    }
}