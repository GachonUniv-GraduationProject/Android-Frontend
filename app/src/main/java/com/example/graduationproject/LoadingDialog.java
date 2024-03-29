package com.example.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

/**
 * Dialog to indicate loading
 * */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context);

        // Remove the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.loading_progress);
    }
}
