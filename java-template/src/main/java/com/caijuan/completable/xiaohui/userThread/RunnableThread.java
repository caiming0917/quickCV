package com.caijuan.completable.xiaohui.userThread;

public class RunnableThread{

    public static void main(String[] args) {

        new Thread(()->{
            System.out.println(Thread.currentThread());
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getPriority());
            System.out.println(Thread.currentThread().getId());
            System.out.println(Thread.currentThread().getThreadGroup().getName());
        }).start();

    }
}

