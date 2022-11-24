package com.example.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LinkDialog extends Dialog {
    private Context context;
    private LinkSet[] linkSets;
    private LinearLayout linkSetContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_link_to_reference);

        Button closeButton = findViewById(R.id.close_link_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        linkSetContainer = findViewById(R.id.container_link_set);

        for(int i = 0; i < linkSets.length; i++)
            linkSetContainer.addView(linkSets[i]);
    }

    public LinkDialog(Context context) {
        super(context);

        this.context = context;

    }

    public void setLinkSets(String[] titles, String[] urls) {
        linkSets = new LinkSet[titles.length];
        for(int i = 0; i < linkSets.length; i++) {
            linkSets[i] = new LinkSet(context);
            linkSets[i].setTitle(titles[i]);
            linkSets[i].setReferenceLink(urls[i]);
        }
    }
}
