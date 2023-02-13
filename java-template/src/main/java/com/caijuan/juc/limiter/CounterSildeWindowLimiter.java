package com.caijuan.juc.limiter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author cai juan
 * @date 2023/2/13 21:37
 */
public class CounterSildeWindowLimiter extends WindowLimiter {


    /**
     * 窗口数量
     */
    private final int windowNum;

    private final AtomicInteger[] counters;

    private final AtomicLong startTime;

    /**
     * 当前窗口下标(逻辑上的最左侧窗口)
     */
    private final AtomicInteger index;


    public CounterSildeWindowLimiter(int windowSize, int limit, int windowNum) {
        super(windowSize, limit);
        this.windowNum = windowNum;
        this.counters = new AtomicInteger[windowNum];
        for (int i = 0; i < this.counters.length; i++) {
            counters[i] = new AtomicInteger(0);
        }
        startTime = new AtomicLong(System.currentTimeMillis());
        index = new AtomicInteger(0);
    }

    @Override
    public <T> boolean tryAcquire(Request<T> request) {
        long currentTime = System.currentTimeMillis();
        // 计算需要移动的窗口数
        int unitSize = windowSize / windowNum;
        long slideNum = (Math.max(currentTime - startTime.get() - windowSize, 0) / unitSize);
        slideWindow(slideNum);
        // 累加通过的请求数
        int sum = 0;
        for (AtomicInteger counter : counters) {
            sum += counter.get();
        }
        if(sum >= limit){
            return false;
        }
        // 请求通过，当前窗口通过请求数加一
        counters[index.get()].addAndGet(1);
        return true;
    }

    /**
     * 滑动窗口
     *
     * @param slideNum 需要移动的窗口数
     */
    private void slideWindow(long slideNum) {
        if (slideNum <= 0) {
            return;
        }
        /**
         * 1、如果 windowNum 更小，腾空 windowNum-1 窗口，最左边的窗口清空
         * 2、如果 slideNum 更小，腾空 slideNum-1 窗口，最左边的窗口清空，保留未用的窗口
         */
        long num = Math.min(slideNum, windowNum);
        // 时间过了的窗口清零(想象移到了右边)
        for (int i = 0; i < num; i++) {
            index.set((index.get() + 1) % windowNum);
            counters[index.get()].set(0);
        }
        // 更新开始时间
        int unitSize = windowSize / windowNum;
        startTime.set(startTime.get() + unitSize * slideNum);
    }

    @Override
    public <T> void handleRequest(Request<T> request) {
        super.handleRequest(request);
    }
}
