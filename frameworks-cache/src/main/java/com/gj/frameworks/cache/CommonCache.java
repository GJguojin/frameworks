package com.gj.frameworks.cache;

import org.springframework.data.redis.core.RedisTemplate;

import com.gj.frameworks.cache.redis.RedisCacheClient;


/**
 * 通用CacheClient
 */
public class CommonCache extends RedisCacheClient<CacheableData> {

	public CommonCache(RedisTemplate<String, Object> redisTemplate) {
		super(redisTemplate);
	}

	/**
	 * 添加cache到默认group中
	 * 
	 * @param key
	 * @param object
	 * @param ttl
	 */
	public void cache(String key, Object object, int ttl) {
		this.cache(CacheableData.GROUP, key, object, ttl);
	}
	/**
	 * 添加 Cache到指定group中
	 * 
	 * @param group
	 * @param key
	 * @param object
	 * @param ttl
	 */
	public void cache(String group, String key, Object object, int ttl) {
		CacheableData cacheable = new CacheableData(key, object, group);
		super.set(cacheable.getCacheKey(), cacheable, ttl);
	}

	/**
	 * 从默认group中获取cache对象的值
	 * @param key
	 * @return Cacheable中保存的对象
	 */
	public Object getData(String key) {
		return this.getData(CacheableData.GROUP,key);
	}
	/**
	 * 从指定group中获取cache对象的值
	 * @param group
	 * @param key
	 * @return Cacheable中保存的对象
	 */
	public Object getData(String group, String key) {
		String cachekey = getCacheKey(group, key);
		CacheableData data = super.get(cachekey);
		return data == null ? null : data.getValue();
	}
	
	/**
	 * 从默认group中删除cache
	 * @param key
	 * @return
	 */
	public boolean remove(String key) {
		return this.remove(CacheableData.GROUP,key);
	}
	/**
	 * 删除指定group中cache
	 * @param group
	 * @param key
	 * @return
	 */
	public boolean remove(String group, String key) {
		String cachekey = getCacheKey(group, key);
		return super.delete(cachekey);
	}

	public Long ttl(String group, String key) {
		return super.ttl(getCacheKey(group, key));
	}

	private static String getCacheKey(String group, String key) {
		if (group == null) {
			group = CacheableData.GROUP;
		}
		return CacheKeyStrategy.getCacheKey(key, CacheableData.NAMESPACE, group);
	}
}
