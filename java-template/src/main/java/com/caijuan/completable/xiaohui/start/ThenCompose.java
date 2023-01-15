package com.caijuan.completable.xiaohui.start;

import com.caijuan.completable.SmallTool;

import java.util.concurrent.CompletableFuture;

public class ThenCompose {
    public static void main(String[] args) {
        thenComposeTest();
        System.out.println();

        supplyAsyncTest01();
        System.out.println();

        supplyAsyncTest02();
    }

    private static void supplyAsyncTest01() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(100);
            CompletableFuture<String> rice = CompletableFuture.supplyAsync(() -> {
                SmallTool.printTimeAndThread("厨师炒菜");
                SmallTool.sleepMillis(100);
                return "番茄炒蛋";
            });
            return rice.join() + "番茄炒蛋";
        });

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread("小白开始吃" + cf1.join());
    }


    private static void supplyAsyncTest02() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        });

        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务员打饭");
            SmallTool.sleepMillis(100);
            return "米饭" + cf1.join();
        });

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread("小白开始吃" + cf2.join());
    }


    private static void thenComposeTest() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        }).thenCompose((dish) -> CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务员打饭");
            return "米饭" + dish;
        }));

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread("小白开始吃" + cf1.join());
    }


}
