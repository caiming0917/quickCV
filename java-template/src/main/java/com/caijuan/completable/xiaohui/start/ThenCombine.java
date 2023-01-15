package com.caijuan.completable.xiaohui.start;

import com.caijuan.completable.SmallTool;

import java.util.concurrent.CompletableFuture;

public class ThenCombine {
    public static void main(String[] args) {
        thenCombineTest();
        System.out.println();

        supplyAsyncTest01();
    }


    private static void thenCombineTest() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务员蒸米饭");
            SmallTool.sleepMillis(200);
            return "米饭";
        }), (disk, rice) -> {
            SmallTool.printTimeAndThread("服务员打饭");
            SmallTool.sleepMillis(200);
            return String.format("%s 和 %s 好了", disk, rice);
        });

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread("小白开始吃" + cf1.join());
    }

    private static void supplyAsyncTest01() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            CompletableFuture<String> disk = CompletableFuture.supplyAsync(() -> {
                SmallTool.printTimeAndThread("厨师炒菜");
                SmallTool.sleepMillis(200);
                return "番茄炒蛋";
            });

            CompletableFuture<String> rice = CompletableFuture.supplyAsync(() -> {
                SmallTool.printTimeAndThread("服务员蒸米饭");
                SmallTool.sleepMillis(200);
                return "番茄炒蛋";
            });

            SmallTool.printTimeAndThread("服务员打饭");
            SmallTool.sleepMillis(200);
            return String.format("%s 和 %s 好了", disk.join(), rice.join());
        });

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread(cf1.join() + ",小白开始吃");
    }
}
