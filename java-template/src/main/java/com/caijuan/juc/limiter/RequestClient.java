package com.caijuan.juc.limiter;

import com.caijuan.juc.threadpool.ThreadPoolFactoryUtils;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author cai juan
 */
public class RequestClient {


    private final static ExecutorService executor = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("limit");


    private static void sendRequest() {
        Request request = new Request();
        request.setHandleTime(LocalTime.now());
    }


    private static String testSendRequest(Limiter limiter) {
        int total = 1000 * 1000 * 10;
        int count = 0;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < total; i++) {
            Request request = new Request();
            if (limiter.tryAcquire(request)) {
                limiter.handleRequest(request);
                count++;
            }
        }
        result.append("ThreadName：" + Thread.currentThread().getName());
        result.append("，请求总数：total=" + total);
        result.append("，通过请求数：count=" + count);
        return result.append("，限流请求数：limitNum=" + (total - count)).toString();
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String>[] completableFutures = new CompletableFuture[10];
        Limiter limiter = new CounterLimiter(1000, 1000);
        for (int i = 0; i < completableFutures.length; i++) {
            completableFutures[i] = CompletableFuture.supplyAsync(() -> {
                return testSendRequest(limiter);
            }, executor);
        }
        System.out.println("等待所有请求完成");
        CompletableFuture.allOf(completableFutures).join();
        for (int i = 0; i < completableFutures.length; i++) {
            System.out.println(completableFutures[i].get());
        }
        ThreadPoolFactoryUtils.printThreadPoolStatus((ThreadPoolExecutor) executor);
        ThreadPoolFactoryUtils.shutDownAllThreadPool();
        if (limiter instanceof CounterLimiter) {
            ((CounterLimiter) limiter).shutdown();
        }
    }
}
