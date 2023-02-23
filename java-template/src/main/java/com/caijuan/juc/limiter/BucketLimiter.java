package com.caijuan.juc.limiter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cai juan
 * @date 2023/2/13 22:54
 */
public abstract class BucketLimiter implements Limiter {

    /**
     * 限流数 / 桶的容量
     */
    protected final int capacity;

    /**
     * 速率 / 每多少秒生成一个令牌或放出一个请求
     */
    protected final int rate;

    /**
     * 剩余数量
     */
    protected final AtomicInteger amount;

    public BucketLimiter(int capacity, int rate) {
        if (capacity <= 0 || rate <= 0){
            throw new IllegalArgumentException("capacity,rate为正整数");
        }
        this.capacity = capacity;
        this.rate = rate;
        amount = new AtomicInteger(capacity);
    }

    @Override
    public abstract <T> boolean tryAcquire(Request<T> request);
}
