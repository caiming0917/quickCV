package com.caijuan.completable.start;

public class CreateThread {
    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(Thread.currentThread() + " : hello world~");
        }).start();
        System.out.println(Thread.currentThread() + " : main thread~");
    }
}
