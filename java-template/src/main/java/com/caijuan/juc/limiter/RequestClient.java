package com.caijuan.juc.limiter;

import com.caijuan.juc.threadpool.ThreadPoolFactoryUtils;
import com.caijuan.utils.SmallTool;
import lombok.SneakyThrows;

import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cai juan
 */
public class RequestClient {


    private final static ExecutorService executor = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("limit");

    private static AtomicInteger countSum = new AtomicInteger(0);

    private static void sendRequest() {
        Request request = new Request();
        request.setHandleTime(LocalTime.now());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // testWindowLimiter();
        testFunction();
    }

    private static void testWindowLimiter() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            int limit = 20;
            CounterSildeWindowLimiter counterSildeWindowLimiter = new CounterSildeWindowLimiter(1000, limit, 10);

            Thread.sleep(3000);
            testWindowLimiter(counterSildeWindowLimiter, limit);

            CounterLimiter counterLimiter = new CounterLimiter(1000, limit);
            testWindowLimiter(counterLimiter,limit);

            System.out.println("\n\n");
        }
    }



    private static String testSendRequest(Limiter limiter) throws InterruptedException {
        int total = 50;
        int passCount = 0;
        int failCount = 0;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < total; i++) {
            Request request = new Request();
            if (limiter.tryAcquire(request)) {
                limiter.handleRequest(request);
                passCount++;
            }else {
                failCount++;
            }
        }
        Thread.sleep(1000);
        for (int i = 0; i < total; i++) {
            Request request = new Request();
            if (limiter.tryAcquire(request)) {
                limiter.handleRequest(request);
                passCount++;
            }else {
                failCount++;;
            }
        }

        countSum.addAndGet(passCount);
        result.append("ThreadName：" + Thread.currentThread().getName());
        result.append("，请求总数：total=" + total * 2);
        result.append("，通过请求数：passNum=" + passCount);
        result.append("，限流请求数：limitNum=" + failCount);
        SmallTool.info(result.toString());
        return result.toString();
    }

    @SneakyThrows
    private static void testWindowLimiter(WindowLimiter windowLimiter, int limit) {
        // 计数器滑动窗口算法模拟100组间隔30ms的50次请求
        System.out.println("开始模拟100组间隔150ms的50次请求");
        int faliCount = 0;
        for (int j = 0; j < 100; j++) {
            faliCount = testWindowLimiter(windowLimiter, limit, faliCount);
        }
        System.out.println("计数器滑动窗口算法测试结束，100组间隔150ms的50次请求模拟完成，限流失败组数：" + faliCount);
        System.out.println("===========================================================================================");
    }

    private static int testWindowLimiter(WindowLimiter windowLimiter, int limit, int faliCount) throws InterruptedException {
        int count = 0;
        for (int i = 0; i < 50; i++) {
            if (windowLimiter.tryAcquire(new Request())) {
                count++;
            }
        }
        // Thread.sleep(250);
        // 模拟50次请求，看多少能通过
        for (int i = 0; i < 50; i++) {
            if (windowLimiter.tryAcquire(new Request())) {
                count++;
            }
        }
        System.out.println("放过的请求数" + count + ",限流：" + limit);
        if (count > limit) {
            System.out.println("时间窗口内放过的请求超过阈值! 放过的请求数" + count + ",限流：" + limit);
            faliCount++;
        }
        Thread.sleep((int) (Math.random() * 100));
        return faliCount;
    }

    private static void testFunction() throws InterruptedException, ExecutionException {
        CompletableFuture<String>[] completableFutures = new CompletableFuture[10];
        Limiter limiter = new TokenBucketLimiter(100000, 1);
        // Limiter limiter = new LeakyBucketLimiter(1000, 2);
        // Limiter limiter = new CounterLimiter(1000, 20);
        // Limiter limiter = new CounterSildeWindowLimiter(1000, 100, 10);
        for (int i = 0; i < completableFutures.length; i++) {
            completableFutures[i] = CompletableFuture.supplyAsync(() -> {
                try {
                    return testSendRequest(limiter);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, executor);
        }
        System.out.println("等待所有请求完成");
        CompletableFuture.allOf(completableFutures).join();
        for (int i = 0; i < completableFutures.length; i++) {
            System.out.println(completableFutures[i].get());
        }
        System.out.println("countSum =>" + countSum.get());
        // ThreadPoolFactoryUtils.printThreadPoolStatus((ThreadPoolExecutor) executor);
        ThreadPoolFactoryUtils.shutDownAllThreadPool();
        if (limiter instanceof CounterLimiter) {
            ((CounterLimiter) limiter).shutdown();
        }
    }
}
