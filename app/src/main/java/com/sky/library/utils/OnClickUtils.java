package com.sky.library.utils;


/**
 * description ：Double-click
 * author : Derik.Wu
 * email : Derik.Wu@waclighting.com.cn
 * date : 2020/4/20
 */
public class OnClickUtils {

    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= 500) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
