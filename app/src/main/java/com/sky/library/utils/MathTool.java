package com.sky.library.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by derik on 18-1-19.
 */

public class MathTool {
    public static int randomValue(int min, int max) {
        return (int) (min + Math.random() * (max - min));
    }

    public static int randomValue(int value) {
        return (int) (Math.random() * value);
    }

    public static int[] randomValues(int range, int number) {

        if (range <= 0 || number <= 0) {
            throw new IllegalArgumentException("参数错误，范围或个数不能小于0");
        }
        number = range < number ? range : number;
        List<Integer> bbr = new ArrayList<>();
        int ids[] = new int[number];

        for (int i = 0; i < range; i++) {
            bbr.add(i);
        }

        while (bbr.size() > 0) {
            int ran = randomValue(bbr.size());
            if (number > 0) {
                ids[number - 1] = bbr.get(ran);
                number--;
            }
            bbr.remove(ran);
        }
        return ids;

    }

}
