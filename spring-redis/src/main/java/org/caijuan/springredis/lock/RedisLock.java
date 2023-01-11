package org.caijuan.springredis.lock;


import redis.clients.jedis.Jedis;

public class RedisLock {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 6379;

    public static void main(String[] args) throws InterruptedException {

        String key = "num";
        Jedis redisClient = new Jedis(IP, PORT);
        long r = redisClient.del(key);
        System.out.println("del r : " + r);
        r = redisClient.setnx(key, "0");
        System.out.println("setnx r : " + r);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try (Jedis jedis = new Jedis(IP, PORT)) {
                    for (int j = 0; j < 10; j++) {
                        int value = Integer.parseInt(jedis.get(key));
                        System.out.println("value : " + value);
                        jedis.set(key, String.valueOf(value + 1));
                    }
                } catch (Exception e) {
                    System.out.println("error : " + e.getMessage());
                }
            }).start();
        }

        Thread.sleep(1000);
        System.out.println("num => " + redisClient.get(key));
        redisClient.close();
    }
}