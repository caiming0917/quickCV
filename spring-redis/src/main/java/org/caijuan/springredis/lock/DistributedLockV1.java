package org.caijuan.springredis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.CompletableFuture;

import static org.caijuan.springredis.lock.RedisConstant.*;

/**
 * 最大的问题是可能导致误释放别人的锁
 */
public class DistributedLockV1 {

    public static void main(String[] args) throws InterruptedException {
        Jedis redisClient = getRedisClient();

        String key = "num";
        long r = redisClient.del(key);
        System.out.println("del r : " + r);

        String lock = "lock";
        r = redisClient.del(lock);
        System.out.println("del r : " + r);


        // 设置过期时间
        long time = 10L;
        SetParams setParams = new SetParams().nx().ex(time);
        redisClient.set(key, "0");

        int count = 10;
        CompletableFuture[] cfs = new CompletableFuture[count];
        for (int i = 0; i < count; i++) {
            final int lockV = i;
            cfs[i] = CompletableFuture.runAsync(() -> {
                try (Jedis jedis = getRedisClient()) {
                    for (int j = 0; j < 10; j++) {
                        // 给锁添加过期时间，加锁失败就循环重试
                        while (!"OK".equals(jedis.set(lock, String.valueOf(lockV), setParams))) {
                            Thread.sleep(2);
                        }
                        int value = Integer.parseInt(jedis.get(key));
                        System.out.println(Thread.currentThread().getName() + ", value : " + value);
                        jedis.set(key, String.valueOf(value + 1));
                        jedis.del(lock);
                    }
                } catch (Exception e) {
                    System.out.println("error : " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }

        CompletableFuture.allOf(cfs).join();
        System.out.println("num => " + redisClient.get(key));
        redisClient.close();
    }
}
