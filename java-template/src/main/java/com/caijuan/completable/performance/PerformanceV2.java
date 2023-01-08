package com.caijuan.completable.performance;

import com.caijuan.completable.SmallTool;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class PerformanceV2 {
    public static void main(String[] args) {

        SmallTool.printTimeAndThread("小白和朋友来了餐厅");



        int count = 39;
        // -Djava.util.completable.ForkJoinPool.common.parallelism=8
        System.setProperty("java.util.completable.ForkJoinPool.common.parallelism", "19");

        SmallTool.printTimeAndThread("三个人点了 " + count + " 盘菜");



        long start = System.currentTimeMillis();


        CompletableFuture[] futures = IntStream.rangeClosed(1, count)
                .mapToObj(i -> new Dish("菜" + i, 1))
                .map(dish -> CompletableFuture.runAsync(dish::make))
                .toArray(CompletableFuture[]::new);

        // 等待所有任务执行完毕
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        CompletableFuture.allOf(futures).join();
        long needTime = System.currentTimeMillis() - start;

        SmallTool.printTimeAndThread("\n\n做" + count + "道菜耗时：" + needTime + "\n");
    }
}
