package com.gj.frameworks.shiro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Data.
 */
@ConfigurationProperties(prefix = "spring.redis")
public class ShiroProperties{
	
	private int timeout = 600000;//session过期时间
	
	private String cache ="ehcache"; //使用cache  默认ehcache

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}
	
}