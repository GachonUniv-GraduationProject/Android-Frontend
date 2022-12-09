package com.example.graduationproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    static StartActivity startActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button loginButton = findViewById(R.id.login_button);
        Button signupButton = findViewById(R.id.signup_button);

        Intent moveToLogin = new Intent(this, LoginActivity.class);
        Intent moveToSignup  = new Intent(this, SignUpSelectionActivity.class);

        loginButton.setOnClickListener(v->startActivity(moveToLogin));
        signupButton.setOnClickListener(v->startActivity(moveToSignup));

        Button testButton = findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BusinessScouterActivity.class);
                startActivity(intent);
            }
        });

        startActivity = this;
    }
}