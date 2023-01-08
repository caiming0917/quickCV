package com.caijuan.completable.advance;

import com.caijuan.completable.SmallTool;

import java.util.concurrent.CompletableFuture;

public class ApplyToEither {
    public static void main(String[] args) {
        applyToEither();
    }

    private static void applyToEither() {
        SmallTool.printTimeAndThread("小白走出餐厅，来到公交站");
        SmallTool.printTimeAndThread("等待 700路 或者 800路 公交到来");

        CompletableFuture<String> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("700 路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "700 路公交到了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("800 路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "800 路公交到了";
        }), firstComeBus -> firstComeBus);
        SmallTool.printTimeAndThread(String.format("%s,小白坐车回家了",bus.join()));
    }

    private static void extracted() {
        SmallTool.printTimeAndThread("小白走出餐厅，来到公交站");
        SmallTool.printTimeAndThread("等待 700路 或者 800路 公交到来");

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("700 路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "700 路公交到了";
        });

        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("800 路公交正在赶来");
            SmallTool.sleepMillis(200);
            return "800 路公交到了";
        });

        while (true) {
            if (!"".equals(cf1.join())) {
                SmallTool.printTimeAndThread(cf1.join());
                break;
            } else if (!"".equals(cf2.join())) {
                SmallTool.printTimeAndThread(cf2.join());
                break;
            }
        }
        SmallTool.printTimeAndThread("小白坐车回家了");
    }
}
