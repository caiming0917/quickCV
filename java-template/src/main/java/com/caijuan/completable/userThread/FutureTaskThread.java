package com.caijuan.completable.userThread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTaskThread {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        FutureTask<String> futureTask = new FutureTask<String>(()->{
            System.out.println(Thread.currentThread());
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getPriority());
            System.out.println(Thread.currentThread().getId());
            System.out.println(Thread.currentThread().getThreadGroup().getName());
            return "result";
        });
        new Thread(futureTask).start();
        System.out.println(futureTask.get(5, TimeUnit.SECONDS));
    }
}

