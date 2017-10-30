package com.gj.frameworks.cache.memcached;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gj.frameworks.cache.CacheClient;

import net.rubyeye.xmemcached.MemcachedClient;

public class MemcacheClient<T> implements CacheClient<T> {

private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final MemcachedClient memCachedClient;

	public MemcacheClient(MemcachedClient memCachedClient) {
		this.memCachedClient = memCachedClient;
	}
	@Override
	public T get(String key) {
		try {
			return (T) memCachedClient.get(key);
		} catch (Exception e) {
			logger.warn("get {} from cache error:"+e.getMessage(),key);
			return null;
		}
	}
	@Override
	public T getAndTouch(String key,int seconds) {
		try {
			return (T) memCachedClient.getAndTouch(key,seconds);
		} catch (Exception e) {
			logger.warn("getAndTouch {} from cache error:"+e.getMessage(),key);
			return null;
		}
	}
	
	@Override
	public boolean touch(String key,int seconds) {
		try {
			return memCachedClient.touch(key,seconds);
		} catch (Exception e) {
			logger.warn("touch {} to cache error:"+e.getMessage(),key);
			return false;
		}
	}

	@Override
	public boolean add(String key, T value) {
		return add(key,value,ONEWEEK);
	}

	@Override
	public boolean add(String key, T value, int seconds) {
		try {
			return memCachedClient.add(key,seconds,value);
		} catch (Exception e) {
			logger.warn("add {} to cache error:"+e.getMessage(),key);
			return false;
		}
	}

	@Override
	public boolean set(String key, T value) {
		return set(key, value,ONEWEEK);
	}

	@Override
	public boolean set(String key, T value, int seconds) {
		try {
			return memCachedClient.set(key,seconds, value);
		} catch (Exception e) {
			logger.warn("set {} to cache error:"+e.getMessage(),key);
			return false;
		}
	}

	@Override
	public boolean delete(String key) {
		try {
			return memCachedClient.delete(key);
		} catch (Exception e) {
			logger.warn("delete {} from cache error:"+e.getMessage(),key);
			return false;
		}
	}

	@Override
	public boolean replace(String key, T value) {
		return replace(key,value,ONEWEEK);
	}
	
	@Override
	public boolean replace(String key, T value,int seconds) {
		try {
			return memCachedClient.replace(key,seconds, value);
		} catch (Exception e) {
			logger.warn("replace {} in cache error:"+e.getMessage(),key);
			return false;
		}
	}
	@Override
	public Long ttl(String key) {
		// TODO 
		return null;
	}

}
