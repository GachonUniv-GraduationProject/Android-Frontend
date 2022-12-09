package com.example.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkDialog extends Dialog {
    private Context context;
    private LinkSet[] linkSets;
    private LinearLayout linkSetContainer;
    private String skillName;

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

    }

    public LinkDialog(Context context) {
        super(context);

        this.context = context;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public void loadUrls() {
        String field = LoginData.currentLoginData.getField();
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<Object> getReferenceLink = service.getReferenceLink(skillName, field);
        getReferenceLink.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    String json = new Gson().toJson(response.body());
                    setReferenceData(json);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setReferenceData(String json) {
        JsonParser parser = new JsonParser();
        JsonObject rootObj = (JsonObject) parser.parse(json);
        JsonArray refArr = (JsonArray) rootObj.get("urls");

        String[] titles = new String[refArr.size()];
        String[] urls = new String[refArr.size()];

        for(int i = 0; i < refArr.size(); i++) {
            JsonObject elemObj = refArr.get(i).getAsJsonObject();
            titles[i] = elemObj.get("name").getAsString();
            urls[i] = elemObj.get("link").getAsString();
        }

        setLinkSets(titles, urls);
    }

    public void setLinkSets(String[] titles, String[] urls) {
        linkSets = new LinkSet[titles.length];
        for(int i = 0; i < linkSets.length; i++) {
            linkSets[i] = new LinkSet(context);
            linkSets[i].setTitle(titles[i]);
            linkSets[i].setReferenceLink(urls[i]);
            linkSetContainer.addView(linkSets[i]);
        }
    }
}
