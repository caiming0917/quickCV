package com.caijuan.utils;

import java.util.StringJoiner;

public class SmallTool {
    public static void sleepMillis(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void info(String content){
        String result = new StringJoiner("-")
                .add("" + Thread.currentThread().getName())
                .add("" + Thread.currentThread().getId())
                .add("" + System.currentTimeMillis())
                .add("info : " + content).toString();
        System.out.println(result);
    }
}
