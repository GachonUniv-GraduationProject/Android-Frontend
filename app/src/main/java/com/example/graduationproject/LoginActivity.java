package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.trend.TrendActivity;

public class LoginActivity extends AppCompatActivity {

    Intent moveToMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText enterEmail = findViewById(R.id.input_email);
        EditText enterPassword = findViewById(R.id.input_password);

        Button loginBtn = findViewById(R.id.login_btn2);
        moveToMain = new Intent(this, StartActivity.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 테스트용 */
                Intent testIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(testIntent);

                // TODO: 나중에 로그인 기능 구현시 복구
                /*if (enterEmail.length() == 0) {
                    StartToast("이메일을 입력하세요.");
                }*/
            }
        });
    }
        public void StartToast(String msg){
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 200);
            toast.show();
        }
    }


