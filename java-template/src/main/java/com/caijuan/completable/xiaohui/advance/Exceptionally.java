package com.caijuan.completable.xiaohui.advance;

import com.caijuan.utils.SmallTool;

import java.util.concurrent.CompletableFuture;

public class Exceptionally {
    public static void main(String[] args) {
        SmallTool.info("小白走出餐厅，来到公交站");
        SmallTool.info("等待 700路 或者 800路 公交到来");

        CompletableFuture<String> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("700 路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "700 路公交到了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SmallTool.info("800 路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "800 路公交到了";
        }), firstComeBus -> {
            SmallTool.info(String.format("%s ,小白上了车",firstComeBus));
            if(firstComeBus.startsWith("700")){
                throw new RuntimeException(String.format("%s 撞树上了",firstComeBus));
            }
            return firstComeBus;
        }).exceptionally(e -> {
            SmallTool.info(e.getMessage());
            SmallTool.info("小白叫了出租车");
            return "出租车叫到了";
        });
        SmallTool.info(String.format("%s,小白坐车回家了",bus.join()));
    }
}
