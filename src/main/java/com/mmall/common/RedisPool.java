package com.mmall.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.mmall.util.PropertiesUtil;

public class RedisPool {
	
	private static JedisPool pool;//jedis连接池
	private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20")); //最大连接数
	private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));//在jedispool中最大的idle状态（空闲的）的jedis实例的个数
	private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));//在jedispool中最小的idle状态（空闲的）的jedis实例的个数
	
	private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true，则得到的jedis的实例肯定是可以用的
	private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true，则放回jedispool的jedis实例肯定是可以用的

	private static String redisIp=PropertiesUtil.getProperty("redis.ip");
	private static Integer redisPort=Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
	
	private static void initPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		
		config.setTestOnBorrow(testOnBorrow);
		config.setTestOnReturn(testOnReturn);
		
		//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true
		config.setBlockWhenExhausted(true);
		
		pool = new JedisPool(config, redisIp, redisPort, 1000*2);
		
	}
	
	//需要这个类在加载到JVM的时候就要初始化这个连接池
	static{
		initPool();
	}
	
	public static Jedis getJedis(){
		return pool.getResource();
	}
	
	//放回连接池
	public static void returnJedis(Jedis jedis){
		if(jedis != null){
			pool.returnResource(jedis);
		}
	}
	
	//如果是一个坏连接，放回brokenResource
	public static void returnBrokenResource(Jedis jedis){
		pool.returnBrokenResource(jedis);
	}
	
	public static void main(String[] args){
		Jedis jedis = pool.getResource();
		jedis.set("geelykey", "geelyvalue");
		returnJedis(jedis);
		pool.destroy();//临时调用，销毁连接池中的所有连接，在应用中不需要，因为在重复使用连接池
		System.out.println("over");
	}
	
}
