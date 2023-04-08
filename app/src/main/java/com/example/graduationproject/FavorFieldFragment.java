package com.example.graduationproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
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
 * Fragment indicating preferred field
 * */
public class FavorFieldFragment extends Fragment {

    /**
     * Container for preference field
     * */
    private LinearLayout favorFieldContainer;
    /**
     * Floating action button for add preference field
     * */
    private FloatingActionButton addFavorFab;

    /**
     * List of preference field
     * */
    private List<String> fields;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favor_field, container, false);

        initUI(view);

        updateFields();

        return view;
    }

    /**
     * Initialize UI by connecting views from xml
     * */
    private void initUI(View view)
    {
        // Load container from xml
        favorFieldContainer = view.findViewById(R.id.favor_field_container);

        // Set the floating action button for
        addFavorFab = view.findViewById(R.id.favor_add_fab);
        addFavorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getContext());
                // Alert the dialog that receives the input of the preferred area to be added
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("추가할 선호 분야를 입력해주세요.");
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
    }

    /**
     * Apply preferred field data.
     * */
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    /**
     * Update the preferred field to UI.
     * */
    public void updateFields() {
        for(int i = 0; i < fields.size(); i++) {
            FavorFieldLayout favorFieldLayout = new FavorFieldLayout(getContext());
            favorFieldLayout.setFieldText(fields.get(i));
            favorFieldContainer.addView(favorFieldLayout);
        }
    }


}