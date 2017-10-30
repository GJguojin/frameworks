package com.gj.frameworks.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = CacheProperties.PREFIX)
public class CacheProperties {
	
	public static final String PREFIX = "gj.cache";

}
