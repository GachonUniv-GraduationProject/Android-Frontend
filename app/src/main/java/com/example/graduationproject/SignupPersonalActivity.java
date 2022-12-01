package com.example.graduationproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupPersonalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_personal);

        EditText nameEditText       = findViewById(R.id.signup_username_edittext);
        EditText idEditText         = findViewById(R.id.signup_id_edittext);
        EditText emailEditText      = findViewById(R.id.signup_email_edittext);
        EditText passwordEditText   = findViewById(R.id.signup_password_edittext);
        EditText phoneEditText      = findViewById(R.id.signup_phone_edittext);

        Button signupBtn=findViewById(R.id.signup_button);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String id = idEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                UserData userData = new UserData(id, password, email, phone, name);

                RetrofitService service = RetrofitClient.getRetrofitService();
                Call<UserData> signup = service.signup(userData, "True");
                signup.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        if(response.isSuccessful()) {
                            String msg = "<Sign up-Personal> Success\n";
                            Log.d("Server Test", msg);
                            showToast("Sign up success!");

                            Intent surveyActivity = new Intent(getApplicationContext(), PersonalSurveyActivity.class);
                            startActivity(surveyActivity);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Toast.makeText(SignupPersonalActivity.this, "Sign up Failed.", Toast.LENGTH_SHORT).show();
                        Log.d("Server Test", "<Sign up-Personal> onFailure: " + t.getMessage());
                    }
                });
            }
        });

    }
    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
