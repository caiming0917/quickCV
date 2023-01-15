package org.caijuan.springredis.lock;

import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

/**
 * 最大的问题是执行的程序异常且未执行释放锁，就会导致锁无法释放
 */
public class DistributedLockV0 {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 6379;

    public static void main(String[] args) throws InterruptedException {
        String key = "num";
        String lock = "lock";

        Jedis redisClient = new Jedis(IP, PORT);
        long r = redisClient.del(key);
        System.out.println("del r : " + r);
        // 清除锁
        r = redisClient.del(lock);
        System.out.println("del lock : " + r);

        redisClient.set(key, "0");

        int count = 10;
        CompletableFuture[] cfs = new CompletableFuture[count];
        for (int i = 0; i < count; i++) {
            final int lockV = i;
            cfs[i] = CompletableFuture.runAsync(() -> {
                try (Jedis jedis = new Jedis(IP, PORT)) {
                    for (int j = 0; j < 10; j++) {
                        // 加锁失败就循环重试
                        while (jedis.setnx(lock, String.valueOf(lockV)) != 1) {
//                            Thread.sleep(10);
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
