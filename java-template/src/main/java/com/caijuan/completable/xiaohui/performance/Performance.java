package com.caijuan.completable.xiaohui.performance;

import com.caijuan.utils.SmallTool;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Performance {
    public static void main(String[] args) {
        SmallTool.info("线程数：" + Runtime.getRuntime().availableProcessors());

        SmallTool.info("小白和朋友来了餐厅");


        int count = 58;
        SmallTool.info("三个人点了 " + count + " 盘菜");


        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Dish disk = new Dish("番茄炒蛋" + i, 200);
            CompletableFuture<Void> cf = CompletableFuture.runAsync(disk::makeUseCPU);
            futures.add(cf);
        }
        for (int i = 0; i < count; i++) {
            futures.get(i).join();
        }
        long needTime = System.currentTimeMillis() - start;

        SmallTool.info("\n\n做" + count + "道菜耗时：" + needTime + "\n");
    }
}
