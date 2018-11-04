package com.systemDemo.data;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.systemDemo.anno.SystemLogAspect;

import redis.clients.jedis.Jedis;

public class JedisUtil {
	//本地异常日志记录对象    
    private  static  final Logger logger = Logger.getLogger(SystemLogAspect. class);    

/*
	// 单例，延迟加载
	private static class SingletonHolder {
		static {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(Config.JEDISPOOL_MAXTOTAL);
			config.setMaxIdle(Config.JEDISPOOL_MAXAIDLE);
			pool = new JedisPool(config, Config.REDIS_HOST, Config.REDIS_PORT, Config.JEDISPOOL_TIMEOUT, null,
					Config.REDIS_DATABASE_NO);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					pool.destroy();
				}
			});
		}
	}
*/


	// 从连接池中取出一个连接
	private static Jedis getJedis() {
		//return SingletonHolder.pool.getResource();
		return RedisCacheConf.jedisPool.getResource();
	}

	// 关闭连接
	private static void closeJedis(Jedis jedis) {
		if (null != jedis) {
			jedis.close();
		}
	}

	public static Long getTTL(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.ttl(key);
		} catch (Exception e) {
			logger.error("redis error ", e);
			return null;
		} finally {
			closeJedis(jedis);
		}
	}

	public static boolean setMap(String key, Map<String, String> map) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.hmset(key, map);
			return true;
		} catch (Exception e) {
			logger.error("redis error ", e);
		} finally {
			closeJedis(jedis);
		}
		return false;
	}

	
	public static boolean setMap(String key, Map<String, String> map, int valid) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.hmset(key, map);
			jedis.expire(key, valid);
			return true;
		} catch (Exception e) {
			logger.error("redis error ", e);
		} finally {
			closeJedis(jedis);
		}
		return false;
	}
	
	// 取出hashMap
	public static Map<String, String> getMap(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error("redis error ", e);
		} finally {
			closeJedis(jedis);
		}
		return null;
	}




	public static JSONObject getJsonObj(String key) {
		String dataString = JedisUtil.get(key);
		if (dataString == null) {
			return null;
		}
		try {
			return JSON.parseObject(dataString, JSONObject.class);
		} catch (Exception e) {
			// 这里可能解析dataString并不是json字符串
			return null;
		}
	}


	/**
	 * 设置缓存
	 * 
	 * @param key
	 * @param val
	 */
	public static boolean set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error("redis error , the key is " + key + " and the value is " + value, e);
			return false;
		} finally {
			closeJedis(jedis);
		}
		return true;
	}


	public static boolean set(String key, String value, int valid) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
			jedis.expire(key, valid);
		} catch (Exception e) {
			logger.error("redis error , the key is " + key + " and the value is " + value, e);
			return false;
		} finally {
			closeJedis(jedis);
		}
		return true;
	}

	// 根据key取出值
	public static String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = getJedis();
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error("redis error,the key is " + key, e);
		} finally {
			closeJedis(jedis);
		}
		return value;
	}

	// 删除指定的键值
	public static void remove(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.del(key);
		} catch (Exception e) {
			logger.error("redis error ", e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	
	/**
	 * 批量删除
	 * @param jedis
	 */
	static void batchRemove(String keyPrefix) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Set<String> set = jedis.keys(keyPrefix + "*");
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String keyStr = it.next();
				jedis.del(keyStr);
			}
		} catch (Exception e) {
			logger.error("redis error ", e);
		} finally {
			closeJedis(jedis);
		}
	}

	public static boolean setExpire(String key, int valid) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.expire(key, valid);
			return true;
		} catch (Exception e) {
			logger.error("redis error ", e);
			return false;
		} finally {
			closeJedis(jedis);
		}
	}

	public static Set<String> getKeys(String keys) {
		Jedis jedis = null;
		Set<String> value = null;
		try {
			jedis = getJedis();
			value = jedis.keys(keys);
		} catch (Exception e) {
			logger.error("redis error,the key is " + keys, e);
		} finally {
			closeJedis(jedis);
		}
		return value;
	}

	// 取出hashMap中对应字段的值
	public static String getFieldValue(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hget(key, field);
		} catch (Exception e) {
			logger.error("redis error ", e);
		} finally {
			closeJedis(jedis);
		}
		return null;
	}

}

