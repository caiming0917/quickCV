package com.caijuan.juc.forkjoin.work;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.Future;

/**
 * @author cai juan
 * @date 2023/3/1 16:32
 */
public class Main {
    public static void main(String[] args){
        final ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("test-thread" + worker.getPoolIndex());
            return worker;
        };
        //创建分治任务线程池，可以追踪到线程名称。线程池一定要作为公共的进行管理，并且要限制初始化的大小和队列的长度，否则会一直吃资源。
        ForkJoinPool forkJoinPool = new ForkJoinPool(16, factory, null, false);
        int count = 10_000_000;
        List<Integer> datas = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            datas.add(i);
        }
        TestTask task = new TestTask(datas);
        Instant start = Instant.now();
        // 执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get());
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toMillis();
            System.out.println("代码执行时间：" + duration + "毫秒");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
