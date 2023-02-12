package com.caijuan.completable.xiaohui.advance;

import com.caijuan.utils.SmallTool;

import java.util.concurrent.CompletableFuture;

public class ThenApply {
    public static void main(String[] args) {
        thenApply();
    }


    private static void thenApply() {
        SmallTool.info("小白吃完了");
        SmallTool.info("小白要求开发票");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("服务员接收 500 元");
            SmallTool.sleepMillis(100);
            return "500";
//        }).thenApplyAsync((money) -> {    // 异步
        }).thenApply((money) -> {       // 同步
                    SmallTool.info("服务员开发票");
                    SmallTool.sleepMillis(200);
                    return String.format("%s 元发票", money);
                });

        SmallTool.info("小白接到朋友电话");
        SmallTool.info(String.format("小白收到 %s , 然后回家了", future.join()));
    }

    private static void two() {
        SmallTool.info("小白吃完了");
        SmallTool.info("小白要求开发票");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("服务员接收 500 元");
            SmallTool.sleepMillis(200);
            return "500";
        }).thenCompose((money) -> CompletableFuture.supplyAsync(() -> {
            SmallTool.info("服务员开发票");
            SmallTool.sleepMillis(100);
            return String.format("%s 元发票", money);
        }));

        SmallTool.info("小白接到朋友电话");
        SmallTool.info(String.format("小白收到 %s , 然后回家了", future.join()));
    }

    private static void one() {
        SmallTool.info("小白吃完了");
        SmallTool.info("小白要求开发票");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("服务员接收 500 元");
            SmallTool.sleepMillis(100);
            SmallTool.info("服务员开发票");
            SmallTool.sleepMillis(200);
            return String.format("%s 元发票", 500);
        });

        SmallTool.info("小白接到朋友电话");
        SmallTool.info(String.format("小白收到 %s , 然后回家了", future.join()));
    }
}
