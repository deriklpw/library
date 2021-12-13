package com.sky.library.singleton;

/**
 * 饿汉模式
 */
public class Singleton1 {
    private static final Singleton1 INSTANCE = new Singleton1();
    private int age = 0;

    private Singleton1() {

    }

    public static Singleton1 getInstance() {
        return INSTANCE;
    }
}
