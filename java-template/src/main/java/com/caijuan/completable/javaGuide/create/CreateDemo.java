package com.caijuan.completable.javaGuide.create;

import com.caijuan.completable.SmallTool;
import org.junit.Assert;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class CreateDemo {

    public static String completeStr(String str) {
        SmallTool.printTimeAndThread("content : " + str);
        return "string :" + str;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // ------------ 连接两个计算任务
        CompletableFuture<String> future = CompletableFuture.completedFuture("hello!")
                .thenApply(s -> s + "world!");
        assertEquals("hello!world!", future.get());
        // 这次调用将被忽略!
        future.thenApply(s -> s + "nice!");
        assertEquals("hello!world!", future.get());

        // 流式调用
        future = CompletableFuture.completedFuture("hello!")
                .thenApply(s -> s + "world!").thenApply(s -> s + "nice!");
        assertEquals("hello!world!nice!", future.get());

//        demo01();
//        demo02();
//        demo03();
    }

    private static void demo03() throws InterruptedException, ExecutionException {
        // 封装任务
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            SmallTool.sleepMillis(2000);
            System.out.println("hello!");
        });

        future.get();// 输出 "hello!"

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "hello!");
        Assert.assertEquals("hello!", future2.get());
    }

    // CompletableFuture.completedFuture(result) : 结果存到 completedFuture 中
    private static void demo02() throws InterruptedException, ExecutionException {
        // 已经知道计算的结果的话，可以使用静态方法 completedFuture() 来创建 CompletableFuture
        CompletableFuture<String> resultFuture = CompletableFuture.completedFuture(completeStr("hello"));
        SmallTool.printTimeAndThread("isDone1 => " + resultFuture.isDone());

        // 调用 get() 方法的线程会阻塞直到 CompletableFuture 完成运算
        resultFuture.complete(completeStr("hello 1"));
        SmallTool.printTimeAndThread("isDone2 => " + resultFuture.get());
        SmallTool.printTimeAndThread("isDone2 => " + resultFuture.isDone());

        // complete() 方法只能调用一次，后续调用将被忽略
        resultFuture.complete(completeStr("hello 2"));
        SmallTool.printTimeAndThread("isDone3 => " + resultFuture.isDone());
        SmallTool.printTimeAndThread("isDone2 => " + resultFuture.get());
    }

    // new CompletableFuture & get/isDone
    private static void demo01() throws InterruptedException, ExecutionException {
        CompletableFuture<String> resultFuture = new CompletableFuture<>();
        SmallTool.printTimeAndThread("isDone1 => " + resultFuture.isDone());

        // 调用 get() 方法的线程会阻塞直到 CompletableFuture 完成运算
        resultFuture.complete(completeStr("hello 1"));
        SmallTool.printTimeAndThread("isDone2 => " + resultFuture.get());
        SmallTool.printTimeAndThread("isDone2 => " + resultFuture.isDone());

        // complete() 方法只能调用一次，后续调用将被忽略
        resultFuture.complete(completeStr("hello 2"));
        SmallTool.printTimeAndThread("isDone3 => " + resultFuture.isDone());
        SmallTool.printTimeAndThread("isDone2 => " + resultFuture.get());
    }
}
