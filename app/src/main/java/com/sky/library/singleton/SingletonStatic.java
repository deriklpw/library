package com.sky.library.singleton;

/**
 * 静态内部类
 */
public class SingletonStatic {
    private int age = 0;

    private SingletonStatic() {

    }

    private static class Singleton4Holder {
        private static final SingletonStatic instance = new SingletonStatic();
    }

    public static SingletonStatic getInstance() {
        return Singleton4Holder.instance;
    }
}
