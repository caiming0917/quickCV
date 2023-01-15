package org.caijuan.springredis.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 使用 Redisson 实现
 */
@Slf4j
public class RedisDistributedLockV2 {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 6379;

    public static void main(String[] args) throws InterruptedException {

        Jedis redisClient = new Jedis(IP, PORT);
        String key = "num";
        redisClient.set(key, "0");

        // 1.创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + IP + ":" + PORT);
        // 2.创建RedissonClient客户端
        RedissonClient redissonClient = Redisson.create(config);

        String lockKey = "lock";
        int count = 10;
        CompletableFuture[] cfs = new CompletableFuture[count];
        for (int i = 0; i < count; i++) {
            String lockV = String.valueOf(i);
            cfs[i] = CompletableFuture.runAsync(() -> {
                try (Jedis jedis = new Jedis(IP, PORT)) {
                    RLock lock = redissonClient.getLock(lockKey);
                    for (int j = 0; j < 10; j++) {
                        // 加锁
                        lock.lock(30, TimeUnit.SECONDS);
                        int value = Integer.parseInt(jedis.get(key));
                        System.out.println(Thread.currentThread().getName() + ", value : " + value);
                        jedis.set(key, String.valueOf(value + 1));
                        // 解锁
                        lock.unlock();
                    }
                } catch (Exception e) {
                    log.info("error : " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
        CompletableFuture.allOf(cfs).join();
        redissonClient.shutdown();

        System.out.println("num => " + redisClient.get(key));
        redisClient.close();
    }
}
