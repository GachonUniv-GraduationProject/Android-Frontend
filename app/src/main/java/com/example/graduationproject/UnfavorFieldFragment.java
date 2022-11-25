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

public class UnfavorFieldFragment extends Fragment {

    private LinearLayout unfavorFieldContainer;

    private FloatingActionButton addUnfavorFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unfavor_field, container, false);

        unfavorFieldContainer = view.findViewById(R.id.unfavor_field_container);

        String[] fields = {"분야 4", "분야 5"};
        FavorFieldLayout favorFieldLayout = new FavorFieldLayout(getContext());
        favorFieldLayout.setFieldText(fields[0]);
        unfavorFieldContainer.addView(favorFieldLayout);

        FavorFieldLayout favorFieldLayout1 = new FavorFieldLayout(getContext());
        favorFieldLayout1.setFieldText(fields[1]);
        unfavorFieldContainer.addView(favorFieldLayout1);

        addUnfavorFab = view.findViewById(R.id.unfavor_add_fab);
        addUnfavorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        return view;
    }
}