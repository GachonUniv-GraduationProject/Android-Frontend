package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavorFieldLayout extends LinearLayout {
    private TextView fieldTextview;
    private Button deleteButton;

    public FavorFieldLayout(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_favor_field, this, true);

        fieldTextview = findViewById(R.id.field_text);
        deleteButton = findViewById(R.id.delete_field_button);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setFieldText(String field) {
        fieldTextview.setText(field);
    }
}
