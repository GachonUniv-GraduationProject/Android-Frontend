package com.example.graduationproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class RoadmapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadmap, container, false);

        LinearLayout roadmapContainer = view.findViewById(R.id.roadmap_container);
        RoadmapDrawer drawer = new RoadmapDrawer(getActivity());
        roadmapContainer.addView(drawer);

        return view;
    }
}