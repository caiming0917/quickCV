package com.caijuan.juc.limiter;

import com.caijuan.utils.SmallTool;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author cai juan
 * @date 2023/2/13 22:55
 */
public class LeakyBucketLimiter extends BucketLimiter {

    private BlockingQueue<Request<?>> bucket;

    // private final ReentrantLock lock = new ReentrantLock();

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
        // TODO : 分发请求的线程只有一个，思考是否存在问题，比如线程阻塞或者挂了？忙不过来？
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            // 取出任务执行
            try {
                Request<?> request = this.bucket.poll(2000, TimeUnit.MILLISECONDS);
                if (Objects.nonNull(request)) {
                    super.handleRequest(request);
                    // this.amount.addAndGet(-1);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("处理请求失败", e);
            }
        }, 10, 10, TimeUnit.MILLISECONDS);
    }

    // tryAcquire V1.0 : 存在并发问题
    @Override
    public <T> boolean tryAcquire(Request<T> request) {
        // return this.amount.get() != 0;
        return this.bucket.size() < this.capacity;
    }

    /*
    @Override
    synchronized public <T> boolean tryAcquire(Request<T> request) {
        // lock.lock();
        boolean result = this.bucket.size() < this.capacity;
        // lock.unlock();
        return result;
    }
    */


    @Override
    public <T> void handleRequest(Request<T> request) {
        // boolean success = this.bucket.offer(request, 1000, TimeUnit.MILLISECONDS);
        // this.amount.addAndGet(-1);
        boolean success = this.bucket.offer(request);
        if (!success) {
            SmallTool.info("系统繁忙");
            // 不能直接抛出异常，可能导致线程全部阻塞
            // throw new RuntimeException("系统繁忙");
            // TODO : 重试，排队，降级，拒绝等
        }
    }
}
