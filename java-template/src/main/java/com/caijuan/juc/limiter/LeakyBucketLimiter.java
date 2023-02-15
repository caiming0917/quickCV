package com.caijuan.juc.limiter;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author cai juan
 * @date 2023/2/13 22:55
 */
public class LeakyBucketLimiter extends BucketLimiter {

    private BlockingQueue<Request<?>> bucket;

    public LeakyBucketLimiter(int capacity, int rate, BlockingQueue<Request<?>> bucket) {
        this(capacity, rate);
        this.bucket = bucket;
        init();
    }

    public LeakyBucketLimiter(int capacity, int rate) {
        super(capacity, rate);
        this.bucket = new ArrayBlockingQueue<>(capacity);
        init();
    }

    private void init() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            // 取出任务执行
            try {
                Request<?> request = this.bucket.poll(2000, TimeUnit.MILLISECONDS);
                if (Objects.nonNull(request)) {
                    super.handleRequest(request);
                }
            } catch (InterruptedException e) {
                // throw new RuntimeException("处理请求失败", e);
            }
        }, 10, 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> boolean tryAcquire(Request<T> request) {
        return this.bucket.size() < this.capacity;
    }

    @Override
    public <T> void handleRequest(Request<T> request) {
        // boolean success = this.bucket.offer(request, 1000, TimeUnit.MILLISECONDS);
        boolean success = this.bucket.offer(request);
        if (!success) {
            // throw new RuntimeException("系统繁忙");
        }
    }
}
