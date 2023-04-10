package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Layout groups to represent fields of preference
 * */
public class FavorFieldLayout extends LinearLayout {
    /**
     * Text view showing preferred field name
     * */
    private TextView fieldTextview;
    /**
     * Button for delete the field
     * */
    private Button deleteButton;

    public FavorFieldLayout(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_favor_field, this, true);

        // Load views from xml
        fieldTextview = findViewById(R.id.field_text);
        deleteButton = findViewById(R.id.delete_field_button);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * Set the content of field textview
     * */
    public void setFieldText(String field) {
        fieldTextview.setText(field);
    }
}
