package com.clearbill.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis的工具类
 */
public class JedisUtils {

	private static JedisPool jedisPool;

	/**
	 * 建立连接池
	 */
	private static void createJedisPool() {
		// 建立连接池配置参数
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxTotal(20);
		// 设置最大阻塞时间，毫秒数
		config.setMaxWaitMillis(2000);
		// 设置空闲连接
		config.setMaxIdle(3);
		//测试连接可用
		config.setTestOnBorrow(true);
		// 创建连接池
		jedisPool = new JedisPool(config, "119.23.240.57");

	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (jedisPool == null){
			createJedisPool();
		}
	}

	/**
	 * 获取一个jedis 对象
	 * 
	 * @return
	 */
	public static Jedis getInstall() {
		Jedis jedis = null;
		try {
			if (jedisPool == null){
				poolInit();
			}
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			jedis.close();
		}
		return jedis;
	}
	
}
