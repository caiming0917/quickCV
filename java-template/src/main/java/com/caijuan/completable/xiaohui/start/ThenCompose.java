package com.caijuan.completable.xiaohui.start;

import com.caijuan.utils.SmallTool;

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
        SmallTool.info("小白进入餐厅");
        SmallTool.info("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("厨师炒菜");
            SmallTool.sleepMillis(100);
            CompletableFuture<String> rice = CompletableFuture.supplyAsync(() -> {
                SmallTool.info("厨师炒菜");
                SmallTool.sleepMillis(100);
                return "番茄炒蛋";
            });
            return rice.join() + "番茄炒蛋";
        });

        SmallTool.info("小白在打王者");
        SmallTool.info("小白开始吃" + cf1.join());
    }


    private static void supplyAsyncTest02() {
        SmallTool.info("小白进入餐厅");
        SmallTool.info("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        });

        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("服务员打饭");
            SmallTool.sleepMillis(100);
            return "米饭" + cf1.join();
        });

        SmallTool.info("小白在打王者");
        SmallTool.info("小白开始吃" + cf2.join());
    }


    private static void thenComposeTest() {
        SmallTool.info("小白进入餐厅");
        SmallTool.info("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        }).thenCompose((dish) -> CompletableFuture.supplyAsync(() -> {
            SmallTool.info("服务员打饭");
            return "米饭" + dish;
        }));

        SmallTool.info("小白在打王者");
        SmallTool.info("小白开始吃" + cf1.join());
    }


}
