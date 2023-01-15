package com.caijuan.completable.xiaohui.expand;

import com.caijuan.completable.SmallTool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

public class MethodPrompt {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        CompletableFuture<String> completionStage = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜1");
            return "disk";
        }, threadPool);

        BiFunction<String, String, String> biFunction = (disk, rice) -> {
            SmallTool.printTimeAndThread("服务员打饭");
            SmallTool.sleepMillis(200);
            return String.format("%s 和 %s 好了", rice, disk);
        };

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            return "disk";
        }, threadPool);

        future.thenCombineAsync(completionStage, biFunction);
        biFunction.apply(completionStage.join(), future.join());
    }
}
