package org.caijuan.springredis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

@Slf4j
@Component
public class DistributedLockDemo {

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
        Transaction multi = null;
        try {
            jedis = getJedis();
            // 开启事务
            multi =  jedis.multi();
            String key = LOCK_KEY + scene;
            // 这个不保证原子性
            Long nxResult = jedis.setnx(key, myId);
            if (nxResult.equals(1L)) {
                jedis.expire(key, maxLockSeconds);
                // 提交事务
                multi.exec();
                return true;
            }
        } catch (Exception e) {
            //放弃事务 事务中的所有命令都不会执行
            assert multi != null;
            multi.discard();
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
        System.out.println(DistributedLockDemo.jedisPool);

        String myId = System.currentTimeMillis() + "";
        String scene = "distributedLockTest";
        DistributedLockDemo redisService = new DistributedLockDemo();
        Boolean lockResult = redisService.tryLock(scene, myId, 10);
        if (lockResult) {
            Thread.sleep(3000);//do some thing
            Boolean unLockResult = redisService.unLock(scene, myId);
            log.error("释放锁：{}", unLockResult);
        }
    }

}
