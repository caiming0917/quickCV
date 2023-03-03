package com.caijuan.juc.threadpool.demo;

import com.caijuan.juc.threadpool.ThreadPoolFactoryUtils;
import com.caijuan.utils.SmallTool;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @author cai juan
 * @date 2023/2/21 21:45
 * <p>
 * &#064;link("<a href="https://www.jianshu.com/p/135c89001b61">简书：线程池对比</a>")
 * &#064;link("<a href="https://www.cnblogs.com/lixin-link/p/10998058.html#countdownlatch">主线程等待子线程结束</a>")
 */
public class ExecutorTest {

    private static Thread mainThread;

    public static void main(String[] args) {
        ThreadFactory threadFactory = ThreadPoolFactoryUtils.createThreadFactory("TEST", true);
        // 固定线程数 ; core = max = n ; 任务队列无限(LinkedBlockingQueue) ; 线程存活时间无效
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10, threadFactory);
        // testFunctionHelper(fixedThreadPool);

        // 线程数可伸缩 ; 0 - Integer.MAX ; SynchronousQueue ; 线程存活时间60S
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(threadFactory);
        testFunctionHelper(cachedThreadPool);

        ExecutorService workStealingPool = Executors.newWorkStealingPool();

    }

    private static void testFunctionHelper(ExecutorService threadPool) {

        testFunction(threadPool);
        // threadPool.shutdownNow();
        System.out.println("=============== END ===============");
    }


    @SneakyThrows
    private static void testFunction(ExecutorService threadPool) {
        int count = 10_000_000;
        CompletableFuture<Long>[] futures = new CompletableFuture[count];
        for (int i = 0; i < count; i++) {
            futures[i] = CompletableFuture.supplyAsync(() -> {
                Random random = new Random();
                long sum = 0;
                int times = 10_000_000;
                for (long j = 0; j < times; j++) {
                    sum = sum + random.nextInt(10);
                    sum = sum - random.nextInt(10);
                    // SmallTool.info("sum => " + sum);
                }
                // SmallTool.info("sum => " + sum);
                return sum;
            }, threadPool);
        }
        CompletableFuture.allOf(futures).join();
        Arrays.stream(futures).parallel().forEach(future -> {
            try {
                SmallTool.info("sum =>" + future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 使用 CountDownLatch 同步
     */
    private static void testFunctionV2(ExecutorService threadPool) {
        int count = 10_0;
        final CountDownLatch cdl = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            threadPool.execute(() -> {
                Random random = new Random();
                long sum = 0;
                int times = 10_000_000;
                for (long j = 0; j < times; j++) {
                    sum = sum + random.nextInt(10);
                    sum = sum - random.nextInt(10);
                    // SmallTool.info("sum => " + sum);
                }
                SmallTool.info("sum => " + sum);
                // 此方法是CountDownLatch的线程数-1
                cdl.countDown();
            });
        }
        // 线程启动后调用countDownLatch方法
        try {
            cdl.await();// 需要捕获异常，当其中线程数为0时这里才会继续运行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(mainThread);
    }
}
