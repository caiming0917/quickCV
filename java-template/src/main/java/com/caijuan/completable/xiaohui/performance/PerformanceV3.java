package com.caijuan.completable.xiaohui.performance;

import com.caijuan.utils.SmallTool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class PerformanceV3 {
    public static void main(String[] args) {

        SmallTool.info("小白和朋友来了餐厅");
        ExecutorService threadPool = Executors.newCachedThreadPool();


        int count = 39;
        SmallTool.info("三个人点了 " + count + " 盘菜");

        long start = System.currentTimeMillis();
        CompletableFuture[] futures = IntStream.rangeClosed(1, count)
                .mapToObj(i -> new Dish("菜" + i, 1))
                .map(dish -> CompletableFuture.runAsync(dish::make, threadPool))
                .toArray(CompletableFuture[]::new);

        // 等待所有任务执行完毕
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        CompletableFuture.allOf(futures).join();
        long needTime = System.currentTimeMillis() - start;
        threadPool.shutdown();

        SmallTool.info("\n\n做" + count + "道菜耗时：" + needTime + "\n");
    }
}
