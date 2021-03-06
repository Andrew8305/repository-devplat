package org.cisiondata.utils.redis;

import java.io.IOException;

import org.cisiondata.utils.serde.SerializerUtils;
import org.cisiondata.utils.spring.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;

public class RedisClusterUtils {
	
	private Logger LOG = LoggerFactory.getLogger(RedisClusterUtils.class);
	
	private JedisCluster jedisCluster = null;

	private RedisClusterUtils() {
		this.jedisCluster = (JedisCluster) SpringBeanFactory.getBean("jedisCluster");
	}

	private static class RedisClusterUtilsHolder {
		public static RedisClusterUtils INSTANCE = new RedisClusterUtils();
	}

	public static RedisClusterUtils getInstance() {
		return RedisClusterUtilsHolder.INSTANCE;
	}
	
	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	/**
	 * 缓存数据
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		try {
			jedisCluster.set(SerializerUtils.write(key), SerializerUtils.write(value));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 缓存数据并设置过期时间
	 * @param key
	 * @param value
	 * @param expireTime
	 */
	public void set(String key, Object value, int expireTime) {
		try {
			byte[] cache_key = SerializerUtils.write(key);
			jedisCluster.set(cache_key, SerializerUtils.write(value));
			jedisCluster.expire(cache_key, expireTime);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 读取数据
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		try {
			byte[] value = jedisCluster.get(SerializerUtils.write(key));
			if (null != value && value.length != 0) {
				return SerializerUtils.read(value);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 刪除KEY
	 * @param key
	 * @return
	 */
	public Long delete(String key) {
		return jedisCluster.del(key);
	}
	
	/**
	 * 新增SET数据集合
	 * @param key
	 * @param members
	 * @return
	 */
	public Long sadd(String key, String... members) {
		return jedisCluster.sadd(key, members);
	}
	
	/**
	 * 删除SET数据集合
	 * @param key
	 * @param members
	 * @return
	 */
	public Long srem(String key, String... members) {
		return jedisCluster.srem(key, members);
	}
	
	/**
	 * SET数据集合成员是否存在
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean sismember(String key, String member) {
		return jedisCluster.sismember(key, member);
	}
	
	/**
	 * SET数据集合长度
	 * @param key
	 * @return
	 */
	public long scard(String key) {
		return jedisCluster.scard(key);
	}

}
