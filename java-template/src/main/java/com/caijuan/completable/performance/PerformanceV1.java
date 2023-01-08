package com.caijuan.completable.performance;

import com.caijuan.completable.SmallTool;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class PerformanceV1 {
    public static void main(String[] args) {

        SmallTool.printTimeAndThread("小白和朋友来了餐厅");



        int count = 39;
        // -Djava.util.completable.ForkJoinPool.common.parallelism=8
        System.setProperty("java.util.completable.ForkJoinPool.common.parallelism", "19");

        SmallTool.printTimeAndThread("三个人点了 " + count + " 盘菜");


        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Dish dish = new Dish("番茄炒蛋" + i, 1);
//            CompletableFuture<Void> cf = CompletableFuture.runAsync(dish::make);
            CompletableFuture<Void> cf = CompletableFuture.runAsync(dish::makeUseCPU);
            futures.add(cf);
        }

        // 等待所有任务执行完毕
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        long needTime = System.currentTimeMillis() - start;

        SmallTool.printTimeAndThread("\n\n做" + count + "道菜耗时：" + needTime + "\n");
    }
}
