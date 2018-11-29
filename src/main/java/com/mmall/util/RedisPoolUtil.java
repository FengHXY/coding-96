package com.mmall.util;

import redis.clients.jedis.Jedis;

import com.mmall.common.RedisPool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisPoolUtil {

	public static String set(String key, String value){
		Jedis jedis = null;
		String result = null;
		
		try {
			jedis = RedisPool.getJedis();
			result = jedis.set(key, value);
		} catch (Exception e) {
			log.error("set key:{} value:{}", key, value, e);
			RedisPool.returnBrokenResource(jedis);
		}
		RedisPool.returnJedis(jedis);
		return result;
	}
	
	public static String get(String key){
		Jedis jedis = null;
		String result = null;
		
		try {
			jedis = RedisPool.getJedis();
			result = jedis.get(key);
		} catch (Exception e) {
			log.error("get key:{}", key, e);
			RedisPool.returnBrokenResource(jedis);
		}
		RedisPool.returnJedis(jedis);
		return result;
	}
	
	//exTime用来设置有效时间,单位是秒
	public static String setEx(String key, String value, int exTime){
		Jedis jedis = null;
		String result = null;
		
		try {
			jedis = RedisPool.getJedis();
			result = jedis.setex(key, exTime, value);
		} catch (Exception e) {
			log.error("set key:{} value:{}", key, value, e);
			RedisPool.returnBrokenResource(jedis);
		}
		RedisPool.returnJedis(jedis);
		return result;
	}
	
	/**
	 * 设置key的有效期，单位是秒
	 * @param key
	 * @param exTime
	 * @return
	 */
	public static Long expire(String key, int exTime){
		Jedis jedis = null;
		Long result = null;
		
		try {
			jedis = RedisPool.getJedis();
			result = jedis.expire(key, exTime);
		} catch (Exception e) {
			log.error("set key:{}", key, e);
			RedisPool.returnBrokenResource(jedis);
		}
		RedisPool.returnJedis(jedis);
		return result;
	}
	
	public static Long del(String key){
		Jedis jedis = null;
		Long result = null;
		
		try {
			jedis = RedisPool.getJedis();
			result = jedis.del(key);
		} catch (Exception e) {
			log.error("del key:{}", key, e);
			RedisPool.returnBrokenResource(jedis);
		}
		RedisPool.returnJedis(jedis);
		return result;
	}
	
	 public static void main(String[] args) {
	        //Jedis jedis = RedisPool.getJedis();

	       String value = RedisPoolUtil.set("1", "2");
	       
	       System.out.println(value);


	    }
}
