package com.app_neighbrsnook.utils;

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {
    public static int getRandomColor() {
        int[] colors = {
                Color.parseColor("#e1c8ea"),
                Color.parseColor("#99c8ec"),
                Color.parseColor("#c0dfd0"),
                Color.parseColor("#9ec4c5"),
                Color.parseColor("#f6b8c1"),
                Color.parseColor("#f2c2c2"),
                Color.parseColor("#ede073")
               };
        Random random = new Random();
        int randomIndex = random.nextInt(colors.length);
        return colors[randomIndex];
    }
}
