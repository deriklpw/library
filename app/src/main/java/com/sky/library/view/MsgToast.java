package com.sky.library.view;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast显示工具，每次使用同一个Toast实例显示信息
 * Created by derik on 16-12-14.
 */

public final class MsgToast {
    private static Toast mToast = null;

    public static void show(Context ctx, String msg) {
        mToast = (mToast == null) ? Toast.makeText(ctx, msg, Toast.LENGTH_LONG) : mToast;
        mToast.setText(msg);
        mToast.show();
    }

    public static void destroy() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

}
