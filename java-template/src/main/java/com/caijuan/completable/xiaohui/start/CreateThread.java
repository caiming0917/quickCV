package com.caijuan.completable.xiaohui.start;

public class CreateThread {
    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(Thread.currentThread() + " : hello world~");
        }).start();
        System.out.println(Thread.currentThread() + " : main thread~");
    }
}
