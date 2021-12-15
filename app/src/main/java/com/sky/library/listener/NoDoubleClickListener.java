package com.sky.library.listener;

import android.view.View;

/**
 * Created by derik on 17-3-1.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentClickTime = System.currentTimeMillis();

        if ((currentClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentClickTime;
            OnNoDoubleClick(v);
        }

    }

    public abstract void OnNoDoubleClick(View v);
}
