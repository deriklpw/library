package com.sky.library.singleton;

/**
 * 双重检测
 */
public class SingletonSync {
    private static volatile SingletonSync INSTANCE = null;
    private int age = 0;

    private SingletonSync() {

    }

    public static SingletonSync getInstance() {
        if (INSTANCE == null) {
            synchronized (SingletonSync.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SingletonSync();
                }
            }
        }
        return INSTANCE;
    }
}
