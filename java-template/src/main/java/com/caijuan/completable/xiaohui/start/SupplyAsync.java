package com.caijuan.completable.xiaohui.start;

import com.caijuan.completable.SmallTool;

import java.util.concurrent.CompletableFuture;

public class SupplyAsync {
    public static void main(String[] args) {
        SmallTool.printTimeAndThread("小白走进一家餐厅");
        SmallTool.printTimeAndThread("小白点了一份蛋炒饭~");

        // 提供者接口 ： 什么都不给，我就能返回你要的东西
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师开始准备米饭");
            SmallTool.sleepMillis(200);
            SmallTool.printTimeAndThread("厨师开始炒菜");
            SmallTool.sleepMillis(200);
            return "蛋炒饭";
        });

        SmallTool.printTimeAndThread("小白开始打王者");
        SmallTool.printTimeAndThread(String.format("小白开始吃%s", cf1.join()));
    }
}

