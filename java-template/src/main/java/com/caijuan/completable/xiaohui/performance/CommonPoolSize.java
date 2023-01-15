package com.caijuan.completable.xiaohui.performance;

import java.util.concurrent.ForkJoinPool;

public class CommonPoolSize {
    public static void main(String[] args) {
        System.out.println("机器线程数：" + Runtime.getRuntime().availableProcessors());

        System.out.println("查看当前线程数：" + ForkJoinPool.commonPool().getPoolSize());

        // Parallelism : 并行度
        System.out.println("最大线程池线程数：" + ForkJoinPool.getCommonPoolParallelism());
    }
}
