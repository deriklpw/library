package com.fih.idx.deriklibrary.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.fih.idx.deriklibrary.R;


/**
 * Created by derik on 18-1-13.
 */

public class SpeakDialog {

    private final WindowManager mWindowManager;
    private View mView;

    public SpeakDialog(Context context) {

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        mView = View.inflate(context, R.layout.voice_dialog, null);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.TRANSLUCENT;

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        mWindowManager.addView(mView, params);
    }

    public void showReady() {
        mView.findViewById(R.id.ready_view).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.speaking_view).setVisibility(View.GONE);
    }

    public void showSpeaking() {
        mView.findViewById(R.id.ready_view).setVisibility(View.GONE);
        mView.findViewById(R.id.speaking_view).setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        mWindowManager.removeView(mView);
    }
}
