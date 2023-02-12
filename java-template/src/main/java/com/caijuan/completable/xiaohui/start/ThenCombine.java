package com.caijuan.completable.xiaohui.start;

import com.caijuan.utils.SmallTool;

import java.util.concurrent.CompletableFuture;

public class ThenCombine {
    public static void main(String[] args) {
        thenCombineTest();
        System.out.println();

        supplyAsyncTest01();
    }


    private static void thenCombineTest() {
        SmallTool.info("小白进入餐厅");
        SmallTool.info("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            SmallTool.info("服务员蒸米饭");
            SmallTool.sleepMillis(200);
            return "米饭";
        }), (disk, rice) -> {
            SmallTool.info("服务员打饭");
            SmallTool.sleepMillis(200);
            return String.format("%s 和 %s 好了", disk, rice);
        });

        SmallTool.info("小白在打王者");
        SmallTool.info("小白开始吃" + cf1.join());
    }

    private static void supplyAsyncTest01() {
        SmallTool.info("小白进入餐厅");
        SmallTool.info("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            CompletableFuture<String> disk = CompletableFuture.supplyAsync(() -> {
                SmallTool.info("厨师炒菜");
                SmallTool.sleepMillis(200);
                return "番茄炒蛋";
            });

            CompletableFuture<String> rice = CompletableFuture.supplyAsync(() -> {
                SmallTool.info("服务员蒸米饭");
                SmallTool.sleepMillis(200);
                return "番茄炒蛋";
            });

            SmallTool.info("服务员打饭");
            SmallTool.sleepMillis(200);
            return String.format("%s 和 %s 好了", disk.join(), rice.join());
        });

        SmallTool.info("小白在打王者");
        SmallTool.info(cf1.join() + ",小白开始吃");
    }
}
