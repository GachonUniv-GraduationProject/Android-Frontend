package com.example.graduationproject;

import android.app.Activity;
import android.widget.Toast;

public class BackKeyHandler {

    private long backKeyPressedTime = 0;    // Time in millisecond when back key is pressed
    private Activity activity;              // Activities to use the Exit function by pressing the Back key twice
    private Toast toast;                    // Toast message that shows back key message

    public BackKeyHandler(Activity activity) {
        this.activity = activity;
    }

    // Show Press the Back key twice to exit message
    private void showGuide() {
        toast = Toast.makeText(activity, "'뒤로'버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    // When back key is pressed
    public void onBackPressed() {
        // If latest backKey pressed time is not within 2 seconds, update the pressed time and show guide
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        // If latest backKey pressed time is within 2 seconds, finish the activity
        else  {
            StartActivity.startActivity.finish();
            activity.finish();
            toast.cancel();
        }
    }
}
