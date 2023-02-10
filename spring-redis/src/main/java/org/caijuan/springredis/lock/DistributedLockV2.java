package org.caijuan.springredis.lock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import static org.caijuan.springredis.lock.RedisConstant.*;

/**
 * 不支持自动续期
 */
public class DistributedLockV2 {


    // 预加载脚本，避免频繁读取脚本。同时lock.lua文件需要放在resources下
    public static final DefaultRedisScript<Integer> luaScript;

    static {
        luaScript = new DefaultRedisScript<>();
        luaScript.setLocation(new ClassPathResource("/script/lock.lua")); // 指定脚本文件路径
        luaScript.setResultType(Integer.class); // 指定脚本返回值类型
    }


    public static void main(String[] args) throws InterruptedException {
        String key = "num";
        String lock = "lock";

        Jedis redisClient = getRedisClient();
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
                try (Jedis jedis = getRedisClient()) {
                    for (int j = 0; j < 10; j++) {

                        // 加锁失败就循环重试
                        while (jedis.setnx(lock, String.valueOf(lockV)) != 1) {
//                            Thread.sleep(10);
                        }
                        int value = Integer.parseInt(jedis.get(key));
                        System.out.println(Thread.currentThread().getName() + ", value : " + value);
                        jedis.set(key, String.valueOf(value + 1));

                        // 只有是自己加的锁才能释放，使用 lua 脚本保证原子性
                        Long result = (Long) jedis.eval(luaScript.getScriptAsString(),
                                Collections.singletonList(lock), Collections.singletonList(String.valueOf(lockV)));
                        System.out.println(result);
                        while (result == 0L) {
                            result = (Long) jedis.eval(luaScript.getScriptAsString(),
                                    Collections.singletonList(lock), Collections.singletonList(String.valueOf(lockV)));
                            System.out.println(result);
//                            Thread.sleep(10);
                        }
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
