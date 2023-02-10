package org.caijuan.springredis.lock;

import redis.clients.jedis.Jedis;

public class RedisConstant {

    public static final String IP = "localhost";

    public static final int PORT = 6379;

//    public static final String PASSWORD = "";

    public static Jedis getRedisClient(){
        Jedis jedis = new Jedis(IP, PORT);
//        jedis.auth(PASSWORD);
        jedis.select(10);
        return jedis;
    }

}
