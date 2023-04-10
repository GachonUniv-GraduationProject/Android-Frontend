package com.example.graduationproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for login
 * */
public class LoginActivity extends AppCompatActivity {

    /**
     * EditText to enter the ID
     * */
    private EditText idEditText;
    /**
     * EditText to enter the password
     * */
    private EditText passwordEditText;
    /**
     * CheckBox whether the user needs auto login
     * */
    private CheckBox autoLoginCheckbox;

    /**
     * Loading dialog during login process
     * */
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Load views from xml
        idEditText = findViewById(R.id.signup_email_edittext);
        passwordEditText = findViewById(R.id.input_password);
        autoLoginCheckbox = findViewById(R.id.auto_login_checkbox);

        // Check if there's auto login data
        checkAutoLogin();

        // Set the login button listener
        Button loginBtn = findViewById(R.id.login_btn2);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if ID is empty
                if (idEditText.length() == 0) {
                    showToast("아이디를 입력하세요.");
                }
                // Check if password is empty
                else if(passwordEditText.length() == 0) {
                    showToast("비밀번호를 입력하세요.");
                }
                // Try login
                else {
                    UserData userData = new UserData(idEditText.getText().toString(), passwordEditText.getText().toString());
                    requestLogin(userData);
                }
            }
        });
    }

    /**
     * Event Listener to deactivate the keyboard when touching an area other than the keyboard
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if(focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int)ev.getX(), y = (int)ev.getY();
            if(!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Request login to server
     * */
    private void requestLogin(UserData userData) {
        // Activate loading dialog during the login process
        loadingDialog = new LoadingDialog(this);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        // Request login to server
        RetrofitService service = RetrofitClient.getRetrofitService();
        Call<LoginData> login = service.login(userData);
        login.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                // If successful, read the required data, call login success, and release the loading dialog
                if(response.isSuccessful()) {
                    LoginData.currentLoginData = response.body();
                    String msg = "<Login> onResponse: Success\n";
                    msg += LoginData.currentLoginData.getUser().getUsername() + " / " + LoginData.currentLoginData.getToken();
                    Log.d("Server Test", msg);
                    showToast("Login Success!");
                    loginSuccess(userData);
                    loadingDialog.dismiss();
                }
                // If failed, remove the auto login data, show message, and release the loading dialog
                else {
                    Log.d("Server Test", "[Code] " + response.code());
                    SharedPreferencesManager.clearPreferences(getApplicationContext());
                    if(response.code() == 400) {
                        showToast("Invalid ID / Password!");
                    }
                    else {
                        showToast("Login Failed.");
                    }
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                // If there's any problem, remove the auto login data, and show the log
                showToast("Login Failed.");
                Log.d("Server Test", "<Login> onFailure: " + t.getMessage());
                SharedPreferencesManager.clearPreferences(getApplicationContext());
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * Check if there's any auto login data
     * */
    private void checkAutoLogin() {
        // Read auto login data
        Map<String, String> loginInfo = SharedPreferencesManager.getLoginInfo(this);
        // If there's login data, try login
        if(loginInfo != null) {
            // Get the ID and password
            String id = loginInfo.get("id");
            String pwd = loginInfo.get("password");
            UserData user = new UserData(id, pwd);
            // Try login
            idEditText.setText(id);
            passwordEditText.setText(pwd);
            autoLoginCheckbox.setChecked(true);
            requestLogin(user);
        }
    }
    /**
     * Successful login starts the next activity
     * */
    private void loginSuccess(UserData userData) {
        // If auto login is needed, save the login information
        if(autoLoginCheckbox.isChecked()) {
            SharedPreferencesManager.setLoginInfo(this, userData.getUsername(), userData.getPassword());
        }
        // Remove the auto login data
        else
            SharedPreferencesManager.clearPreferences(this);

        Intent nextActivity;
        // If the user is individual, start the normal user's activity
        if(LoginData.currentLoginData.isIndividual())
            nextActivity = new Intent(getApplicationContext(), MainActivity.class);
        // If the user is business, start the business user's activity
        else
            nextActivity = new Intent(getApplicationContext(), BusinessScouterActivity.class);
        startActivity(nextActivity);
    }

    /**
     * Show the toast message
     * */
    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}


