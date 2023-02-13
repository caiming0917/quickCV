package com.caijuan.juc.limiter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author cai juan
 * @date 2023/2/13 22:55
 */
public class LeakyBucketLimiter extends BucketLimiter {

    private final BlockingQueue<Request<?>> bucket;

    public LeakyBucketLimiter(int capacity, int rate, BlockingQueue<Request<?>> bucket) {
        super(capacity, rate);
        this.bucket = bucket;
    }

    public LeakyBucketLimiter(int capacity, int rate) {
        super(capacity, rate);
        this.bucket = new ArrayBlockingQueue<>(capacity * 100);
    }

    @Override
    public <T> boolean tryAcquire(Request<T> request) {
        return false;
    }
}
