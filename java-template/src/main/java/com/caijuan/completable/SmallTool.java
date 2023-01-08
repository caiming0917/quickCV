package com.caijuan.completable;

import java.util.StringJoiner;

public class SmallTool {
    public static void sleepMillis(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printTimeAndThread(String content){
        String result = new StringJoiner("\t|\t")
                .add("current time:" + System.currentTimeMillis())
                .add("Thread name:" + Thread.currentThread().getName())
                .add("Thread id:" + Thread.currentThread().getId())
                .add(content).toString();
        System.out.println(result);
    }

    public static void main(String[] args) {
        printTimeAndThread("tag");
    }
}
