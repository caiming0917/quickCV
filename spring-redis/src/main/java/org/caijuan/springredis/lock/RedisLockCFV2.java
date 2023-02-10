package org.caijuan.springredis.lock;


import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RedisLockCFV2 {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 6379;

    public static void main(String[] args) throws InterruptedException {

        String key = "num";
        Jedis redisClient = new Jedis(IP, PORT);
        long r = redisClient.del(key);
        System.out.println("del r : " + r);
        r = redisClient.setnx(key, "0");
        System.out.println("setnx r : " + r);

        Lock lock = new ReentrantLock();

        int count = 10;
        CompletableFuture[] cfs = new CompletableFuture[count];
        for (int i = 0; i < count; i++) {
            cfs[i] = CompletableFuture.runAsync(() -> {
                try (Jedis jedis = new Jedis(IP, PORT)) {
                    // 只有一个线程执行，其他线程不执行，程序无限循环
                    while (lock.tryLock(10, TimeUnit.MILLISECONDS)) {
                        lock.lock();
                        for (int j = 0; j < 10; j++) {
                            int value = Integer.parseInt(jedis.get(key));
                            System.out.println(Thread.currentThread().getName() + ", value : " + value);
                            jedis.set(key, String.valueOf(value + 1));
                        }
                        lock.unlock();
                    }
                } catch (Exception e) {
                    System.out.println("error : " + e.getMessage());
                }
            });
        }

        CompletableFuture.allOf(cfs).join();
        System.out.println("num => " + redisClient.get(key));
        redisClient.close();
    }
}