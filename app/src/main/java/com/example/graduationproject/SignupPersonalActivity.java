package com.example.graduationproject;

import android.content.Intent;
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

        Button signupBtn=findViewById(R.id.signup_button);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String name = nameEditText.getText().toString();
                String id = idEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                UserData userData = new UserData(name, id, password, email);

                RetrofitService service = RetrofitClient.getRetrofitService();
                Call<LoginData> signup = service.signup(userData);
                signup.enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                        if(response.isSuccessful()) {
                            LoginData.currentLoginData = response.body();
                            String msg = "<Sign up-Personal> Success\n";
                            msg += LoginData.currentLoginData.getUser().getUsername() + " / " + LoginData.currentLoginData.getToken();
                            Log.d("Server Test", msg);
                            showToast("Sign up success!");

                            Intent nextActivity = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(nextActivity);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginData> call, Throwable t) {
                        Toast.makeText(SignupPersonalActivity.this, "Sign up Failed.", Toast.LENGTH_SHORT).show();
                        Log.d("Server Test", "<Sign up-Personal> onFailure: " + t.getMessage());
                    }
                });*/
                Intent surveyActivity = new Intent(getApplicationContext(), PersonalSurveyActivity.class);
                startActivity(surveyActivity);
            }
        });

    }
    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }
}
