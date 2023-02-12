package com.caijuan.completable.xiaohui.advance;

import com.caijuan.utils.SmallTool;

import java.util.concurrent.CompletableFuture;

public class ApplyToEither {
    public static void main(String[] args) {
        applyToEither();
    }

    private static void applyToEither() {
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
        }), firstComeBus -> firstComeBus);
        SmallTool.info(String.format("%s,小白坐车回家了",bus.join()));
    }

    private static void extracted() {
        SmallTool.info("小白走出餐厅，来到公交站");
        SmallTool.info("等待 700路 或者 800路 公交到来");

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("700 路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "700 路公交到了";
        });

        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("800 路公交正在赶来");
            SmallTool.sleepMillis(200);
            return "800 路公交到了";
        });

        while (true) {
            if (!"".equals(cf1.join())) {
                SmallTool.info(cf1.join());
                break;
            } else if (!"".equals(cf2.join())) {
                SmallTool.info(cf2.join());
                break;
            }
        }
        SmallTool.info("小白坐车回家了");
    }
}
