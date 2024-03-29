package com.caijuan.completable.xiaohui.performance;

import com.caijuan.utils.SmallTool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadReuse {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());

        CompletableFuture.runAsync(() -> SmallTool.info("A"), executor)
                .thenRunAsync(() -> SmallTool.info("B"), executor)
                .join();
    }
}
