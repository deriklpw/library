package com.fih.idx.deriklibrary.utils;

import android.os.Handler;
import android.os.Message;

public class Log {

    private static final String INFO = "INFO";

    private static final String ERROR = "ERROR";

    private static boolean ENABLE = true;

    private static Handler handler;

    public static void i(String tag, String message) {
        log(INFO, tag, message);
    }

    public static void e(String tag, String message) {
        log(ERROR, tag, message);
    }

    public static void setHandler(Handler handler) {
        Log.handler = handler;
    }

    private static void log(String level, String tag, String message) {
        if (!ENABLE) {
            return;
        }
        if (level.equals(INFO)) {
            android.util.Log.i(tag, message);

        } else if (level.equals(ERROR)) {
            android.util.Log.e(tag, message);
        }
        if (handler != null) {
            Message msg = Message.obtain();
            msg.obj = "[" + level + "]" + message + "\n";
            handler.sendMessage(msg);
        }
    }

    public static void setEnable(boolean isEnable) {
        ENABLE = isEnable;
    }
}
