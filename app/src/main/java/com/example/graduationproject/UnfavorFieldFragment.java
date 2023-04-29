package com.example.graduationproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Fragments representing non-preferred areas
 * */
public class UnfavorFieldFragment extends Fragment {

    /**
     * Container for unfavor field info
     * */
    private LinearLayout unfavorFieldContainer;
    /**
     * Add field floating action button
     * */
    private FloatingActionButton addUnfavorFab;

    /**
     * List of non-preferred fields
     * */
    private List<String> fields;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate this layout
        View view = inflater.inflate(R.layout.fragment_unfavor_field, container, false);

        // Load container from xml
        unfavorFieldContainer = view.findViewById(R.id.unfavor_field_container);

        // Set floating action button listener
        addUnfavorFab = view.findViewById(R.id.unfavor_add_fab);
        addUnfavorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Activate incoming dialogs for fields to add
                final EditText editText = new EditText(getContext());
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("추가할 비선호 분야를 입력해주세요.");
                dialog.setView(editText);
                dialog.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputField = editText.getText().toString();
                        Toast.makeText(getContext(), "추가할 분야: " + inputField, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
            }
        });
        //
        updateFields();

        return view;
    }

    /**
     * Set field list
     * */
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    /**
     * Apply non-preferred field to UI
     * */
    public void updateFields() {
        for(int i = 0; i < fields.size(); i++) {
            FavorFieldLayout favorFieldLayout = new FavorFieldLayout(getContext());
            favorFieldLayout.setFieldText(fields.get(i));
            unfavorFieldContainer.addView(favorFieldLayout);
        }
    }
}