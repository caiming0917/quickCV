package org.caijuan.springredis.lock;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j

public class DistributedLockV2 {

    private static JedisPool jedisPool;

    private final static String LOCK_KEY = "Distributed_";

    /**
     * 分布式锁，尝试获取锁
     *
     * @param scene          场景字段，不为空
     * @param myId           线程标识，不为空
     * @param maxLockSeconds 最大锁定时间，秒，大于0
     * @return
     */
    public Boolean tryLock(String scene, String myId, int maxLockSeconds) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String key = LOCK_KEY + scene;
            Long nxResult = jedis.setnx(key, myId);
            if (nxResult.equals(1L)) {
                jedis.expire(key, maxLockSeconds);
                return true;
            }
        } catch (Exception e) {
            log.error("tryLock exception", e);
        } finally {
            returnResource(jedis);
        }
        log.error("分布式锁，尝试获取锁-失败");
        return false;
    }

    /**
     * 分布式锁，释放锁
     *
     * @param scene 场景字段，不为空
     * @param myId  线程标识，不为空
     * @return
     */
    public Boolean unLock(String scene, String myId) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String key = LOCK_KEY + scene;
            String value = jedis.get(key);
            if (!"".equals(value) && !"null".equals(value) && value.equals(myId)) {
                //哪个线程锁的，哪个线程解
                return jedis.del(key) == 1L;
            }
        } catch (Exception e) {
            log.error("unLock exception", e);
        } finally {
            returnResource(jedis);
        }
        log.error("分布式锁，释放锁-失败");
        return false;
    }

    private Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        int dataBase = 0;
        jedis.select(dataBase);
        return jedis;
    }

    private void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置最大10个连接
        jedisPoolConfig.setMaxTotal(10);
        String IP = "127.0.0.1";
        jedisPool = new JedisPool(jedisPoolConfig, IP,6379);
        System.out.println(DistributedLockV2.jedisPool);

        String myId = System.currentTimeMillis() + "";
        String scene = "distributedLockTest";
        DistributedLockV2 redisService = new DistributedLockV2();
        Boolean lockResult = redisService.tryLock(scene, myId, 10);
        if (lockResult) {
            Thread.sleep(3000);//do some thing
            Boolean unLockResult = redisService.unLock(scene, myId);
            log.error("释放锁：{}", unLockResult);
        }
    }

}
