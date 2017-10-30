package com.gj.frameworks.cache.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.gj.frameworks.cache.CacheClient;


public class StringRedisCacheClient implements CacheClient<String> {

	private StringRedisTemplate stringRedisTemplate;

	public StringRedisCacheClient(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	public String get(String key) {
		return stringRedisTemplate.boundValueOps(key).get();
	}

	@Override
	public String getAndTouch(String key, int seconds) {
		stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
		return get(key);
	}

	@Override
	public boolean touch(String key, int seconds) {
		return stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
	}

	@Override
	public boolean add(String key, String data) {
		return add(key, data, ONEWEEK);
	}

	@Override
	public boolean add(String key, String data, int seconds) {
		boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, data);
		if (result) {
			stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
		}
		return result;
	}

	@Override
	public boolean set(String key, String data) {
		stringRedisTemplate.opsForValue().set(key, data, ONEWEEK, TimeUnit.SECONDS);
		return true;
	}

	@Override
	public boolean set(String key, String data, int seconds) {
		stringRedisTemplate.opsForValue().set(key, data, seconds, TimeUnit.SECONDS);
		return true;
	}

	@Override
	public boolean delete(String key) {
		stringRedisTemplate.delete(key);
		return true;
	}

	@Override
	public boolean replace(String key, String data) {
		return replace(key, data, ONEWEEK);
	}

	@Override
	public boolean replace(String key, String data, int seconds) {
		if (stringRedisTemplate.hasKey(key)) {
			stringRedisTemplate.opsForValue().set(key, data, seconds, TimeUnit.SECONDS);
			return true;
		}
		return false;
	}

	@Override
	public Long ttl(String key) {
		return stringRedisTemplate.getExpire(key);
	}

}
