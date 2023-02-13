package com.caijuan.juc.limiter;

/**
 * @author cai juan
 * @date 2023/2/13 22:54
 */
public abstract class BucketLimiter implements Limiter {

    /**
     * 限流数
     */
    private final int capacity;

    /**
     * 速率
     */
    private final int rate;

    /**
     * 剩余数量
     */
    private final int amount;

    public BucketLimiter(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        amount = capacity;
    }

    @Override
    public abstract <T> boolean tryAcquire(Request<T> request);
}
