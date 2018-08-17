package com.fih.idx.deriklibrary.utils;

import android.os.Handler;
import android.os.Message;

public class Log {

    private static final String INFO = "INFO";

    private static final String ERROR = "ERROR";

    private static final String DEBUG = "DEBUG";

    private static final String VERBOSE = "VERBOSE";

    private static final String WARN = "WARN";

    private static boolean ENABLE = true;

    private static Handler handler;

    public static void i(String tag, String message) {
        log(INFO, tag, message);
    }

    public static void e(String tag, String message) {
        log(ERROR, tag, message);
    }

    public static void d(String tag, String message) {
        log(DEBUG, tag, message);
    }

    public static void v(String tag, String message) {
        log(VERBOSE, tag, message);
    }

    public static void w(String tag, String message) {
        log(WARN, tag, message);
    }

    public static void setHandler(Handler handler) {
        Log.handler = handler;
    }

    private static void log(String level, String tag, String message) {
        if (!ENABLE) {
            return;
        }

        switch (level) {
            case INFO:
                android.util.Log.i(tag, message);
                break;
            case ERROR:
                android.util.Log.e(tag, message);
                break;
            case DEBUG:
                android.util.Log.d(tag, message);
                break;
            case VERBOSE:
                android.util.Log.v(tag, message);
                break;
            case WARN:
                android.util.Log.w(tag, message);
                break;
            default:
                break;
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
