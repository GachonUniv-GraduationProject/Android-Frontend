package com.example.graduationproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

public class FavorFieldFragment extends Fragment {

    private LinearLayout favorFieldContainer;

    private FloatingActionButton addFavorFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favor_field, container, false);

        favorFieldContainer = view.findViewById(R.id.favor_field_container);

        String[] fields = {"분야 1", "분야 2"};
        FavorFieldLayout favorFieldLayout = new FavorFieldLayout(getContext());
        favorFieldLayout.setFieldText(fields[0]);
        favorFieldContainer.addView(favorFieldLayout);

        FavorFieldLayout favorFieldLayout1 = new FavorFieldLayout(getContext());
        favorFieldLayout1.setFieldText(fields[1]);
        favorFieldContainer.addView(favorFieldLayout1);

        addFavorFab = view.findViewById(R.id.favor_add_fab);
        addFavorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getContext());
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

        return view;
    }
}