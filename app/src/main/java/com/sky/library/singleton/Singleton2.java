package com.sky.library.singleton;

import android.content.Context;

/**
 * 懒汉模式，非线程安全
 */
public class Singleton2 {
    private static Singleton2 INSTANCE = null;
    private int age = 0;

    private Singleton2(Context context) {

    }

    public static Singleton2 getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Singleton2(context);
        }
        return INSTANCE;
    }
}
