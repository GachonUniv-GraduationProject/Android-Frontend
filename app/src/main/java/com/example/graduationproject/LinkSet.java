package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Layout showing title and hyperlink button of training material
 * */
public class LinkSet extends LinearLayout {
    /**
     * Context of this layout
     * */
    private Context context;
    /**
     * Title of the training material
     * */
    private TextView titleTextview;
    /**
     * Hyperlink of the training material
     * */
    private Button refLinkButton;

    public LinkSet(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_link_set, this, true);

        this.context = context;

        // Load views from xml
        titleTextview = findViewById(R.id.link_title_text);
        refLinkButton = findViewById(R.id.reference_link_button);
    }

    /**
     * Set the title of this link
     * */
    public void setTitle(String title) {
        titleTextview.setText(title);
    }

    /**
     * Set the URL of this link
     * */
    public void setReferenceLink(String url) {
        refLinkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent refIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(refIntent);
            }
        });
    }
}
