package com.caijuan.juc.limiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author cai juan
 * @date 2023/2/16 22:40
 */
public class TokenBucketLimiter extends BucketLimiter {

    private final AtomicLong startTime;

    public TokenBucketLimiter(int capacity, int rate) {
        super(capacity, rate);
        startTime = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    synchronized public <T> boolean tryAcquire(Request<T> request) {
        // 看是否有令牌
        long tokenCount = (Math.min(System.currentTimeMillis() - startTime.get(), capacity)) / rate;
        return tokenCount >= 0;
    }

    @Override
    public <T> void handleRequest(Request<T> request) {
        // 请求过来取走令牌
        startTime.addAndGet(rate);
        super.handleRequest(request);
    }
}
