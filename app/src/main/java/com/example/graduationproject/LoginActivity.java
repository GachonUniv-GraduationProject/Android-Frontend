package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private CheckBox autoLoginCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText idEditText = findViewById(R.id.signup_email_edittext);
        EditText passwordEditText = findViewById(R.id.input_password);
        autoLoginCheckbox = findViewById(R.id.auto_login_checkbox);

        checkAutoLogin();

        Button loginBtn = findViewById(R.id.login_btn2);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idEditText.length() == 0) {
                    showToast("아이디를 입력하세요.");
                }
                else if(passwordEditText.length() == 0) {
                    showToast("비밀번호를 입력하세요.");
                }
                else {
                    UserData userData = new UserData(idEditText.getText().toString(), passwordEditText.getText().toString());
                    requestLogin(userData);
                }
            }
        });
    }

    private void requestLogin(UserData userData) {
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<LoginData> login = service.login(userData);
        login.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if(response.isSuccessful()) {
                    LoginData.currentLoginData = response.body();
                    String msg = "<Login> onResponse: Success\n";
                    msg += LoginData.currentLoginData.getUser().getUsername() + " / " + LoginData.currentLoginData.getToken();
                    Log.d("Server Test", msg);
                    showToast("Login Success!");
                    loginSuccess(userData);
                }
                else {
                    Log.d("Server Test", "[Code] " + response.code());
                    if(response.code() == 400) {
                        showToast("Invalid ID / Password!");
                        SharedPreferencesManager.clearPreferences(getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                Log.d("Server Test", "<Login> onFailure: " + t.getMessage());
                SharedPreferencesManager.clearPreferences(getApplicationContext());
            }
        });
    }

    private void checkAutoLogin() {
        Map<String, String> loginInfo = SharedPreferencesManager.getLoginInfo(this);
        if(loginInfo != null) {
            String id = loginInfo.get("id");
            String pwd = loginInfo.get("password");
            UserData user = new UserData(id, pwd);
            requestLogin(user);
        }
    }
    private void loginSuccess(UserData userData) {
        if(autoLoginCheckbox.isChecked()) {
            SharedPreferencesManager.setLoginInfo(this, userData.getUsername(), userData.getPassword());
        }
        else
            SharedPreferencesManager.clearPreferences(this);

        Intent nextActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(nextActivity);
    }

    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }
}


