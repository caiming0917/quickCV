package org.caijuan.springredis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class RedisDistributedLock {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 6379;
    private static final Integer REDIS_SUCCESS_NUM = 1;
    private static final Long REDIS_FAIL_NUM = 0L;
    private static final String REDIS_SUCCESS_FLAG = "OK";


    // 预加载脚本，避免频繁读取脚本。同时lock.lua文件需要放在resources下
    public static final DefaultRedisScript<Long> luaScript;

    static {
        luaScript = new DefaultRedisScript<>();
        luaScript.setLocation(new ClassPathResource("/script/lock.lua")); // 指定脚本文件路径
        luaScript.setResultType(Long.class); // 指定脚本返回值类型
    }

    // 尝试获取锁
    public static boolean tryLock(String lockKey, String value, long time, Jedis jedis) {
        // count 防止无限重试，如果间隔 2+1+1 = 4s 左右还是拿不到锁，放弃
        // TODO 01: 因为网络问题，加锁失败的情况不同，处理因为不同，不总都是重试
        int count = 11;
        int no = 0;
        boolean isSuccess;
        while (!(isSuccess = lock(lockKey, value, time, jedis)) && no < count) {
            interval((long) Math.pow(2, no++));
        }
        return isSuccess;
    }

    // 加锁
    public static boolean lock(String lockKey, String value, long time, Jedis jedis) {
        // TODO 02: 支持可重入锁
        // TODO 04: 思考是否可以对数据集合加分段锁
        SetParams setParams = new SetParams().nx().ex(time);
        return REDIS_SUCCESS_FLAG.equals(jedis.set(lockKey, String.valueOf(value), setParams));
    }

    // 间隔
    public static void interval(long time) {
        try {
            log.info("time = > " + time);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 执行 lua 脚本
    public static Object evalLua(String lockKey, String value, DefaultRedisScript<Long> luaScript, Jedis jedis) {
        return jedis.eval(luaScript.getScriptAsString(),
                Collections.singletonList(lockKey), Collections.singletonList(String.valueOf(value)));
    }

    // 解锁
    public static void unLock(String lockKey, String value, DefaultRedisScript<Long> script, Jedis jedis) {
        // 只有是自己加的锁才能释放，使用 lua 脚本保证原子性
        Long result = (Long) evalLua(lockKey, value, script, jedis);
        while (result.equals(REDIS_FAIL_NUM)) {
            result = (Long) evalLua(lockKey, value, script, jedis);
        }
    }

    // TODO 03: 续期 —— 自定续期或者手动续期(后台线程定期延长锁到期时间)
//    Refresh

    public static void main(String[] args) throws InterruptedException {
        Jedis redisClient = new Jedis(IP, PORT);

        // 初始化数据
        String key = "num";
        redisClient.set(key, "0");

        // 清除锁
        String lock = "lock";
        redisClient.del(lock);

        int count = 10;
        int time = 10;
        CompletableFuture[] cfs = new CompletableFuture[count];
        for (int i = 0; i < count; i++) {
            String lockV = String.valueOf(i);
            cfs[i] = CompletableFuture.runAsync(() -> {
                try (Jedis jedis = new Jedis(IP, PORT)) {
                    for (int j = 0; j < 10; j++) {
                        if (!tryLock(lock, lockV, time, jedis)) {
                            throw new RuntimeException("加锁失败，请稍后重试");
                        }
                        int value = Integer.parseInt(jedis.get(key));
                        log.info(Thread.currentThread().getName() + ", value : " + value);
                        jedis.set(key, String.valueOf(value + 1));
                        unLock(lock, lockV, luaScript, jedis);
                    }
                } catch (Exception e) {
                    log.info("error : " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
        CompletableFuture.allOf(cfs).join();
        System.out.println("num => " + redisClient.get(key));
        redisClient.close();
    }
}
