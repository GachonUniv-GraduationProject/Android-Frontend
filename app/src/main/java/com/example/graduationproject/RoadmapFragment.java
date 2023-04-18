package com.example.graduationproject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Fragment of MainActivity to show roadmap
 * */
public class RoadmapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadmap, container, false);

        // Load roadmap container from xml
        LinearLayout roadmapContainer = view.findViewById(R.id.roadmap_container);
        FrameLayout roadmapFrameContainer = view.findViewById(R.id.roadmap_frame_container);
        // Create roadmap drawer and add to the container
        RoadmapDrawer drawer = new RoadmapDrawer(getActivity(), roadmapFrameContainer);
        roadmapContainer.addView(drawer);

        // Set the title of roadmap fragment
        TextView fieldTextView = view.findViewById(R.id.roadmap_field_textview);
        String field = LoginData.currentLoginData.getField();
        fieldTextView.setText(field + "에요.");
        Spannable fieldSpan = (Spannable) fieldTextView.getText();
        fieldSpan.setSpan(new ForegroundColorSpan(Color.BLACK),
                0, field.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        return view;
    }
}