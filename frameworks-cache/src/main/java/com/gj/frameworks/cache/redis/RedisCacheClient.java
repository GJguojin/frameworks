package com.gj.frameworks.cache.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.gj.frameworks.cache.CacheClient;
import com.gj.frameworks.cache.Cacheable;

public class RedisCacheClient<T extends Object> implements CacheClient<T>{

	private RedisTemplate<String, Object> redisTemplate;

	public RedisCacheClient(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public T get(String key) {
		return (T) redisTemplate.boundValueOps(key).get();
	}

	@Override
	public T getAndTouch(String key, int seconds) {
		redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
		return get(key);
	}

	@Override
	public boolean touch(String key, int seconds) {
		return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
	}

	@Override
	public boolean add(String key, T data) {
		return add(key,data,ONEWEEK);
	}

	@Override
	public boolean add(String key, T data, int seconds) {
		boolean result = redisTemplate.opsForValue().setIfAbsent(key, data);
		if(result){
			redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
		}
		return result;
	}

	@Override
	public boolean set(String key, T data) {
		return set(key,data,ONEWEEK);
	}

	@Override
	public boolean set(String key, T data, int seconds) {
		redisTemplate.opsForValue().set(key, data, seconds, TimeUnit.SECONDS);
		return true;
	}

	@Override
	public boolean delete(String key) {
		redisTemplate.delete(key);
		return true;
	}

	@Override
	public boolean replace(String key, T data) {
		return replace(key, data, ONEWEEK);
	}

	@Override
	public boolean replace(String key, T data, int seconds) {
		if(redisTemplate.hasKey(key)){
			redisTemplate.opsForValue().set(key, data, seconds, TimeUnit.SECONDS);
			return true;
		}
		return false;
	}

	@Override
	public Long ttl(String key) {
		return redisTemplate.getExpire(key);
	}

}
