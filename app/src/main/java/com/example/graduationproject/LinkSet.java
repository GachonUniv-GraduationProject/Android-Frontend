package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LinkSet extends LinearLayout {
    private Context context;
    private TextView titleTextview;
    private Button refLinkButton;
    public LinkSet(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_link_set, this, true);

        this.context = context;

        titleTextview = findViewById(R.id.link_title_text);
        refLinkButton = findViewById(R.id.reference_link_button);
    }

    public void setTitle(String title) {
        titleTextview.setText(title);
    }

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
