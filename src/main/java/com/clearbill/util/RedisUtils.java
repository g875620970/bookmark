package com.clearbill.util;

import redis.clients.jedis.Jedis;

/**
 * redis的工具类
 */
public class RedisUtils {

    private static Jedis redisClients = new Jedis("114.215.71.63",8845);
    
    public static Jedis getInstall(){
    	return redisClients;
    }

    public static void main(String[] args) {
    	Jedis redisClients = new Jedis("119.23.240.57");
    	System.out.println(redisClients.get("userName"));
	}
}
