package com.example.graduationproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.graduationproject.trend.PieChartActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById((R.id.login_btn));
        Button joinBtn = findViewById(R.id.signup_btn);

        Intent moveToLogin=new Intent(this,LoginActivity.class);
        Intent moveToJoin= new Intent(this,JoinActivity.class);



        loginBtn.setOnClickListener(v->startActivity(moveToLogin));
        joinBtn.setOnClickListener(v->startActivity(moveToJoin));


    }
}