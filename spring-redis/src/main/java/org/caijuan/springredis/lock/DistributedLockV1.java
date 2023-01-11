package org.caijuan.springredis.lock;

import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public class DistributedLockV1 {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 6379;

    public static void main(String[] args) throws InterruptedException {
        String key = "num";
        long time = 1L;
        String lock = "lock";

        Jedis redisClient = new Jedis(IP, PORT);
        long r = redisClient.del(key);
        System.out.println("del r : " + r);
        r = redisClient.del(lock);
        System.out.println("del r : " + r);

        redisClient.setnx(key, "0");

        int count = 10;
        CompletableFuture[] cfs = new CompletableFuture[count];
        for (int i = 0; i < count; i++) {
            final int lockV = i;
            cfs[i] = CompletableFuture.runAsync(() -> {
                try (Jedis jedis = new Jedis(IP, PORT)) {
//                    for (int j = 0; j < 10; j++) {
                        //
                        while ("ok".equals(jedis.setex(lock,time, String.valueOf(lockV)))) {
                        }
                        int value = Integer.parseInt(jedis.get(key));
                        System.out.println(Thread.currentThread().getName() + ", value : " + value);
                        jedis.set(key, String.valueOf(value + 1));
                        jedis.del(lock);
//                    }
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
