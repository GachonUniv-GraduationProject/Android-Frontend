package com.example.graduationproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Project Experience Fragments to be shown in User Information Activity
 * */
public class ProjectExperienceFragment extends Fragment {

    /**
     * Container for experience list
     * */
    private LinearLayout projectExperienceContainer;
    /**
     * List of experience data
     * */
    private List<Experience> experienceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_experience, container, false);

        // Load container from xml
        projectExperienceContainer = view.findViewById(R.id.project_experience_container);

        // If experience list is available, update the experience list
        if(experienceList != null)
            updateExperiences();

        return view;
    }

    /**
     * Set list of experience data
     * */
    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    /**
     * Update experience data to the view
     * */
    public void updateExperiences() {
        // Create experience layout, set the data, and add to container for each experience
        for(int i = 0; i < experienceList.size(); i++) {
            ProjectExperienceLayout experienceLayout = new ProjectExperienceLayout(getContext());
            experienceLayout.setField(experienceList.get(i).getField());
            int projectSize = experienceList.get(i).getProjects().size();
            experienceLayout.addProject(experienceList.get(i).getProjects().toArray(new String[projectSize]));
            projectExperienceContainer.addView(experienceLayout);
        }
    }
}