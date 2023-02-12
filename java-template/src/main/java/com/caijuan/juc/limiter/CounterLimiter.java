package com.caijuan.juc.limiter;

import com.caijuan.utils.SmallTool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cai juan
 * @date 2023/2/12 22:39
 */
public class CounterLimiter extends WindowLimiter {

    /**
     * 计数器
     */
    private final AtomicInteger count;

    ScheduledExecutorService service;

    public CounterLimiter(int windowSize, int limit) {
        super(windowSize, limit);
        this.count = new AtomicInteger();
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> count.set(0), windowSize, windowSize, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> boolean tryAcquire(Request<T> request) {
        return count.incrementAndGet() <= limit;
    }

    @Override
    public <T> void handleRequest(Request<T> request) {
        SmallTool.info("RequestId => " + request.getRequestId());
    }

    public void shutdown() {
        service.shutdown();
        try {
            service.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            SmallTool.info("limiter Thread never terminated");
            service.shutdownNow();
        }
    }
}
