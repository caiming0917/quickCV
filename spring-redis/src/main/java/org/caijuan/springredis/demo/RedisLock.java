package org.caijuan.springredis.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Component
public class RedisLock implements Lock {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Object singleLockHandle(String key, LockCallBack callBack, long expereTime) {
        Object obj = null;

        boolean locked = false;
        try {
            locked = tryLock(key, expereTime);
            if (!locked) {
                throw new RuntimeException("LOCK_ERROR");
            }
            obj = callBack.handle();
            releaseLock(key);
        } catch (RuntimeException e) {
            if (locked) {
                releaseLock(key);
            }
            throw e;
        } catch (Exception e) {
            log.error("锁业务处理未知错误", e);
            if (locked) {
                releaseLock(key);
            }
            throw new LockLogicHandleException(e);
        }
        return obj;
    }

    @Override
    public Object requestLockHandle(String key, LockCallBack callBack, long expereTime) {
        boolean locked = false;
        Object obj = null;
        try {
            locked = tryTryLock(key, expereTime);
            if (!locked) {
                throw new RuntimeException("LOCK_ERROR");
            }
            obj = callBack.handle();
            releaseLock(key);
        } catch (RuntimeException e) {
            if (locked) {
                releaseLock(key);
            }
            throw e;
        } catch (Exception e) {
            log.error("锁业务处理未知错误", e);
            if (locked) {
                releaseLock(key);
            }
            throw new LockLogicHandleException(e);
        }
        return obj;
    }

    private boolean tryLock(String key, long expereTime) {
        Long currentTime = this.currentTime();

        // 获取锁
        // 3分钟
        boolean success = Boolean.TRUE.equals(redisTemplate.execute((RedisConnection conn) -> {

            boolean succ = Boolean.TRUE.equals(conn.setNX(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key)), redisTemplate.getStringSerializer().serialize(currentTime.toString())));
            if (succ) {
                conn.expire(key.getBytes(), expereTime);// 3分钟
            }
            return succ;
        }));
        if (success) {
            return success;
        }

        // 防止宕机导致锁一直存在，比较锁value的时间值与当前时间，如果锁是在很久以前加上的，则可以认为该锁异常，可强制释放锁
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String value = operations.get(key);
        if (StringUtils.hasLength(value)) {
            Long oldTime = Long.valueOf(value);

            if (currentTime - oldTime > expereTime * 1000) {// 3分钟
                // 释放锁
                releaseLock(key);

                // 再次获取锁
                // 3分钟
                return Boolean.TRUE.equals(redisTemplate.execute((RedisConnection conn) -> {

                    boolean succ = Boolean.TRUE.equals(conn.setNX(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key)), Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(currentTime.toString()))));
                    if (succ) {
                        conn.expire(key.getBytes(), expereTime);// 3分钟
                    }
                    return succ;
                }));
            }
        }
        return false;
    }

    private boolean tryTryLock(String key, long expereTime) {
        Long currentTime = this.currentTime();

        // 获取锁
        boolean success = false;
        for (int i = 0; i < 15; i++) {
            // 3分钟
            success = Boolean.TRUE.equals(redisTemplate.execute((RedisConnection conn) -> {

                boolean succ = Boolean.TRUE.equals(conn.setNX(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key)), redisTemplate.getStringSerializer().serialize(currentTime.toString())));
                if (succ) {
                    conn.expire(key.getBytes(), expereTime);// 3分钟
                }
                return succ;
            }));
            if (success) {
                return success;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("tryTryLock sleep error ", e);
                Thread.currentThread().interrupt();
            }
        }

        // 防止宕机导致锁一直存在，比较锁value的时间值与当前时间，如果锁是在很久以前加上的，则可以认为该锁异常，可强制释放锁
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String value = operations.get(key);
        if (StringUtils.hasLength(value)) {
            Long oldTime = Long.valueOf(value);

            if (currentTime - oldTime > expereTime * 1000) {// 3分钟
                // 释放锁
                releaseLock(key);

                // 再次获取锁
                // 3分钟
                return Boolean.TRUE.equals(redisTemplate.execute((RedisConnection conn) -> {

                    boolean succ = Boolean.TRUE.equals(conn.setNX(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key)), Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(currentTime.toString()))));
                    if (succ) {
                        conn.expire(key.getBytes(), expereTime);// 3分钟
                    }
                    return succ;
                }));
            }
        }
        return false;
    }

    private void releaseLock(String key) {
        redisTemplate.execute((RedisConnection conn) -> conn.del(key.getBytes()));
    }

    private Long currentTime() {
        return redisTemplate.execute((RedisConnection conn) -> conn.time());
    }

    public Boolean updateLock(String key) {
        Long currentTime = this.currentTime();
        boolean success = false;
        // 3分钟
        success = Boolean.TRUE.equals(redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection conn) {
                boolean succ = Boolean.TRUE.equals(conn.setNX(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key)), Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(currentTime.toString()))));
                conn.expire(key.getBytes(), 3 * 60L);// 3分钟
                return succ;
            }
        }));
        if (success) {
            return success;
        } else {
            return false;
        }
    }
}
