package com.gj.frameworks.cache.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.gj.frameworks.cache.CommonCache;
import com.gj.frameworks.cache.serializer.FSTSerializer;


@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@Import(RedisConnectionConfiguration.class)
public class CacheAutoConfiguration {

	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setDefaultSerializer(redisSerializer());
		return template;
	}
	
	
	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean(name = "commonCache")
	public CommonCache commonCache(RedisTemplate<String, Object> redisTemplate) {
		return new CommonCache(redisTemplate);
	}

	@Bean
	public RedisSerializer redisSerializer() {
//		 ObjectMapper mapper = new ObjectMapper();
//		 mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
//		 return new GenericJackson2JsonRedisSerializer(mapper);
		 return new FSTSerializer();
	}

}
