package com.sky.library.utils;

public class LogUtil {

    private static final String INFO = "INFO";

    private static final String ERROR = "ERROR";

    private static final String DEBUG = "DEBUG";

    private static final String VERBOSE = "VERBOSE";

    private static final String WARN = "WARN";

    private static boolean ENABLE = true;

    public static void i(String tag, String message) {
        log(INFO, tag, message);
    }

    public static void i(String message) {
        log(INFO, buildTAG(), message);
    }

    public static void e(String tag, String message) {
        log(ERROR, tag, message);
    }

    public static void e(String message) {
        log(ERROR, buildTAG(), message);
    }

    public static void d(String tag, String message) {
        log(DEBUG, tag, message);
    }

    public static void d(String message) {
        log(DEBUG, buildTAG(), message);
    }

    public static void v(String tag, String message) {
        log(VERBOSE, tag, message);
    }

    public static void v(String message) {
        log(VERBOSE, buildTAG(), message);
    }

    public static void w(String tag, String message) {
        log(WARN, tag, message);
    }

    public static void w(String message) {
        log(WARN, buildTAG(), message);
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
    }

    public static void setEnable(boolean isEnable) {
        ENABLE = isEnable;
    }

    private static String buildTAG() {
        StringBuilder buffer = new StringBuilder();

        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        buffer.append("【");
            /*buffer.append(Thread.currentThread().getName());
            buffer.append("】【");*/
        buffer.append(stackTraceElement.getFileName());
        buffer.append("】【");
        buffer.append(stackTraceElement.getLineNumber());
        buffer.append("row】【");
        buffer.append(stackTraceElement.getMethodName());
        buffer.append("()】 ");
        return buffer.toString();
    }
}
