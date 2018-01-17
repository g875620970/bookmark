package com.clearbill.util;

import redis.clients.jedis.Jedis;

/**
 * redis的工具类
 */
public class RedisUtils {

    private static Jedis redisClients = new Jedis("119.23.240.57");
    
    public static Jedis getInstall(){
    	if(redisClients==null){
    		redisClients = new Jedis("119.23.240.57");
    	}
    	return redisClients;
    }

}
