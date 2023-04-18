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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Signup activity for individual member
 * */
public class SignupPersonalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_personal);

        //Load up the signup UI component
        EditText nameEditText       = findViewById(R.id.signup_username_edittext);
        EditText idEditText         = findViewById(R.id.signup_id_edittext);
        EditText emailEditText      = findViewById(R.id.signup_email_edittext);
        EditText passwordEditText   = findViewById(R.id.signup_password_edittext);
        EditText phoneEditText      = findViewById(R.id.signup_phone_edittext);

        // Set the signup button click listener
        Button signupBtn=findViewById(R.id.signup_button);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Object the registration information entered by the user
                String name = nameEditText.getText().toString();
                String id = idEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                UserData userData = new UserData(id, password, email, phone, name);

                // Send it to the server
                RetrofitService service = RetrofitClient.getRetrofitService();
                Call<UserData> signup = service.signup(userData, "True");
                signup.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        if(response.isSuccessful()) {
                            String msg = "<Sign up-Personal> Success\n";
                            Log.d("Server Test", msg);
                            showToast("Sign up success!");

                            // Parse the signup process result
                            String signupJson = new Gson().toJson(response.body());
                            JsonParser parser = new JsonParser();
                            JsonObject rootObj = (JsonObject) parser.parse(signupJson);

                            // Get the user ID
                            int userId = rootObj.get("id").getAsInt();

                            // Start survey activity with id
                            Intent surveyActivity = new Intent(getApplicationContext(), PersonalSurveyActivity.class);
                            surveyActivity.putExtra("newUserId", userId);
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
    /**
     * Show toast message to user with msg(message)
     * */
    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
