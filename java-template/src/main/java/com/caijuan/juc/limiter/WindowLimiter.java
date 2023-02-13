package com.caijuan.juc.limiter;

/**
 * @author cai juan
 * @date 2023/2/12 22:42
 */
public abstract class WindowLimiter implements Limiter {

    /**
     * 窗口大小，毫秒为单位
     */
    int windowSize;

    /**
     * 窗口内限流大小
     */
    int limit;

    public WindowLimiter(int windowSize, int limit) {
        this.windowSize = windowSize;
        this.limit = limit;
    }
}
