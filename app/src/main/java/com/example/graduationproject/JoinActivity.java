package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        EditText enterName = findViewById(R.id.input_username);
        EditText enterEmail = findViewById(R.id.input_email);
        EditText enterPassword=findViewById(R.id.input_password2);

        Button signupBtn=findViewById(R.id.signup_btn);
        Intent moveToLogin=new Intent(this,LoginActivity.class);

        signupBtn.setOnClickListener(v->startActivity(moveToLogin));




    }
}
