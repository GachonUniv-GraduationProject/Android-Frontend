package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignupBusinessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_business);

        EditText nameEditText       = findViewById(R.id.signup_company_name_edittext);
        EditText idEditText         = findViewById(R.id.signup_id_edittext);
        EditText emailEditText      = findViewById(R.id.signup_email_edittext);
        EditText passwordEditText   = findViewById(R.id.signup_password_edittext);

        Button signupBtn = findViewById(R.id.signup_button);
        Intent moveToLogin = new Intent(this,LoginActivity.class);

        signupBtn.setOnClickListener(v->startActivity(moveToLogin));
    }
}
