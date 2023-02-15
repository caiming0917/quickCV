package com.caijuan.juc.limiter;

/**
 * @author cai juan
 * @date 2023/2/13 22:54
 */
public abstract class BucketLimiter implements Limiter {

    /**
     * 限流数
     */
    protected final int capacity;

    /**
     * 速率
     */
    protected final int rate;

    /**
     * 剩余数量
     */
    protected final int amount;

    public BucketLimiter(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        amount = capacity;
    }

    @Override
    public abstract <T> boolean tryAcquire(Request<T> request);
}
