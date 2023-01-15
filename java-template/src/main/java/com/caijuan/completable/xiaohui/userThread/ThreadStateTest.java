package com.caijuan.completable.xiaohui.userThread;

import java.util.concurrent.TimeUnit;

public class ThreadStateTest extends Thread{

    public static void main(String[] args) throws InterruptedException {
        ThreadStateTest stateTest = new ThreadStateTest();
        System.out.println(stateTest.getState());

        stateTest.start();
        System.out.println(stateTest.getState());

        TimeUnit.SECONDS.sleep(1);

        System.out.println(stateTest.getState());
    }

    @Override
    public void run() {
//        super.run();
        System.out.println("线程" + Thread.currentThread());
    }
}
