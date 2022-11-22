package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_selection);

        Button personalSignupButton = findViewById(R.id.personal_signup_button);
        Button businessSignupButton = findViewById(R.id.business_signup_button);

        personalSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent personalActivity = new Intent(getApplicationContext(), SignupPersonalActivity.class);
                startActivity(personalActivity);
            }
        });
        businessSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent businessActivity = new Intent(getApplicationContext(), SignupBusinessActivity.class);
                startActivity(businessActivity);
            }
        });
    }
}